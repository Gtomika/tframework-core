/* Licensed under Apache-2.0 2024. */
package org.tframework.test.annotations;

import org.tframework.test.TFrameworkExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in conjunction with {@link TFrameworkExtension} to simulate command line arguments passed to
 * the application.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SetCommandLineArguments {

    /**
     * The "command line arguments" that will be passed to the application.
     */
    String[] value();

}
