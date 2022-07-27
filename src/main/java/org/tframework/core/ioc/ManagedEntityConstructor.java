package org.tframework.core.ioc;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.ApplicationContext;
import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.constants.ConstructionMethod;
import org.tframework.core.ioc.constants.InjectionType;
import org.tframework.core.ioc.exceptions.InvalidDependencyException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;
import org.tframework.core.ioc.exceptions.NotConstructibleException;
import org.tframework.core.properties.exceptions.PropertyException;
import org.tframework.core.annotations.NeedsTesting;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for creating instances of the managed classes. There are multiple ways
 * how a managed entity can be constructed, see {@link ConstructionMethod}.
 */
@Slf4j
public class ManagedEntityConstructor<T> {

    /**
     * Stores how the managed entity is constructed. This is determined right when the
     * {@link ManagedEntityConstructor} object is created.
     */
    @Getter
    private final ConstructionMethod constructionMethod;

    /**
     * The class that this object must construct instances of.
     */
    private final Class<T> managedEntity;

    /**
     * A method that can be called to get new instances of the managed entity. This only has a value
     * if {@link #constructionMethod} is {@link ConstructionMethod#PROVIDER}, otherwise it is null.
     */
    @Nullable
    @Getter
    private final Method providerMethod;

    /**
     * A constructor selected to create instances of the managed entity. This will only have value if
     * {@link #constructionMethod} is {@link ConstructionMethod#PUBLIC_CONSTRUCTOR}.
     */
    @Nullable
    @Getter
    private Constructor<T> selectedConstructor;

    /**
     * Create a managed entity constructor from a given class.
     * @param managedEntity The class that must be constructed. It will be investigated
     *                      with {@link #determineConstructionMethod()}, and an appropriate
     *                      way will be determined how new instances can be created.
     * @throws NotConstructibleException If no way was found to construct this entity.
     */
    public ManagedEntityConstructor(Class<T> managedEntity) throws NotConstructibleException {
        this(managedEntity, null);
    }

    /**
     * Create a managed entity constructor from a given class and a provider method.
     * @param managedEntity The class that new instances must be constructed from. It will NOT be investigated
     *                      for ways to be constructed, because such way is already provided by the provided method.
     * @param providerMethod A method that returns the same type as 'managedEntity'. Can be null.
     * @throws IllegalArgumentException If the 'providerMethod' cannot be used to construct instances of 'managedEntity'.
     * @throws NotConstructibleException If no way was found to construct this entity.
     */
    public ManagedEntityConstructor(
            Class<T> managedEntity,
            @Nullable Method providerMethod
    ) throws IllegalArgumentException, NotConstructibleException {
        this.managedEntity = managedEntity;
        if(providerMethod != null) {
            constructionMethod = ConstructionMethod.PROVIDER;
            this.providerMethod = providerMethod;
            IocValidator.validateProviderMethod(providerMethod, managedEntity);
            log.debug("Managed entity constructor for class '{}' created with provider method '{}'. It will be used for construction.",
                    managedEntity.getName(), providerMethod.getName());
        } else {
            this.providerMethod = null;
            constructionMethod = determineConstructionMethod();
            if(constructionMethod == ConstructionMethod.NOT_CONSTRUCTIBLE) {
                throw new NotConstructibleException(managedEntity);
            }
            log.debug("Managed entity constructor created for class '{}'. Determined construction method is '{}'.",
                    managedEntity.getName(), constructionMethod);
        }
    }

    /**
     * Create an instance of the managed entity using one of the {@link ConstructionMethod}s.
     * @param dependencies The list of dependencies that this managed entity has. They may not be needed during construction,
     *                     or it is also possible that only some of them will be needed.
     * @return Instance.
     * @throws NotConstructibleException If this entity cannot be constructed with any supported ways.
     */
    public T constructManagedEntity(List<DependencyInformation<?>> dependencies) throws NotConstructibleException {
        switch (constructionMethod) {
            case PUBLIC_CONSTRUCTOR:
                return constructWithPublicConstructor(dependencies);
            case PROVIDER:
                return constructWithProviderMethod(dependencies);
            default:
                throw new NotConstructibleException(managedEntity);
        }
    }

    /**
     * Investigates a class and determines the preferred way to create an instance of it. If no available
     * way is found, then {@link ConstructionMethod#NOT_CONSTRUCTIBLE} is returned.
     * @return Preferred construction method.
     * @throws NotConstructibleException If the class is in an invalid state, for example multiple available constructor is present.
     */
    private ConstructionMethod determineConstructionMethod() throws NotConstructibleException {
        //the provider method takes precedence.
        if(providerMethod != null) {
            return ConstructionMethod.PROVIDER;
        }
        //then looking for a constructor
        selectedConstructor = findAppropriateConstructor();
        return selectedConstructor != null ? ConstructionMethod.PUBLIC_CONSTRUCTOR : ConstructionMethod.NOT_CONSTRUCTIBLE;
    }

    /**
     * Attempts to find a constructor with parameters that can be used to create instances of this class.
     * @return The constructor that can be used or null if no such was found.
     * @throws NotConstructibleException If the class is in an invalid state, for example multiple available constructor is present.
     */
    @Nullable
    private Constructor<T> findAppropriateConstructor() throws NotConstructibleException {
        List<Constructor<?>> constructors = Arrays.asList(managedEntity.getConstructors());
        //look for constructor annotated with @Injected first -> this takes precedence
        List<Constructor<?>> annotatedConstructors = constructors.stream()
                .filter(c -> c.isAnnotationPresent(Injected.class))
                .collect(Collectors.toList());
        if(annotatedConstructors.size() > 1) {
            throw NotConstructibleException.customNotConstructibleException(String.format("On managed entity with class '%s': " +
                    "multiple constructors found annotated with @Injected. Unable to select one. Have @Injected annotation on at most " +
                    "1 public constructor.", managedEntity.getName()));
        }
        if(annotatedConstructors.size() == 1) {
            return (Constructor<T>) annotatedConstructors.get(0);
        }
        //no constructor annotated with @Injected, select from rest
        if(constructors.size() > 1) {
            throw NotConstructibleException.customNotConstructibleException(String.format("On managed entity with class '%s': " +
                    "multiple constructors found. Unable to select one for construction. Have only one constructor or select " +
                    "one of the with @Injected.", managedEntity.getName()));
        }
        if(constructors.size() == 0) {
            return null;
        }
        return (Constructor<T>) constructors.get(0);
    }

    /**
     * Construct a managed entity with a public constructor.
     * @param dependencies All dependencies of the managed entity. It is up to this method to determine which
     *                     ones are needed for the provider method.
     * @return Instance of the class.
     * @throws NotConstructibleException If an exception happened during construction.
     */
    private T constructWithPublicConstructor(List<DependencyInformation<?>> dependencies) throws NotConstructibleException {
        if(selectedConstructor == null) {
            log.error("Attempted to construct managed entity class '{}' with constructor, but there is no valid constructor available",
                    managedEntity.getName());
            throw new NotConstructibleException(managedEntity);
        }
        try {
            log.debug("Attempting to get instances of the parameters for the constructor of the managed entity with class '{}'.",
                    managedEntity.getName());
            Object[] constructorParameters = getInstancesOfExecutableParameters(selectedConstructor, dependencies);
            return selectedConstructor.newInstance(constructorParameters);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new NotConstructibleException(managedEntity, e);
        }
    }

    /**
     * Create an instance by calling a provider method.
     * @param dependencies All dependencies of the managed entity. It is up to this method to determine which
     *                     ones are needed for the provider method.
     * @return The constructed entity.
     */
    private T constructWithProviderMethod(List<DependencyInformation<?>> dependencies) {
        if(providerMethod == null) {
            log.error("Attempted to construct managed entity class '{}' with provider method, but there is no such method available",
                    managedEntity.getName());
            throw new NotConstructibleException(managedEntity);
        }
        try {
            log.debug("Attempting to get an instance of the managed entity that declares the provider method '{}'",
                    providerMethod.getName());
            Object providerDeclaringInstance = getInstanceOfProviderMethodDeclaringEntity(providerMethod);
            log.debug("Attempting to get instances of the parameters for the provider method of the managed entity with class '{}'.",
                    managedEntity.getName());
            Object[] providerMethodParameters = getInstancesOfExecutableParameters(providerMethod, dependencies);
            return (T) providerMethod.invoke(providerDeclaringInstance, providerMethodParameters);
        } catch (InvocationTargetException | IllegalAccessException | InvalidDependencyException | ClassCastException e) {
            throw new NotConstructibleException(managedEntity, e);
        }
    }

    /**
     * Finds an instance of the class that declares the provider method. This is needed, because
     * the provider method can't be static.
     * @throws InvalidDependencyException If it was not possible ot return the instance.
     */
    @NeedsTesting
    private Object getInstanceOfProviderMethodDeclaringEntity(Method providerMethod) throws InvalidDependencyException {
        String declaringEntityName = IocUtils.getReferencedEntityName(providerMethod.getDeclaringClass());
        try {
            var declaringEntityContainer = ApplicationContext.getInstance().getTFrameworkIoc()
                    .getManagedEntitiesRepository().grabManagedEntityContainer(declaringEntityName);
            log.debug("The provider method '{}' is declared in the managed entity '{}'.",
                    providerMethod.getName(), declaringEntityContainer.getName());
            return declaringEntityContainer.grabInstance();
        } catch (NoSuchManagedEntityException e) {
            log.error("Internal error: Failed to get an instance of the managed entity that declares the provider method '{}'.",
                    providerMethod.getName());
            throw new InvalidDependencyException(providerMethod.getName(), declaringEntityName, e);
        }
    }

    /**
     * Finds the instances of parameters of a provider method or constructor
     * @param executable The provider method or constructor.
     * @param dependencies List of all dependencies. This method must select the ones needed for the parameters.
     * @return The instances of the parameters, in the order they are expected by the method or constructor.
     * @throws NotConstructibleException If the parameters cannot be constructed.
     */
    @NeedsTesting
    private Object[] getInstancesOfExecutableParameters(
            Executable executable,
            List<DependencyInformation<?>> dependencies
    ) throws NotConstructibleException {
        Object[] parameterInstances = new Object[executable.getParameterCount()];
        Parameter[] expectedParameters = executable.getParameters();

        InjectionType injectionType = executable instanceof Constructor ? InjectionType.CONSTRUCTOR_INJECTION :
                InjectionType.PROVIDER_INJECTION;
        List<DependencyInformation<?>> parameterDependencies = dependencies.stream()
                .filter(dependency -> dependency.getInjectionType() == injectionType)
                .collect(Collectors.toList());

        log.debug("Resolving parameters of executable '{}'. Expected parameter count is '{}', found '{}' dependencies that " +
                "can be injected using '{}'.", executable.getName(), expectedParameters.length, parameterDependencies.size(), injectionType);

        //all parameters must be accounted for
        if(parameterInstances.length != parameterDependencies.size()) {
            log.error("Internal error: mismatch of expected and resolved parameter count, executable '{}'", executable.getName());
            throw new InvalidDependencyException(String.format("Internal error: the executable '%s' expects '%d' parameters, " +
                    "but only '%d' have resolved dependencies.", executable.getName(), parameterInstances.length, parameterDependencies.size()));
        }

        for(int i = 0; i < parameterInstances.length; i++) {
            try {
                //check if this is the expected parameter
                if(!expectedParameters[i].equals(parameterDependencies.get(i).getInjectedParameter())) {
                    throw NotConstructibleException.customNotConstructibleException(String.format("Executable '%s', parameter " +
                            "'%s': mismatch between provided and expected parameter.", executable.getName(), expectedParameters[i].getName()));
                }
                Object parameterInstance = parameterDependencies.get(i).getDependencyContainer().grabInstance();
                parameterInstances[i] = parameterInstance;
                log.debug("Executable '{}', resolved the parameter '{}' using an object of type '{}'",
                        executable.getName(), expectedParameters[i].getName(), parameterInstance.getClass().getName());
            } catch (PropertyException e) {
                var exc = NotConstructibleException.customNotConstructibleException(String.format("Executable '%s', parameter " +
                        "'%s': no instance for this parameter could be constructed.", executable.getName(), expectedParameters[i].getName()));
                exc.initCause(e);
                throw exc;
            }
        }

        return parameterInstances;
    }
}
