/* Licensed under Apache-2.0 2024. */
package org.tframework.test.annotations;

import org.tframework.test.TFrameworkExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in conjunction with {@link TFrameworkExtension} to specify the application name.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SetApplicationName {

    /**
     * The application name to use.
     */
    String value();

}
