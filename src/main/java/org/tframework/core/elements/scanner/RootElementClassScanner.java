/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.properties.converters.PropertyConverter;
import org.tframework.core.properties.converters.PropertyConvertersFactory;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.classes.ClassFilter;
import org.tframework.core.reflection.classes.NestedClassScanner;
import org.tframework.core.reflection.classes.PackageClassScanner;
import org.tframework.core.utils.Constants;

import java.util.Set;

/**
 * A {@link ElementClassScanner} which scans the root class (including nested classes) and its hierarchy of packages:
 * root classes package and all its sub-packages. This scanner is enabled by default.
 * <ul>
 *     <li>
 *         To disable root class hierarchy scanning, set {@value #ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY}to {@code false}.
 *         In this case, only the root class and its nested classes will be scanned, but not the package and the sub-packages.
 *     </li>
 *     <li>
 *         To disable this scanner altogether (not even the root class should be scanned), use {@value ROOT_SCANNING_ENABLED_PROPERTY}
 *         with {@code false} value.
 *     </li>
 * </ul>
 */
@Slf4j
public class RootElementClassScanner extends ElementClassScanner {

    public static final String ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY = Constants.TFRAMEWORK_PROPERTIES_PREFIX + ".elements.scan-root-hierarchy";
    private static final SinglePropertyValue ROOT_HIERARCHY_SCANNING_ENABLED_DEFAULT_VALUE = new SinglePropertyValue("true");

    public static final String ROOT_SCANNING_ENABLED_PROPERTY = Constants.TFRAMEWORK_PROPERTIES_PREFIX + ".elements.scan-root";
    private static final SinglePropertyValue ROOT_SCANNING_ENABLED_DEFAULT_VALUE = new SinglePropertyValue("true");

    private final PackageClassScanner packageClassScanner;
    private final NestedClassScanner rootClassScanner;
    private final PropertyConverter<Boolean> propertyConverter;
    private final Class<?> rootClass;

    RootElementClassScanner(
            AnnotationScanner annotationScanner,
            ClassFilter classFilter,
            PropertiesContainer propertiesContainer,
            PackageClassScanner packageClassScanner,
            NestedClassScanner rootClassScanner,
            Class<?> rootClass
    ) {
        super(classFilter, annotationScanner, propertiesContainer);
        this.packageClassScanner = packageClassScanner;
        this.rootClassScanner = rootClassScanner;
        this.propertyConverter = PropertyConvertersFactory.getConverter(Boolean.class);
        this.rootClass = rootClass;
    }

    @Override
    public Set<Class<?>> scanPotentialElements() {
        var scanRootProperty = propertiesContainer.getPropertyValueObject(
                ROOT_SCANNING_ENABLED_PROPERTY,
                ROOT_SCANNING_ENABLED_DEFAULT_VALUE
        );
        log.debug("The EFFECTIVE value of property '{}' is '{}'", ROOT_SCANNING_ENABLED_PROPERTY, scanRootProperty);
        if(propertyConverter.convert(scanRootProperty)) {
            //scanner is enabled, but is it enabled for only the root class, or the entire hierarchy?
            var scanRootHierarchyProperty = propertiesContainer.getPropertyValueObject(
                    ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY,
                    ROOT_HIERARCHY_SCANNING_ENABLED_DEFAULT_VALUE
            );
            log.debug("The EFFECTIVE value of property '{}' is '{}'", ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY, scanRootHierarchyProperty);
            if(propertyConverter.convert(scanRootHierarchyProperty)) {
                String packageName = rootClass.getPackageName();
                this.packageClassScanner.setPackageNames(Set.of(packageName));
                log.debug("The root element scanner will scan the package '{}' and all its sub-packages", packageName);
                return packageClassScanner.scanClasses();
            } else {
                log.debug("The root element scanner will scan only the root class '{}'", rootClass.getName());
                return rootClassScanner.scanClasses();
            }
        } else {
            log.debug("The root element scanner is disabled, no classes will be scanned");
            return Set.of();
        }
    }

    @Builder
    private static RootElementClassScanner create(
            @NonNull AnnotationScanner annotationScanner,
            @NonNull ClassFilter classFilter,
            @NonNull PropertiesContainer propertiesContainer,
            @NonNull PackageClassScanner packageClassScanner,
            @NonNull NestedClassScanner rootClassScanner,
            @NonNull Class<?> rootClass
    ) {
        return new RootElementClassScanner(
                annotationScanner, classFilter, propertiesContainer, packageClassScanner, rootClassScanner, rootClass
        );
    }
}
