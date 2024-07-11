package org.tframework.core.elements.context.filter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be placed on elements so that they can be filtered out based on
 * properties. For example, to activate an element only when {@code my.property} is present:
 *
 * <pre>{@code
 * @Element
 * @RequiredProperty(name = "my.property")
 * public class SomeElement {}
 * }</pre>
 *
 * To activate an element only when {@code my.property} is set to {@code value}:
 *
 * <pre>{@code
 * @Element
 * @RequiredProperty(name = "my.property", hasValue = "value")
 * public class SomeElement {}
 * }</pre>
 *
 * You can freely combine this annotation with other filters, or use it multiple times on the same element.
 * These will be connected with a logical AND.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatedRequiredProperty.class)
public @interface RequiredProperty {

    String VALUE_NOT_SET = "";

    /**
     * Required property name to check for.
     */
    String name();

    /**
     * An optional property value to check for. If not set, the property must be present,
     * but its value is not checked.
     */
    String hasValue() default VALUE_NOT_SET;

    /**
     * An optional negated property value. If not set, the property must be present,
     * but its value is not checked.
     */
    String hasNoValue() default VALUE_NOT_SET;
}
