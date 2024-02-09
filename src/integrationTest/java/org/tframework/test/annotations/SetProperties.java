/* Licensed under Apache-2.0 2024. */
package org.tframework.test.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.tframework.test.TFrameworkExtension;

/**
 * Used in conjunction with {@link TFrameworkExtension} to specify properties to be set into the
 * applications {@link org.tframework.core.properties.PropertiesContainer}. Specify properties as strings,
 * where the property name and values are separated by {@value org.tframework.core.properties.parsers.PropertyParsingUtils#PROPERTY_NAME_VALUE_SEPARATOR}.
 * For example, to set the {@code cool.prop} and {@code test.prop} properties to {@code cool} and {@code test},
 * respectively, use:
 * <pre>
 * {@literal @Properties({"cool.prop=cool", "test.prop=test"})}
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SetProperties {

    /**
     * The raw property string that should be set.
     */
    String[] value();

}
