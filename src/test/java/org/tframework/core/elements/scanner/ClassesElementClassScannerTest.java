/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.classes.ClassFiltersFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        PropertiesContainer propertiesContainer = PropertiesContainer.fromProperties(List.of(
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
        var propertiesContainer = PropertiesContainer.fromProperties(List.of(new Property(
                ClassesElementClassScanner.SCAN_CLASSES_PROPERTY,
                new ListPropertyValue(classesToScan)
        )));
        scanner = buildScanner(propertiesContainer);
    }

    private void setUpScannerWithOneClassProperty(String classToScan) {
        var propertiesContainer = PropertiesContainer.fromProperties(List.of(new Property(
                ClassesElementClassScanner.SCAN_CLASSES_PROPERTY,
                new SinglePropertyValue(classToScan)
        )));
        scanner = buildScanner(propertiesContainer);
    }

    private void setUpScannerWithScannerWithNoClassToScan() {
        var propertiesContainer = PropertiesContainer.empty();
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
