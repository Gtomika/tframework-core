/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.tframework.core.di.scanner.InternalElementClassScanner.TFRAMEWORK_INTERNAL_PACKAGE_PROPERTY;

import java.util.Map;
import java.util.Set;
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
class InternalElementClassScannerTest {

    @Mock
    private PackageClassScanner packageClassScanner;

    private InternalElementClassScanner internalElementClassScanner;

    @Test
    public void shouldScanInternalElements_whenEnableProperty_isProvidedAsTrue() {
        setUpScannerWithScanEnabledProperty(true);
        when(packageClassScanner.scanClasses()).thenReturn(Set.of(SomeElement.class, SomeNonElement.class));

        var elements = internalElementClassScanner.scanElements();

        assertEquals(1, elements.size());
        assertTrue(elements.contains(SomeElement.class));
    }

    @Test
    public void shouldScanInternalElementsAsEmpty_whenEnableProperty_isProvidedAsFalse() {
        setUpScannerWithScanEnabledProperty(false);

        var elements = internalElementClassScanner.scanElements();

        assertTrue(elements.isEmpty());
    }

    @Test
    public void shouldScanInternalElements_whenEnableProperty_isNotProvided_defaultValue() {
        setUpScannerWithScanEnabledProperty(null);

        var elements = internalElementClassScanner.scanElements();

        //defaults to true
        assertEquals(1, elements.size());
        assertTrue(elements.contains(SomeElement.class));
    }

    private void setUpScannerWithScanEnabledProperty(Boolean enabled) {
        PropertiesContainer properties;
        if(enabled == null) {
            properties = PropertiesContainer.empty();
        } else if(enabled) {
            properties = PropertiesContainer.fromProperties(Map.of(
                    TFRAMEWORK_INTERNAL_PACKAGE_PROPERTY, new SinglePropertyValue("true")
            ));
        } else {
            properties = PropertiesContainer.fromProperties(Map.of(
                    TFRAMEWORK_INTERNAL_PACKAGE_PROPERTY, new SinglePropertyValue("false")
            ));
        }

        //To reduce mocking noise, some of the classes are not mocked.
        internalElementClassScanner = InternalElementClassScanner.builder()
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
