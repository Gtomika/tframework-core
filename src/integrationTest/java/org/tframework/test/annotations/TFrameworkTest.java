/* Licensed under Apache-2.0 2024. */
package org.tframework.test.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.extension.ExtendWith;
import org.tframework.test.TFrameworkExtension;

/**
 * A composed annotation of {@link TFrameworkExtension} and configurations, that can be used to start a full-fledged
 * TFramework application for the tests. The root class will be looked up from the classpath, it must be
 * annotated with {@link org.tframework.core.TFrameworkRootClass}. All element scanning is enabled.
 */
@ExtendWith(TFrameworkExtension.class)
@SetRootClass(
        useTestClassAsRoot = false,
        findRootClassOnClasspath = true
)
@SetElements(
        rootScanningEnabled = true,
        rootHierarchyScanningEnabled = true,
        internalScanningEnabled = true
)
@Retention(RetentionPolicy.RUNTIME)
public @interface TFrameworkTest {
}
