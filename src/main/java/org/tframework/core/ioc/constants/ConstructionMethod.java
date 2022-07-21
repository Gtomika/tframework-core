/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.constants;

/**
 * Possible ways of constructing a managed entity, including not constructible.
 */
public enum ConstructionMethod {

    /**
     * Means that the managed entity is to be created by calling a 'provider' method. This is how external
     * classes can be made managed. This kind of construction may have dependencies, which must be passed to
     * the provider method as parameters either not annotated or annotated with {@link org.tframework.core.ioc.annotations.Injected}.
     * This construction method has the highest priority, meaning if there is a provider method it will be used over constructors.
     */
    PROVIDER,

    /**
     * Indicates that the managed entity should be constructed with a public constructor. It may or may not have parameters.
     * These parameters will be dependencies (if not annotated or annotated with {@link org.tframework.core.ioc.annotations.Injected})
     * or properties. In priority, this is below {@link #PROVIDER}.
     */
    PUBLIC_CONSTRUCTOR,

    /**
     * Indicates that no supported way was found to construct the managed entity.
     */
    NOT_CONSTRUCTIBLE
}
