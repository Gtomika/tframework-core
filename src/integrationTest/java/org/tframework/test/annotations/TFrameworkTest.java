/* Licensed under Apache-2.0 2024. */
package org.tframework.test.annotations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.annotations.Element;
import org.tframework.test.TFrameworkExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A composed annotation of {@link TFrameworkExtension} and configurations, that can be used to start a full-fledged
 * TFramework application for the tests. The root class will be looked up from the classpath, it must be
 * annotated with {@link org.tframework.core.TFrameworkRootClass}. All element scanning is enabled.
 * <br><br>
 * <b>When using this annotation, the test class must not also be the root class of the application.</b>
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
@Element(scope = ElementScope.SINGLETON)
@Retention(RetentionPolicy.RUNTIME)
public @interface TFrameworkTest {
}
