/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.tframework.core.elements.ElementScope;

/**
 * This annotation is used to mark a class as an element of the dependency injection. An element
 * is managed by the framework, and can be injected into other elements. There are a few ways to
 * declare elements with this annotation. It can be placed on a class:
 *
 * <pre>{@code
 * @Element
 * public class MyElement {}
 * }</pre>
 *
 * In cases where you have no access to the class source code, it can be placed on a method of another
 * element, provided it fulfills these requirements:
 * <ul>
 *     <li>The method must be public (to be able to invoke it).</li>
 *     <li>The method must be non-static (because it will be called as an instance method of the parent element).</li>
 *     <li>The method must have not void return type (because the returned value will be the element instance).</li>
 * </ul>
 * For example:
 *
 * <pre>{@code
 * @Element
 * public class MyElement {
 *
 *     @Element
 *     public String provideStringElement() {
 *         return "Hello";
 *     }
 * }
 * }</pre>
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
