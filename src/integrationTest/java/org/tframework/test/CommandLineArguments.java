/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in conjunction with {@link TFrameworkExtension} to simulate command line arguments passed to
 * the application.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLineArguments {

    /**
     * The "command line arguments" that will be passed to the application.
     */
    String[] value();

}
