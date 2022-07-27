package org.tframework.core.ioc.containers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.annotations.TFrameworkInternal;
import org.tframework.core.ioc.DependencyInformation;
import org.tframework.core.ioc.ManagedEntityConstructor;
import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for containers that wrap managed entities.
 */
@Slf4j
public abstract class AbstractContainer<T> {

    /**
     * Managing type of the entity wrapped by this container.
     */
    @Getter
    protected final ManagingType managingType;

    /**
     * Name of the entity wrapped by this container. Unique across all entities.
     */
    @Getter
    protected final String name;

    /**
     * Actual instance of the managed entity.
     */
    @Getter
    protected final Class<T> instanceType;

    /**
     * Object responsible for creating new instances of the managed entity.
     */
    @Getter
    protected final ManagedEntityConstructor<T> managedEntityConstructor;

    /**
     * Contains the data about the dependencies of this managed entity. This is not a list
     * of actual dependency instances, only information about them.
     */
    protected final List<DependencyInformation<?>> dependencyInformationList;

    /**
     * Stores if the container has a pre-constructed instance.
     */
    @TFrameworkInternal
    @Getter
    private final boolean preConstructed;

    /**
     * The default abstract container constructor.
     * @param managingType {@link ManagingType} of the managed entity.
     * @param name Name of the managed entity.
     * @param instanceType Instance type of the managed entity.
     */
    public AbstractContainer(
            ManagingType managingType,
            String name,
            Class<T> instanceType
    ) {
        this(managingType, name, instanceType, null);
    }

    /**
     * Similar to {@link #AbstractContainer(ManagingType, String, Class)}, but also allows to pass a
     * provider method.
     * @throws IllegalArgumentException If the 'providerMethod' was not null, and it was invalid.
     */
    public AbstractContainer(
            ManagingType managingType,
            String name,
            Class<T> instanceType,
            @Nullable Method providerMethod
    ) throws IllegalArgumentException {
        this.managingType = managingType;
        this.name = name;
        this.instanceType = instanceType;
        this.managedEntityConstructor = new ManagedEntityConstructor<T>(instanceType, providerMethod);
        this.dependencyInformationList = new ArrayList<>();
        this.preConstructed = false;
    }

    /**
     * Special constructor allowing to create abstract container where there is no need to use
     * {@link ManagedEntityConstructor}, because there is already a pre-constructed instance.
     * @param preConstructed Must always be true, it only exists to differentiate this container from the others.
     * @throws IllegalArgumentException If {@code preConstructed} is not true.
     */
    @TFrameworkInternal
    public AbstractContainer(
            ManagingType managingType,
            String name,
            Class<T> instanceType,
            boolean preConstructed
    ) throws IllegalArgumentException {
        if(!preConstructed) throw new IllegalArgumentException("'preConstructed' must be true");
        this.managingType = managingType;
        this.name = name;
        this.instanceType = instanceType;
        this.managedEntityConstructor = null;
        this.dependencyInformationList = new ArrayList<>();
        this.preConstructed = true;
    }

    /**
     * Saves information about one dependency of this managed entity.
     */
    public void addDependency(DependencyInformation<?> dependencyInformation) {
        dependencyInformationList.add(dependencyInformation);
        log.debug("Container '{}' received new dependency '{}'", name, dependencyInformation.getDependencyContainer().getName());
    }

    /**
     * Gets an instance of this managed entity. The container has control of creating a
     * new instance, or using an already existing one.
     * @throws NotConstructibleException If no instance can be constructed and provided.
     */
    public abstract T grabInstance() throws NotConstructibleException;

}
