/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.classes.ClassFilter;
import org.tframework.core.reflection.classes.NestedClassScanner;
import org.tframework.core.reflection.classes.PackageClassScanner;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.tframework.core.elements.scanner.RootElementClassScanner.ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY;
import static org.tframework.core.elements.scanner.RootElementClassScanner.ROOT_SCANNING_ENABLED_PROPERTY;

@ExtendWith(MockitoExtension.class)
class RootElementClassScannerTest {

    @Mock
    private AnnotationScanner annotationScanner;

    @Mock
    private ClassFilter classFilter;

    @Mock
    private PackageClassScanner packageClassScanner;

    @Mock
    private NestedClassScanner rootClassScanner;

    private RootElementClassScanner scanner;

    private final Set<Class<?>> elements = Set.of(SomeElement.class, OtherElement.class);
    private final Set<AnnotationFilteringResult<Element, Class<?>>> elementAnnotationFilterResults = Set.of(
        new AnnotationFilteringResult<>(SomeElement.class.getAnnotation(Element.class), SomeElement.class) ,
        new AnnotationFilteringResult<>(OtherElement.class.getAnnotation(Element.class), OtherElement.class)
    );

    private final Set<Class<?>> rootClassSet = Set.of(RootClass.class);
    private final Set<AnnotationFilteringResult<Element, Class<?>>> rootClassAnnotationFilteringResults = Set.of(
            new AnnotationFilteringResult<>(RootClass.class.getAnnotation(Element.class), RootClass.class)
    );

    @Test
    public void shouldScanRootHierarchy_whenEnableProperty_isProvidedAsTrue_andHierarchyProperty_isProvidedAsTrue() {
        var properties = setUpBooleanPropertyProperty(ROOT_SCANNING_ENABLED_PROPERTY, BoolPropertyState.ENABLED)
                .merge(setUpBooleanPropertyProperty(ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY, BoolPropertyState.ENABLED));
        setUpScanner(properties);

        when(packageClassScanner.scanClasses()).thenReturn(elements);
        when(classFilter.filterByAnnotation(elements, Element.class, annotationScanner, true))
                .thenReturn(elementAnnotationFilterResults);

        var results = scanner.scanElements();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(SomeElement.class)));
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(OtherElement.class)));
    }

    @Test
    public void shouldScanRootClassOnly_whenEnableProperty_isProvidedAsTrue_andHierarchyProperty_isProvidedAsFalse() {
        var properties = setUpBooleanPropertyProperty(ROOT_SCANNING_ENABLED_PROPERTY, BoolPropertyState.ENABLED)
                .merge(setUpBooleanPropertyProperty(ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY, BoolPropertyState.DISABLED));
        setUpScanner(properties);

        doReturn(rootClassSet).when(rootClassScanner).scanClasses();
        when(classFilter.filterByAnnotation(rootClassSet, Element.class, annotationScanner, true))
                .thenReturn(rootClassAnnotationFilteringResults);

        var results = scanner.scanElements();

        assertEquals(1, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(RootClass.class)));
    }

    @ParameterizedTest
    @EnumSource(BoolPropertyState.class)
    public void shouldScanElementsAsEmpty_whenEnableProperty_isProvidedAsFalse(BoolPropertyState hierarchyState) {
        var properties = setUpBooleanPropertyProperty(ROOT_SCANNING_ENABLED_PROPERTY, BoolPropertyState.DISABLED)
                //in this case hierarchy state will be ignored, because root scanning is disabled altogether
                .merge(setUpBooleanPropertyProperty(ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY, hierarchyState));
        setUpScanner(properties);

        when(classFilter.filterByAnnotation(Set.of(), Element.class, annotationScanner, true)).thenReturn(Set.of());

        var results = scanner.scanElements();

        assertTrue(results.isEmpty());
    }

    @Test
    public void shouldScanElements_whenEnableProperty_isNotProvided_defaultValue() {
        var properties = setUpBooleanPropertyProperty(ROOT_SCANNING_ENABLED_PROPERTY, BoolPropertyState.NOT_SET)
                .merge(setUpBooleanPropertyProperty(ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY, BoolPropertyState.ENABLED));
        setUpScanner(properties);

        when(packageClassScanner.scanClasses()).thenReturn(elements);
        when(classFilter.filterByAnnotation(elements, Element.class, annotationScanner, true))
                .thenReturn(elementAnnotationFilterResults);

        var results = scanner.scanElements();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(SomeElement.class)));
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(OtherElement.class)));
    }

    private PropertiesContainer setUpBooleanPropertyProperty(String propertyName, BoolPropertyState boolPropertyState) {
        return switch (boolPropertyState) {
            case BoolPropertyState.ENABLED -> PropertiesContainer.fromProperties(List.of(new Property(
                    propertyName, new SinglePropertyValue("true")
            )));
            case BoolPropertyState.DISABLED -> PropertiesContainer.fromProperties(List.of(new Property(
                    propertyName, new SinglePropertyValue("false")
            )));
            case BoolPropertyState.NOT_SET ->  PropertiesContainer.empty();
        };
    }

    private void setUpScanner(PropertiesContainer propertiesContainer) {
        scanner = RootElementClassScanner.builder()
                .annotationScanner(annotationScanner)
                .classFilter(classFilter)
                .packageClassScanner(packageClassScanner)
                .rootClassScanner(rootClassScanner)
                .propertiesContainer(propertiesContainer)
                .rootClass(RootClass.class)
                .build();
    }

    enum BoolPropertyState {
        ENABLED, DISABLED, NOT_SET
    }

    @Element
    static class RootClass {}

    @Element
    static class SomeElement {}

    @Element
    static class OtherElement {}
}
