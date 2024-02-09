/* Licensed under Apache-2.0 2024. */
package org.tframework.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.tframework.core.TFrameworkRootClass;
import org.tframework.test.TFrameworkExtension;

/**
 * A composed annotation of {@link TFrameworkExtension} and other configurations: this allows to
 * launch a small and contained TFramework application withing a given test class.
 * <p><br>
 * The root class of this application will be the test class. Only elements inside the test class
 * will be scanned.
 */
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
@TFrameworkRootClass
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsolatedTFrameworkTest {
}
