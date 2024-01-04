/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.tframework.core.di.scanner.RootElementClassScanner.ROOT_SCANNING_ENABLED_PROPERTY;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.annotations.AnnotationScannersFactory;
import org.tframework.core.classes.ClassFiltersFactory;
import org.tframework.core.classes.PackageClassScanner;
import org.tframework.core.di.annotations.Element;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.SinglePropertyValue;

@ExtendWith(MockitoExtension.class)
class RootElementClassScannerTest {

    @Mock
    private PackageClassScanner packageClassScanner;

    private RootElementClassScanner scanner;

    @Test
    public void shouldScanElements_whenEnableProperty_isProvidedAsTrue() {
        setUpScannerWithScanEnabledProperty(true);
        when(packageClassScanner.scanClasses()).thenReturn(List.of(SomeElement.class, SomeNonElement.class));

        var elements = scanner.scanElements();

        assertEquals(1, elements.size());
        assertTrue(elements.contains(SomeElement.class));
    }

    @Test
    public void shouldScanElementsAsEmpty_whenEnableProperty_isProvidedAsFalse() {
        setUpScannerWithScanEnabledProperty(false);

        var elements = scanner.scanElements();

        assertTrue(elements.isEmpty());
    }

    @Test
    public void shouldScanElements_whenEnableProperty_isNotProvided_defaultToTrue() {
        setUpScannerWithScanEnabledProperty(null);
        when(packageClassScanner.scanClasses()).thenReturn(List.of(SomeElement.class, SomeNonElement.class));

        var elements = scanner.scanElements();

        assertEquals(1, elements.size());
        assertTrue(elements.contains(SomeElement.class));
    }

    private void setUpScannerWithScanEnabledProperty(Boolean enabled) {
        PropertiesContainer properties;
        if(enabled == null) {
            properties = PropertiesContainer.empty();
        } else if(enabled) {
            properties = PropertiesContainer.fromProperties(Map.of(
                        ROOT_SCANNING_ENABLED_PROPERTY, new SinglePropertyValue("true")
                ));
        } else {
            properties = PropertiesContainer.fromProperties(Map.of(
                        ROOT_SCANNING_ENABLED_PROPERTY, new SinglePropertyValue("false")
                ));
        }

        //To reduce mocking noise, some of the classes are not mocked.
        scanner = RootElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .classScanner(packageClassScanner)
                .propertiesContainer(properties)
                .build();
    }

    @Element
    static class SomeElement {}

    static class SomeNonElement {}
}
