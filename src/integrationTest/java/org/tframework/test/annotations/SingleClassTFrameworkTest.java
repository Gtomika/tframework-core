/* Licensed under Apache-2.0 2024. */
package org.tframework.test.annotations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.tframework.test.TFrameworkExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(TFrameworkExtension.class)
@SetRootClass(
        useTestClassAsRoot = true,
        findRootClassOnClasspath = false
)
@SetElements(
        rootScanningEnabled = true, //only the root class (which is the test class) will be scanned
        rootHierarchyScanningEnabled = false,
        internalScanningEnabled = false
)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleClassTFrameworkTest {
}
