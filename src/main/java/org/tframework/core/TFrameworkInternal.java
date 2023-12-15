/* Licensed under Apache-2.0 2023. */
package org.tframework.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks that a component should only be used internally. If placed on a class, it means that all
 * methods of that class are considered internal.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TFrameworkInternal {
}
