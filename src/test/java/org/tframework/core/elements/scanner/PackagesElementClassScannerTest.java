/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.elements.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.classes.ClassFiltersFactory;
import org.tframework.core.reflection.classes.PackageClassScanner;

@ExtendWith(MockitoExtension.class)
class PackagesElementClassScannerTest {

    @Mock
    private PackageClassScanner packageClassScanner;

    private PackagesElementClassScanner packagesElementClassScanner;

    @Test
    public void shouldScanPackages_whenPackagesProperty_isProvidedAsList() {
        setUpScannerWithMultiplePackagesProperty(List.of("some.custom.package1", "some.custom.package2"));
        when(packageClassScanner.scanClasses())
                .thenReturn(Set.of(SomeElementFromPackage1.class, SomeElementFromPackage2.class, SomeNonElement.class));

        var results = packagesElementClassScanner.scanElements();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(SomeElementFromPackage1.class)));
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(SomeElementFromPackage2.class)));
    }

    @Test
    public void shouldScanPackages_whenPackagesProperty_isProvidedAsSingleValue() {
        setUpScannerWithOnePackageProperty("some.custom.package1");
        when(packageClassScanner.scanClasses())
                .thenReturn(Set.of(SomeElementFromPackage1.class, SomeNonElement.class));

        var results = packagesElementClassScanner.scanElements();

        assertEquals(1, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(SomeElementFromPackage1.class)));
    }

    @Test
    public void shouldScanPackages_whenPackagesProperty_isNotProvided() {
        setUpScannerWithScannerWithNoPackagesToScan();

        var results = packagesElementClassScanner.scanElements();

        assertTrue(results.isEmpty());
    }

    @Test
    public void shouldScanPackages_whenPackagesProperty_isInvalid() {
        setUpScannerWithOnePackageProperty(null);
        when(packageClassScanner.scanClasses())
                .thenThrow(new NullPointerException("The package name cannot be null"));

        var elements = packagesElementClassScanner.scanElements();

        assertTrue(elements.isEmpty());
    }

    private void setUpScannerWithMultiplePackagesProperty(List<String> packagesToScan) {
        var propertiesContainer = PropertiesContainerFactory.fromProperties(List.of(new Property(
                PackagesElementClassScanner.SCAN_PACKAGES_PROPERTY,
                new ListPropertyValue(packagesToScan)
        )));
        packagesElementClassScanner = buildScanner(propertiesContainer);
    }

    private void setUpScannerWithOnePackageProperty(String packageToScan) {
        var propertiesContainer = PropertiesContainerFactory.fromProperties(List.of(new Property(
                PackagesElementClassScanner.SCAN_PACKAGES_PROPERTY,
                new SinglePropertyValue(packageToScan)
        )));
        packagesElementClassScanner = buildScanner(propertiesContainer);
    }

    private void setUpScannerWithScannerWithNoPackagesToScan() {
        var propertiesContainer = PropertiesContainerFactory.empty();
        packagesElementClassScanner = buildScanner(propertiesContainer);
    }

    private PackagesElementClassScanner buildScanner(PropertiesContainer propertiesContainer) {
        return PackagesElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .classScanner(packageClassScanner)
                .propertiesContainer(propertiesContainer)
                .build();
    }

    @Element
    static class SomeElementFromPackage1 {}

    @Element
    static class SomeElementFromPackage2 {}

    static class SomeNonElement {}

}
