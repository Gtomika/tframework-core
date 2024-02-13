/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.classes.ClassFilter;
import org.tframework.core.reflection.classes.ClassScannersFactory;
import org.tframework.core.utils.ClassLoaderUtils;
import org.tframework.core.utils.Constants;
import org.tframework.core.utils.LogUtils;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link ElementClassScanner} that scans elements from a set of classes. The classes can be provided
 * with the {@value #SCAN_CLASSES_PROPERTY} property. Additionally, this property name can also be
 * suffixed such as {@value #SCAN_CLASSES_PROPERTY}{@code -important} and these kind of properties will
 * also be picked up and checked for classes. It is the responsibility of the user to set the {@value #SCAN_CLASSES_PROPERTY} property
 * with actual valid classes (if a class cannot be loaded, it will be ignored, with an error message logged).
 * <p>
 * Normally, this scanner is not required, because the {@link RootElementClassScanner} can pick up
 * all application elements. However, it may be useful in some cases to pick up individual classes. For example,
 * during testing, it may be useful to pick up only a few classes instead of entire packages.
 * @see PackagesElementClassScanner
 */
//well this is an obnoxious name...
@Slf4j
public class ClassesElementClassScanner extends ElementClassScanner {

    public static final String SCAN_CLASSES_PROPERTY = Constants.TFRAMEWORK_PROPERTIES_PREFIX + ".elements.scan-classes";
    private static final List<String> SCAN_CLASSES_DEFAULT_VALUE = List.of();

    static final Pattern ADDITIONAL_SCAN_CLASSES_PROPERTY_REGEX = Pattern.compile(
            "org\\.tframework\\.elements\\.scan-classes-.[a-z-]+"
    );

    @Builder
    private ClassesElementClassScanner(
            ClassFilter classFilter,
            AnnotationScanner annotationScanner,
            PropertiesContainer propertiesContainer
    ) {
        super(classFilter, annotationScanner, propertiesContainer);
    }

    @Override
    protected Set<Class<?>> scanPotentialElements() {
        var classesToScan = propertiesContainer.getPropertyValueList(
                SCAN_CLASSES_PROPERTY, SCAN_CLASSES_DEFAULT_VALUE
        );
        log.debug("Picked up {} classes from property '{}'", classesToScan.size(), SCAN_CLASSES_PROPERTY);

        var additionalClassesToScan = findAdditionalClassesToScan(propertiesContainer);
        log.debug("Picked up {} classes to scan from additional 'scan-classes-*' properties", additionalClassesToScan.size());

        return Stream.concat(classesToScan.stream(), additionalClassesToScan.stream())
                .map(this::scanClass)
                .flatMap(Set::stream) //flatten so that outer classes and inner classes are in the same set
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> scanClass(String className) {
        try {
            var clazz = ClassLoaderUtils.loadClass(className, ClassesElementClassScanner.class);
            var scannedClasses = ClassScannersFactory.createNestedClassScanner(clazz).scanClasses();
            log.debug("The class '{}' was scanned for elements. The following POTENTIAL elements were found (including nested): {}",
                    clazz.getName(), LogUtils.classNames(scannedClasses, true));
            return scannedClasses;
        } catch (Exception e) {
            log.error("Could not load class '{}'. This class will not be scanned for elements.", className, e);
            return Set.of();
        }
    }

    private List<String> findAdditionalClassesToScan(PropertiesContainer properties) {
        return properties.propertyNames().stream()
                .filter(propertyName -> ADDITIONAL_SCAN_CLASSES_PROPERTY_REGEX.matcher(propertyName).matches())
                .peek(propertyName -> log.debug("Found property '{}' that will be used to find classes to scan", propertyName))
                .map(propertiesContainer::getPropertyValueList)
                .flatMap(List::stream)
                .toList();
    }
}
