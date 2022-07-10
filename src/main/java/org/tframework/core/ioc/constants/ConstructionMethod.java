/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.constants;

/**
 * Possible ways of constructing a managed entity, including not constructible.
 */
public enum ConstructionMethod {

    /**
     * Indicates that the managed entity should be constructed with a public,
     * no argument constructor.
     */
    PUBLIC_NO_ARGS_CONSTRUCTOR,

    /**
     * Means that the managed entity is to be created by calling a 'provider' method. This is how external
     * classes can be made managed.
     */
    PROVIDER,

    /**
     * Indicates that no supported way was found to construct the managed entity.
     */
    NOT_CONSTRUCTIBLE
}
