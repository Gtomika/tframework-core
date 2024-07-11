package org.tframework.core.elements.context.filter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Repeatable container of {@link RequiredProperty}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatedRequiredProperty {
        RequiredProperty[] value();
}
