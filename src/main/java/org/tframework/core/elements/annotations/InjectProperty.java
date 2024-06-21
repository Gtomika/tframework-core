/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.tframework.core.properties.converters.PropertyConverter;

/**
 * This annotation is used to mark a field or parameter as a property dependency. The framework will
 * look up the property by name and inject it into the field or parameter. In case of fields, all
 * conditions must be met for it to be considered valid for injection:
 * <ul>
 *     <li>The field is not static.</li>
 *     <li>The field is not final.</li>
 *     <li>Visibility can be any.</li>
 * </ul>
 * It is also required that an appropriate {@link PropertyConverter} exists that can convert the property
 * for the type of field or parameter. For example:
 * <pre>{@code
 * @InjectProperty("my.string.property")
 * private String myStringProperty;
 *
 * //assuming 'my.int.property' can be converted to an int
 * @InjectProperty("my.int.property")
 * private int myIntProperty;
 *
 * //this will work even if the property is not found
 * @InjectProperty(value = "my.boolean.property", defaultValue = "true")
 * private boolean myBooleanProperty;
 * }</pre>
 * These examples were for field injection, but the same applies to constructor injection.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectProperty {

    String DEFAULT_VALUE_NOT_PROVIDED = "";

    /**
     * The <b>name</b> of the property to inject. Please note that despite this attribute being named
     * {@code value}, it is actually the name of the property. It is named {@code value} because
     * that is the default name for the attribute in annotations.
     */
    String value();

    /**
     * The default value to use if the property is not found.
     * This is a raw string and will be converted to the appropriate type by the framework.
     */
    String defaultValue() default DEFAULT_VALUE_NOT_PROVIDED;

}
