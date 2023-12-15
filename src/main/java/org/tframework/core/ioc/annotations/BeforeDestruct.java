/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a method to be called before the managed class is destructed. Ideal place for releasing
 * resources, closing connections and so on. Multiple methods of a managed class can be annotated
 * with this.
 * <p>
 * Methods annotated with this must be non-static, have no parameters and a void return type. Method visibility is
 * not limited to public.
 */
@Target(ElementType.METHOD)
public @interface BeforeDestruct {}
