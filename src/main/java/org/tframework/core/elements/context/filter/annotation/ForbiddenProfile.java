/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Can be placed on element classes or methods, to declare that the element
 * requires a profile (or profiles) to <b>not be set</b>. For example, if we want
 * {@code SomeElement} to be only active when profiles {@code a} and {@code b} are <b>not</b> set:
 *
 * <pre>{@code
 * @Element
 * @ForbiddenProfile({"a", "b"})
 * public class SomeElement {}
 * }</pre>
 *
 * This annotation will have no effect when placed on something that is not an element.
 * @see RequiredProfile
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ForbiddenProfile {

    /**
     * The profiles that <b>must not be set</b> for the element to be activated.
     */
    String[] value();

}
