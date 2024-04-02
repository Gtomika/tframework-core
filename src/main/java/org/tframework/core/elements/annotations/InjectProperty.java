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
 * for the type of field or parameter.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectProperty {

    /**
     * The name of the property to inject.
     */
    String value();

}
