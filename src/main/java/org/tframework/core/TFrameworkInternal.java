/* Licensed under Apache-2.0 2023. */
package org.tframework.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks that a component should only be used internally inside the framework.
 * <ul>
 *     <li>
 *         If placed on a class, it means that all methods of that class are considered internal.
 *         All subclasses are also internal.
 *     </li>
 *     <li>If placed on an interface, all implementations are internal.</li>
 *     <li>If placed on a package, all contents of that package are internal.</li>
 * </ul>
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE})
public @interface TFrameworkInternal {
}
