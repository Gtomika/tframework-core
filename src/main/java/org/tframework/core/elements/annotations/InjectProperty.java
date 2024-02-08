/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to mark a field or parameter as a property dependency. The framework will
 * look up the property by name and inject it into the field or parameter.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectProperty {

    /**
     * The name of the property to inject.
     */
    String value();

}
