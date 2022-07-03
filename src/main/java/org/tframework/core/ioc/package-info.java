/**
 * Contains the classes and annotation related to the TFramework inversion of control (dependency
 * injection).
 *
 * <p>Each class that you want to be managed by TFramework IoC must be
 *
 * <ul>
 *   <li>
 *       Annotated with {@link org.tframework.core.ioc.annotations.Managed}. This will mark the
 *       class to be picked up by the framework.
 *   </li>
 *   <li>
 *       Constructable by the framework. There are multiple way to achieve this.
 *   </li>
 * </ul>
 *
 * <p>Managed classes can have different types, which controls how many of the are constructed. The
 * default is 'singleton', which means only one instance ever exists.
 */
package org.tframework.core.ioc;
