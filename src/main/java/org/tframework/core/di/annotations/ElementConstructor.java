/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a constructor as the one to be used when instantiating the element.
 * It is required only to when there are multiple constructors available, and the framework otherwise
 * wouldn't know which one to use.
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementConstructor {
}
