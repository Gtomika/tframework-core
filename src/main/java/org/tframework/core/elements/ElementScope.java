/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

/**
 * Enum for all supported element scopes. The element scope defines the lifecycle of an element.
 */
public enum ElementScope {

    /**
     * The element will be created once at initialization, and will be reused for all injections.
     */
    SINGLETON,

    /**
     * The element will be created once for each injection, only when it is needed.
     */
    PROTOTYPE;

}
