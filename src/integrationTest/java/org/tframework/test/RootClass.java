/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in conjunction with {@link TFrameworkExtension} to specify the application's root class.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RootClass {

    /**
     * The root class to use.
     */
    Class<?> value();

}
