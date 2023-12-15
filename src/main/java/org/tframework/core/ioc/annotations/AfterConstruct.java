/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a method to be run after the managed entity is created and the dependencies are injected. This is ideal for
 * initializations. A managed class can have multiple methods annotated with this.
 * <p>
 * Methods annotated with this must be non-static, have no parameters and a void return type. Method visibility is
 * not limited to public.
 */
@Target(ElementType.METHOD)
public @interface AfterConstruct {}
