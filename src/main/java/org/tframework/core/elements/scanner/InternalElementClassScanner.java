/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import java.util.Set;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.classes.ClassFilter;
import org.tframework.core.reflection.classes.PackageClassScanner;

/**
 * An {@link ElementClassScanner} implementation which scans the internal TFramework packages
 * ({@value TFRAMEWORK_INTERNAL_PACKAGE}). This scanner cannot be disabled, as the internal elements
 * are crucial for the framework.
 */
@Slf4j
public class InternalElementClassScanner extends ElementClassScanner {

    /**
     * The internal TFramework package (and subpackages) that will be scanned for elements,
     * if this scanner is enabled.
     */
    public static final String TFRAMEWORK_INTERNAL_PACKAGE = "org.tframework";
    public static final String TFRAMEWORK_REJECTED_INTERNAL_PACKAGE = "org.tframework.test";

    private final PackageClassScanner classScanner;

    InternalElementClassScanner(
            AnnotationScanner annotationScanner,
            ClassFilter classFilter,
            PropertiesContainer propertiesContainer,
            PackageClassScanner classScanner
    ) {
        super(classFilter, annotationScanner, propertiesContainer);
        this.classScanner = classScanner;
        this.classScanner.setPackageNames(Set.of(TFRAMEWORK_INTERNAL_PACKAGE));
        this.classScanner.setRejectedPackages(Set.of(TFRAMEWORK_REJECTED_INTERNAL_PACKAGE));
    }

    @Override
    public Set<Class<?>> scanPotentialElements() {
        log.debug("The internal element scanner will scan the package '{}' and all its sub-packages",
                TFRAMEWORK_INTERNAL_PACKAGE);
        log.debug("The internal element scanner will ignore the package '{}' and all its sub-packages",
                TFRAMEWORK_REJECTED_INTERNAL_PACKAGE);
        return classScanner.scanClasses();
    }

    @Builder
    private static InternalElementClassScanner create(
            AnnotationScanner annotationScanner,
            ClassFilter classFilter,
            PropertiesContainer propertiesContainer,
            PackageClassScanner classScanner
    ) {
        return new InternalElementClassScanner(annotationScanner, classFilter, propertiesContainer, classScanner);
    }
}
