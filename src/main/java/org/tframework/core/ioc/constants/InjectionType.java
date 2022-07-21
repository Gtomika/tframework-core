/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.constants;

/**
 * Constants for the possible ways that a dependency can be injected.
 */
public enum InjectionType {

    /**
     * Injection that happens when a field of a managed entity is
     * annotated with {@link org.tframework.core.ioc.annotations.Injected}.
     */
    FIELD_INJECTION,

    /**
     * Injection that happens when a parameter of a constructor isn't annotated or is annotated
     * with {@link org.tframework.core.ioc.annotations.Injected}.
     */
    CONSTRUCTOR_INJECTION,

    /**
     * Injection that happens when a provider method has parameters that are not annotated or are
     * annotated with {@link org.tframework.core.ioc.annotations.Injected}.
     */
    PROVIDER_INJECTION;

}
