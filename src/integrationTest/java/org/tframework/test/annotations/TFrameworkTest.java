package org.tframework.test.annotations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.tframework.test.TFrameworkExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A composed annotation of {@link TFrameworkExtension} and configurations, that can be used to start a full-fledged
 * TFramework application for the tests. The root class will be looked up from the classpath, it must be
 * annotated with {@link org.tframework.core.TFrameworkRootClass}. All element scanning is enabled.
 * <p><br>
 * You must also provide the {@link BasePackage} annotation together with this one, to specify where to
 * look for your root class. You may create a composed annotation of this one and your {@link BasePackage}
 * annotation.
 */
@ExtendWith(TFrameworkExtension.class)
//user will specify @BasePackage
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
