package org.tframework.test.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Allows to specify the base package in which your root class should be scanned.
 * This is used in conjunction with {@link SetRootClass} if you enabled {@link SetRootClass#findRootClassOnClasspath()}.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BasePackage {

    /**
     * The package in which to look for the root class. Sub-packages will also be scanned. This
     * is typically the 'group ID' of your application, a package in which all your classes are located.
     */
    String value();

}
