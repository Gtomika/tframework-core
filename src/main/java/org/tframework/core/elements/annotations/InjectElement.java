/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.tframework.core.elements.ElementUtils;

/**
 * This annotation is used to mark a field or parameter as an element dependency. The framework will
 * look up the element by name and inject it into the field or parameter. In case of fields, all
 * conditions must be met for it to be considered valid for injection:
 * <ul>
 *     <li>The field is not static.</li>
 *     <li>The field is not final.</li>
 *     <li>Visibility can be any.</li>
 * </ul>
 * In all cases, when using {@link #value()} to specify an element by name, the element must be assignable
 * to the type of the field or parameter you are trying to inject it into. For example:
 * <pre>{@code
 * //assuming there is an element which is assignable to MyElement type
 * @InjectElement
 * private MyElement myElement;
 *
 * //assuming there is an element named 'my.element' which is assignable to MyElement type
 * @InjectElement("my.element")
 * private MyElement myElement;
 * }</pre>
 * These examples were for field injection, but the same applies to constructor injection.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectElement {

    /**
     * The name of the dependency to inject. If not specified, the name will be from the type of the construct
     * that has this annotation ({@link ElementUtils#getElementNameByType(Class)}).
     * Usually this does not need to be specified, but it can be useful if there are multiple elements of the same type.
     */
    String value() default Element.NAME_NOT_SPECIFIED;

}
