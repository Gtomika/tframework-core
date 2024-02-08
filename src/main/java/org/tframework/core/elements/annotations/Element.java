/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.annotations;

import org.tframework.core.elements.ElementScope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to mark a class as an element of the dependency injection. An element
 * is managed by the framework, and can be injected into other elements.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {

    String NAME_NOT_SPECIFIED = "";

    /**
     * The unique name of the element. If this is not specified, it will default to the name of the
     * class that is annotated with this annotation. In case of a method is annotated, the
     * name of the return value class will be used.
     */
    String name() default NAME_NOT_SPECIFIED;

    /**
     * The scope of the element. By default, it is {@link ElementScope#SINGLETON}.
     * @see ElementScope
     */
    ElementScope scope() default ElementScope.SINGLETON;

}
