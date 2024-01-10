/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.util.Set;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.properties.converters.PropertyConverter;
import org.tframework.core.properties.converters.PropertyConvertersFactory;
import org.tframework.core.reflection.classes.ClassFilter;
import org.tframework.core.reflection.classes.PackageClassScanner;

/**
 * An {@link ElementClassScanner} implementation which scans the internal TFramework packages
 * ({@value TFRAMEWORK_INTERNAL_PACKAGE}). By default, this scanner is enabled, but can be enabled by setting the
 * {@value TFRAMEWORK_INTERNAL_PACKAGE_PROPERTY} to {@code false}.
 */
@Slf4j
public class InternalElementClassScanner extends ElementClassScanner {

    /**
     * The internal TFramework package (and subpackages) that will be scanned for elements,
     * if this scanner is enabled.
     */
    public static final String TFRAMEWORK_INTERNAL_PACKAGE = "org.tframework";

    /**
     * The property that enables/disables this scanner.
     */
    public static final String TFRAMEWORK_INTERNAL_PACKAGE_PROPERTY = "tframework.dependency-injection.scan-internal";

    private static final SinglePropertyValue TFRAMEWORK_INTERNAL_PACKAGE_DEFAULT_VALUE = new SinglePropertyValue("true");

    private final PackageClassScanner classScanner;
    private final PropertyConverter<Boolean> propertyConverter;

     InternalElementClassScanner(
            AnnotationScanner annotationScanner,
            ClassFilter classFilter,
            PropertiesContainer propertiesContainer,
            PackageClassScanner classScanner
    ) {
        super(classFilter, annotationScanner, propertiesContainer);

        this.classScanner = classScanner;
        this.classScanner.setPackageNames(Set.of(TFRAMEWORK_INTERNAL_PACKAGE));
        log.debug("The internal element scanner will scan the package '{}' and all its sub-packages", TFRAMEWORK_INTERNAL_PACKAGE);

        this.propertyConverter = PropertyConvertersFactory.getConverter(Boolean.class);
    }

    @Override
    public Set<Class<?>> scanPotentialElements() {
        var scanInternalProperty = propertiesContainer.getPropertyValueObject(
                TFRAMEWORK_INTERNAL_PACKAGE_PROPERTY,
                TFRAMEWORK_INTERNAL_PACKAGE_DEFAULT_VALUE
        );
        if(propertyConverter.convert(scanInternalProperty)) {
            return classScanner.scanClasses();
        } else {
            log.debug("The internal element scanner is disabled, no classes will be scanned");
            return Set.of();
        }
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
