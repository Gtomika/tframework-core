/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.classes.ClassFilter;
import org.tframework.core.reflection.classes.PackageClassScanner;

/**
 * This {@link ElementClassScanner} scans all elements in the given packages. A list of packages
 * can be specified by the {@value #SCAN_PACKAGES_PROPERTY} property. If this property is not set,
 * there will be no additional packages scanned by this scanner. It is the responsibility of the
 * user to set the {@value #SCAN_PACKAGES_PROPERTY} property with actual valid packages (if the
 * package names are not valid, this scanner will not scan any elements).
 * <p>
 * Package names can contain wildcards, as specified by the {@link PackageClassScanner}. There is no
 * need to specify subpackages, because the underlying {@link PackageClassScanner} will also scan all subpackages.
 * <p>
 * Normally, this scanner is not required, because the {@link RootElementClassScanner} can pick up
 * all application elements. However, if for some reason the package structure is non-standard,
 * this scanner can be used to add additional packages to the element scanning.
 * @see ClassesElementClassScanner
 */
@Slf4j
public class PackagesElementClassScanner extends ElementClassScanner {

    public static final String SCAN_PACKAGES_PROPERTY = "org.tframework.dependency-injection.scan-packages";
    private static final List<String> SCAN_PACKAGES_DEFAULT_VALUE = List.of();

    private final PackageClassScanner classScanner;

    private PackagesElementClassScanner(
            ClassFilter classFilter,
            AnnotationScanner annotationScanner,
            PropertiesContainer propertiesContainer,
            PackageClassScanner classScanner
    ) {
        super(classFilter, annotationScanner, propertiesContainer);
        this.classScanner = classScanner;
    }

    @Override
    protected Set<Class<?>> scanPotentialElements() {
        var packageNames = propertiesContainer.getPropertyValueList(
                SCAN_PACKAGES_PROPERTY,
                SCAN_PACKAGES_DEFAULT_VALUE
        )
                .stream()
                .peek(packageName -> log.debug("The package '{}' will be scanned for elements", packageName))
                .collect(Collectors.toSet());

        if(packageNames.isEmpty()) {
            log.debug("No additional packages will be scanned for elements");
            return Set.of();
        } else {
            try {
                classScanner.setPackageNames(packageNames);
                return classScanner.scanClasses();
            } catch (Exception e) {
                //if class scanner decides that one package is not valid, it will throw an exception
                log.error("Failed to scan additional packages for elements.", e);
                return Set.of();
            }
        }
    }

    @Builder
    static PackagesElementClassScanner create(
            ClassFilter classFilter,
            AnnotationScanner annotationScanner,
            PropertiesContainer propertiesContainer,
            PackageClassScanner classScanner
    ) {
        return new PackagesElementClassScanner(classFilter, annotationScanner, propertiesContainer, classScanner);
    }
}
