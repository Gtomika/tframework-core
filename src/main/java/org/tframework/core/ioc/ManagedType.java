/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

/**
 *  The possible types of managed classes. This type defines the
 * lifecycle of the managed entity.
 */
public enum ManagedType {

    /**
     * Manage only one instance of the entity, which will be injected to every place and returned by
     * every request.
     */
    SINGLETON,

    /**
     * Manage as many instances as requested. Every time the entity is injected or requested directly,
     * a new instance is constructed.
     */
    CREATE_WHEN_REQUESTED
}
