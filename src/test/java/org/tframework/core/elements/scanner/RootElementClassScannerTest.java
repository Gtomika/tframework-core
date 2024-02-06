/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.classes.ClassFiltersFactory;
import org.tframework.core.reflection.classes.PackageClassScanner;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.tframework.core.elements.scanner.RootElementClassScanner.ROOT_SCANNING_ENABLED_PROPERTY;

@ExtendWith(MockitoExtension.class)
class RootElementClassScannerTest {

    @Mock
    private PackageClassScanner packageClassScanner;

    private RootElementClassScanner scanner;

    @Test
    public void shouldScanElements_whenEnableProperty_isProvidedAsTrue() {
        setUpScannerWithScanEnabledProperty(true);
        when(packageClassScanner.scanClasses()).thenReturn(Set.of(SomeElement.class, SomeNonElement.class));

        var results = scanner.scanElements();

        assertEquals(1, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(SomeElement.class)));
    }

    @Test
    public void shouldScanElementsAsEmpty_whenEnableProperty_isProvidedAsFalse() {
        setUpScannerWithScanEnabledProperty(false);

        var results = scanner.scanElements();

        assertTrue(results.isEmpty());
    }

    @Test
    public void shouldScanElements_whenEnableProperty_isNotProvided_defaultValue() {
        setUpScannerWithScanEnabledProperty(null);
        when(packageClassScanner.scanClasses()).thenReturn(Set.of(SomeElement.class, SomeNonElement.class));

        var results = scanner.scanElements();

        //defaults to true, so scanned elements should be returned
        assertEquals(1, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(SomeElement.class)));
    }

    private void setUpScannerWithScanEnabledProperty(Boolean enabled) {
        PropertiesContainer properties;
        if(enabled == null) {
            properties = PropertiesContainer.empty();
        } else if(enabled) {
            properties = PropertiesContainer.fromProperties(List.of(new Property(
                        ROOT_SCANNING_ENABLED_PROPERTY, new SinglePropertyValue("true")
                )));
        } else {
            properties = PropertiesContainer.fromProperties(List.of(new Property(
                        ROOT_SCANNING_ENABLED_PROPERTY, new SinglePropertyValue("false")
                )));
        }

        //To reduce mocking noise, some of the classes are not mocked.
        scanner = RootElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .classScanner(packageClassScanner)
                .propertiesContainer(properties)
                .rootClass(RootClass.class)
                .build();
    }

    static class RootClass {}

    @Element
    static class SomeElement {}

    static class SomeNonElement {}
}
