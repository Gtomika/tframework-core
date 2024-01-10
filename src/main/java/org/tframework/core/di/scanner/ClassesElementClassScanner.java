/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.classes.ClassFilter;
import org.tframework.core.reflection.classes.ClassScannersFactory;
import org.tframework.core.utils.ClassLoaderUtils;
import org.tframework.core.utils.LogUtils;

/**
 * A {@link ElementClassScanner} that scans elements from a set of classes. The classes can be provided
 * with the {@value #SCAN_CLASSES_PROPERTY} property. If this property is not set, there will be no additional
 * classes scanned. It is the responsibility of the caller to set the {@value #SCAN_CLASSES_PROPERTY} property
 * with actual valid classes (if a class cannot be loaded, it will be ignored).
 * <p>
 * Normally, this scanner is not required, because the {@link RootElementClassScanner} can pick up
 * all application elements. However, it may be useful in some cases to pick up individual classes. For example,
 * during testing, it may be useful to pick up only a few classes instead of the entire application.
 * @see PackagesElementClassScanner
 */
//well this is an obnoxious class name...
@Slf4j
public class ClassesElementClassScanner extends ElementClassScanner {

    public static final String SCAN_CLASSES_PROPERTY = "org.tframework.dependency-injection.scan-classes";
    private static final List<String> SCAN_CLASSES_DEFAULT_VALUE = List.of();

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
        return propertiesContainer.getPropertyValueList(
                SCAN_CLASSES_PROPERTY, SCAN_CLASSES_DEFAULT_VALUE
        )
                .stream()
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
}
