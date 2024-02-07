package org.tframework.test.annotations;

import org.tframework.test.TFrameworkExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in conjunction with {@link TFrameworkExtension} to specify element related settings
 * or additional classes/packages that should be scanned during tests.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SetElements {

    /**
     * Control if the root scanning should be enabled.
     * @see org.tframework.core.elements.scanner.RootElementClassScanner
     */
    boolean rootScanningEnabled() default true;

    /**
     * Control if the root hierarchy scanning should be enabled.
     * @see org.tframework.core.elements.scanner.RootElementClassScanner
     */
    boolean rootHierarchyScanningEnabled() default true;

    /**
     * Control if the internal scanning should be enabled.
     * @see org.tframework.core.elements.scanner.InternalElementClassScanner
     */
    boolean internalScanningEnabled() default true;

    /**
     * Set additional packages to scanned for elements.
     * @see org.tframework.core.elements.scanner.PackagesElementClassScanner
     */
    String[] scanAdditionalPackages() default {};

    /**
     * Set additional classes to be scanned for elements.
     * @see org.tframework.core.elements.scanner.ClassesElementClassScanner
     */
    String[] scanAdditionalClasses() default {};

}
