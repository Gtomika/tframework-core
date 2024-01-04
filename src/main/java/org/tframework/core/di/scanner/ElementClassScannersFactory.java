/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.annotations.AnnotationScannersFactory;
import org.tframework.core.classes.ClassFiltersFactory;
import org.tframework.core.classes.ClassScannersFactory;
import org.tframework.core.properties.PropertiesContainer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementClassScannersFactory {

    public static RootElementClassScanner createRootElementClassScanner(Class<?> rootClass, PropertiesContainer properties) {
        String packageName = rootClass.getPackageName();
        log.debug("The root element scanner will scan the package '{}' and all its sub-packages", packageName);

        var packageClassScanner = ClassScannersFactory.createPackageClassScanner(Set.of(packageName));
        return RootElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .classScanner(packageClassScanner)
                .propertiesContainer(properties)
                .build();
    }

}
