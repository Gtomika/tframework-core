/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in conjunction with {@link TFrameworkExtension} to specify the application name.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationName {

    /**
     * The application name to use.
     */
    String value();

}
