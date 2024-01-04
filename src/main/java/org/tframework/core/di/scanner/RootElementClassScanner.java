/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.classes.ClassFilter;
import org.tframework.core.classes.PackageClassScanner;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.properties.converters.PropertyConverter;
import org.tframework.core.properties.converters.PropertyConvertersFactory;

/**
 * A {@link ElementClassScanner} which scans the root class' package and all its sub-packages.
 * This scanner is enabled by default, but can be disabled by setting the {@value #ROOT_SCANNING_ENABLED_PROPERTY}
 * to {@code false}.
 */
@Slf4j
public class RootElementClassScanner extends ElementClassScanner {

    public static final String ROOT_SCANNING_ENABLED_PROPERTY = "tframework.dependency-injection.scan-root";
    private static final SinglePropertyValue ROOT_SCANNING_ENABLED_DEFAULT_VALUE = new SinglePropertyValue("true");

    private final PropertiesContainer propertiesContainer;
    private final PackageClassScanner classScanner;
    private final PropertyConverter<Boolean> propertyConverter;

    RootElementClassScanner(
            AnnotationScanner annotationScanner,
            ClassFilter classFilter,
            PackageClassScanner classScanner,
            PropertiesContainer propertiesContainer
    ) {
        super(classFilter, annotationScanner);
        this.propertiesContainer = propertiesContainer;
        this.classScanner = classScanner;
        this.propertyConverter = PropertyConvertersFactory.getConverter(Boolean.class);
    }

    @Override
    public List<Class<?>> scanPotentialElements() {
        var scanRootProperty = propertiesContainer.getPropertyValueObject(
                ROOT_SCANNING_ENABLED_PROPERTY,
                ROOT_SCANNING_ENABLED_DEFAULT_VALUE
        );
        log.debug("The EFFECTIVE value of property '{}' is '{}'", ROOT_SCANNING_ENABLED_PROPERTY, scanRootProperty);
        if(propertyConverter.convert(scanRootProperty)) {
            return classScanner.scanClasses();
        } else {
            return List.of();
        }
    }

    @Builder
    private static RootElementClassScanner create(
            @NonNull AnnotationScanner annotationScanner,
            @NonNull ClassFilter classFilter,
            @NonNull PackageClassScanner classScanner,
            @NonNull PropertiesContainer propertiesContainer
    ) {
        return new RootElementClassScanner(annotationScanner, classFilter, classScanner, propertiesContainer);
    }
}
