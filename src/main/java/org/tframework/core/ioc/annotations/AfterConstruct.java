/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a method to be run after the managed class is constructed. This is ideal for
 * initializations. A managed class can have multiple methods annotated with this.
 */
@Target(ElementType.METHOD)
public @interface AfterConstruct {}
