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

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.classes.ClassFiltersFactory;

class ClassesElementClassScannerTest {

    private ClassesElementClassScanner scanner;

    @Test
    public void shouldScanElements_ifScanClassesProperty_isProvidedAsList() {
        setUpScannerWithMultipleClassesProperty(List.of(
                OuterElementClass.class.getName(), OuterNonElementClass.class.getName()
        ));

        var results = scanner.scanElements();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(OuterElementClass.class)));
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(OuterElementClass.InnerElementClass.class)));
    }

    @Test
    public void shouldScanElements_ifScanClassesProperty_isProvidedAsSingleValue() {
        setUpScannerWithOneClassProperty(OuterElementClass.class.getName());

        var results = scanner.scanElements();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(OuterElementClass.class)));
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(OuterElementClass.InnerElementClass.class)));
    }

    @Test
    public void shouldScanElementsEmpty_ifScanClassesProperty_isNotProvided() {
        setUpScannerWithScannerWithNoClassToScan();

        var results = scanner.scanElements();

        assertTrue(results.isEmpty());
    }

    @Test
    public void shouldScanElementsEmpty_ifScanClassesProperty_isInvalid() {
        setUpScannerWithOneClassProperty(null);

        var results = scanner.scanElements();

        assertTrue(results.isEmpty());
    }

    @Test
    public void shouldScanElements_ifAdditionalScanClassesProperties_areAlsoProvided() {
        PropertiesContainer propertiesContainer = PropertiesContainerFactory.fromProperties(List.of(
                new Property(
                        ClassesElementClassScanner.SCAN_CLASSES_PROPERTY + "-test",
                        new ListPropertyValue(List.of(OuterElementClass.class.getName()))
                ),
                new Property(
                        ClassesElementClassScanner.SCAN_CLASSES_PROPERTY + "-additional",
                        new ListPropertyValue(List.of(OuterElementClass.InnerElementClass.class.getName()))
                )
        ));
        scanner = buildScanner(propertiesContainer);

        var results = scanner.scanElements();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(OuterElementClass.class)));
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(OuterElementClass.InnerElementClass.class)));
    }

    private void setUpScannerWithMultipleClassesProperty(List<String> classesToScan) {
        var propertiesContainer = PropertiesContainerFactory.fromProperties(List.of(new Property(
                ClassesElementClassScanner.SCAN_CLASSES_PROPERTY,
                new ListPropertyValue(classesToScan)
        )));
        scanner = buildScanner(propertiesContainer);
    }

    private void setUpScannerWithOneClassProperty(String classToScan) {
        var propertiesContainer = PropertiesContainerFactory.fromProperties(List.of(new Property(
                ClassesElementClassScanner.SCAN_CLASSES_PROPERTY,
                new SinglePropertyValue(classToScan)
        )));
        scanner = buildScanner(propertiesContainer);
    }

    private void setUpScannerWithScannerWithNoClassToScan() {
        var propertiesContainer = PropertiesContainerFactory.empty();
        scanner = buildScanner(propertiesContainer);
    }

    private ClassesElementClassScanner buildScanner(PropertiesContainer propertiesContainer) {
        return ClassesElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .propertiesContainer(propertiesContainer)
                .build();
    }

    @Element
    static class OuterElementClass {
        @Element
        static class InnerElementClass {}

        static class InnerNonElementClass {}
    }

    static class OuterNonElementClass {}

}
