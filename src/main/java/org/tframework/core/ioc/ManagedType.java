/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

/** The possible types of managed classes. */
public enum ManagedType {

  /**
   * Manage only one instance of the class, which will be injected to every place and returned by
   * every grab.
   */
  SINGLETON,

  /**
   * Manage as many instances as requested. Every time the class is injected or grabbed directly, a
   * new instance is constructed.
   */
  MULTI_INSTANCE
}
