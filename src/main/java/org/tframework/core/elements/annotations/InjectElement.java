/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to mark a field or parameter as an element dependency. The framework will
 * look up the element by name and inject it into the field or parameter.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectElement {

    /**
     * The name of the dependency to inject. If not specified, the name will be from the type of the construct
     * that has this annotation ({@link org.tframework.core.elements.ElementUtils#getElementNameByType(Class)}).
     * Usually this does not need to be specified, but it can be useful if there are multiple elements of the same type.
     */
    String value() default Element.NAME_NOT_SPECIFIED;

}
