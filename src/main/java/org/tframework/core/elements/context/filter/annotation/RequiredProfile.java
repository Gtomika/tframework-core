/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Can be placed on element classes or methods, to declare that the element
 * requires a profile (or profiles) to be activated. For example, if we want
 * {@code SomeElement} to be only active when profiles {@code a} and {@code b} are both set:
 *
 * <pre>{@code
 * @Element
 * @ForbiddenProfile({"a", "b"})
 * public class SomeElement {}
 * }</pre>
 *
 * This annotation will have no effect when placed on something that is not an element.
 * @see ForbiddenProfile
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredProfile {

    /**
     * The profiles that are required for the element to be activated.
     */
    String[] value();

}
