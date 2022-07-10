package org.tframework.core.ioc;

import lombok.experimental.NonFinal;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.tframework.core.ioc.constants.ConstructionMethod;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is responsible for creating instances of the managed classes. There are multiple ways
 * how a managed entity can be constructed, see {@link ConstructionMethod}.
 */
public class ManagedEntityConstructor<T> {

    /**
     * Stores how the managed entity is constructed. This is determined right when the
     * {@link ManagedEntityConstructor} object is created.
     */
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
    private final Method providerMethod;

    /**
     * Create a managed entity constructor from a given class.
     * @param managedEntity The class that must be constructed. It will be investigated
     *                      with {@link #determineConstructionMethod()}, and an appropriate
     *                      way will be determined how new instances can be created.
     * @throws NotConstructibleException If no way was found to construct this entity.
     */
    public ManagedEntityConstructor(Class<T> managedEntity) throws NotConstructibleException {
        this.managedEntity = managedEntity;
        this.providerMethod = null;
        constructionMethod = determineConstructionMethod();
        if(constructionMethod == ConstructionMethod.NOT_CONSTRUCTIBLE) {
            throw new NotConstructibleException(managedEntity);
        }
    }

    /**
     * Create a managed entity constructor from a given class and a provider method.
     * @param managedEntity The class that new instances must be constructed from. It will NOT be investigated
     *                      for ways to be constructed, because such way is already provided by the provided method.
     * @param providerMethod A method that returns the same type as 'managedEntity'. Must not be null, if you don't
     *                       need provider method, use the other constructor.
     * @throws IllegalArgumentException If the 'providerMethod' cannot be used to construct instances of 'managedEntity'.
     */
    public ManagedEntityConstructor(Class<T> managedEntity, @Nonnull Method providerMethod) throws IllegalArgumentException {
        this.managedEntity = managedEntity;
        constructionMethod = ConstructionMethod.PROVIDER;
        this.providerMethod = providerMethod;
        IocValidator.validateProviderMethod(providerMethod, managedEntity);
    }

    /**
     * Create an instance of the managed entity using one of the {@link ConstructionMethod}s.
     * @return Instance.
     * @throws NotConstructibleException If this entity cannot be constructed with any supported ways.
     */
    public T constructManagedEntity() throws NotConstructibleException {
        switch (constructionMethod) {
            case PUBLIC_NO_ARGS_CONSTRUCTOR:
                return constructWithPublicNoArgsConstructor();
            case PROVIDER:
                //TODO: resolve managed class where this method is. resolve parameters
                return constructWithPublicNoArgsConstructor(); //TODO remove duplicate case
            default:
                throw new NotConstructibleException(managedEntity);
        }
    }

    /**
     * Investigates a class and determines the preferred way to create an instance of it. If no available
     * way is found, then {@link ConstructionMethod#NOT_CONSTRUCTIBLE} is returned.
     * @return Preferred construction method.
     */
    private ConstructionMethod determineConstructionMethod() {
        Constructor<T> publicNoArgs = ConstructorUtils.getMatchingAccessibleConstructor(managedEntity);
        return publicNoArgs != null ? ConstructionMethod.PUBLIC_NO_ARGS_CONSTRUCTOR : ConstructionMethod.NOT_CONSTRUCTIBLE;
    }

    /**
     * Construct a managed entity with a public, no argument constructor. This should only be called after it
     * was validated that this entity can be constructed as such.
     * @return Instance of the class.
     * @throws NotConstructibleException If an exception happened during construction.
     */
    private T constructWithPublicNoArgsConstructor() throws NotConstructibleException {
        Constructor<T> publicNoArgs = ConstructorUtils.getMatchingAccessibleConstructor(managedEntity);
        try {
            return publicNoArgs.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new NotConstructibleException(managedEntity, e);
        }
    }
}
