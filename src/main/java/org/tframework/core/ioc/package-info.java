/**
 * Contains the classes and annotation related to the TFramework inversion of control (dependency
 * injection).
 * <p><p>
 * Each class that you want to be managed by TFramework IoC must be
 * <ul>
 *   <li>
 *       Annotated with {@link org.tframework.core.ioc.annotations.Managed}. This will mark the
 *       class to be picked up by the framework.
 *   </li>
 *   <li>
 *       Constructable by the framework. There are multiple way to achieve this, see {@link org.tframework.core.ioc.ManagedEntityConstructor}.
 *   </li>
 * </ul>
 * <p><p>
 * Another way to make entities managed is by provider methods. This is useful when we don't have access to the
 * source class. To do this, create a valid provider method (inside a managed entity!) and mark it with
 * {@link org.tframework.core.ioc.annotations.Managed}.
 * <p><p>
 * Managed classes can have different types, which controls how many of the are constructed. The
 * default is 'singleton', which means only one instance ever exists.
 */
package org.tframework.core.ioc;
