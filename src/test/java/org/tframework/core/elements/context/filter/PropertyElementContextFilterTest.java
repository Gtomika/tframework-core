package org.tframework.core.elements.context.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.Application;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.filter.annotation.RequiredProperty;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertyNotFoundException;
import org.tframework.core.reflection.annotations.AnnotationScanner;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PropertyElementContextFilterTest {

    private static final String PROPERTY_NAME = "my.property";
    private static final String OTHER_PROPERTY_NAME = "my.other.property";
    private static final String PROPERTY_VALUE = "test";

    @Mock
    private AnnotationScanner annotationScanner;

    @Mock
    private ElementContext elementContext;

    @Mock
    private ElementSource elementSource;

    @Mock
    private PropertiesContainer propertiesContainer;

    @InjectMocks
    private PropertyElementContextFilter filter;

    private AnnotatedElement annotatedElementSource;
    private Application application;

    @BeforeEach
    public void setUp() {
        annotatedElementSource = this.getClass();
        when(elementContext.getSource()).thenReturn(elementSource);
        when(elementSource.annotatedSource()).thenReturn(annotatedElementSource);

        application = Application.builder()
                .propertiesContainer(propertiesContainer)
                .build();
    }

    @Test
    public void shouldKeepElementContext_whenRequiredPropertyAnnotationIsNotPresent() {
        givenRequiredPropertyAnnotations(List.of());
        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertFalse(discardElementContext);
    }

    @Test
    void shouldKeepElementContext_whenPropertyExists() throws Exception {
        var annotation = givenAnnotation("elementWithRequiredProperty");
        givenRequiredPropertyAnnotations(List.of(annotation));
        givenPropertyExists();

        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertFalse(discardElementContext);
    }

    @Test
    public void shouldDiscardElementContext_whenPropertyIsMissing() throws Exception {
        var annotation = givenAnnotation("elementWithRequiredProperty");
        givenRequiredPropertyAnnotations(List.of(annotation));
        givenPropertyIsMissing();

        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertTrue(discardElementContext);
    }

    @Test
    public void shouldKeepElementContext_whenPropertyExists_andHasValue() throws Exception {
        var annotation = givenAnnotation("elementWithRequiredPropertyAndValue");
        givenRequiredPropertyAnnotations(List.of(annotation));
        givenPropertyExists();

        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertFalse(discardElementContext);
    }

    @Test
    public void shouldDiscardElementContext_whenPropertyExists_andValueMismatch() throws Exception {
        var annotation = givenAnnotation("elementWithRequiredPropertyAndValue");
        givenRequiredPropertyAnnotations(List.of(annotation));
        givenPropertyExists(PROPERTY_NAME,"different value");

        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertTrue(discardElementContext);
    }

    @Test
    public void shouldKeepElementContext_whenPropertyExists_andHasNoValue() throws Exception {
        var annotation = givenAnnotation("elementWithRequiredPropertyAndNoValue");
        givenRequiredPropertyAnnotations(List.of(annotation));
        givenPropertyExists(PROPERTY_NAME, "different value");

        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertFalse(discardElementContext);
    }

    @Test
    public void shouldDiscardElementContext_whenPropertyExists_andHasNotAllowedValue() throws Exception {
        var annotation = givenAnnotation("elementWithRequiredPropertyAndNoValue");
        givenRequiredPropertyAnnotations(List.of(annotation));
        givenPropertyExists();

        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertTrue(discardElementContext);
    }

    @Test
    public void shouldKeepElementContext_whenMultiplePropertyConditionsSet_andAllFulfilled() throws Exception {
        var annotation1 = givenAnnotation("elementWithRequiredProperty");
        var annotation2 = givenAnnotation("elementWithOtherRequiredProperty");
        givenRequiredPropertyAnnotations(List.of(annotation1, annotation2));

        givenPropertyExists(PROPERTY_NAME, PROPERTY_VALUE);
        givenPropertyExists(OTHER_PROPERTY_NAME, PROPERTY_VALUE);

        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertFalse(discardElementContext);
    }

    @Test
    public void shouldDiscardElementContext_whenMultiplePropertyConditionsSet_andAnyNotFulfilled() throws Exception {
        var annotation1 = givenAnnotation("elementWithRequiredProperty");
        var annotation2 = givenAnnotation("elementWithOtherRequiredProperty");
        givenRequiredPropertyAnnotations(List.of(annotation1, annotation2));

        givenPropertyIsMissing(PROPERTY_NAME);

        boolean discardElementContext = filter.discardElementContext(elementContext, application);
        assertTrue(discardElementContext);
    }

    private RequiredProperty givenAnnotation(String fieldName) throws Exception {
        return getClass().getDeclaredField(fieldName).getAnnotation(RequiredProperty.class);
    }

    private void givenRequiredPropertyAnnotations(List<RequiredProperty> annotations) {
        when(annotationScanner.scan(annotatedElementSource, RequiredProperty.class))
                .thenReturn(annotations);
    }

    private void givenPropertyExists() {
        givenPropertyExists(PROPERTY_NAME, PROPERTY_VALUE);
    }

    private void givenPropertyExists(String name, String value) {
        when(propertiesContainer.getPropertyValue(name, String.class)).thenReturn(value);
    }

    private void givenPropertyIsMissing() {
        givenPropertyIsMissing(PROPERTY_NAME);
    }

    private void givenPropertyIsMissing(String name) {
        when(propertiesContainer.getPropertyValue(name, String.class))
                .thenThrow(new PropertyNotFoundException(name));
    }

    @RequiredProperty(name = PROPERTY_NAME)
    private String elementWithRequiredProperty;

    @RequiredProperty(name = PROPERTY_NAME, hasValue = PROPERTY_VALUE)
    private String elementWithRequiredPropertyAndValue;

    @RequiredProperty(name = PROPERTY_NAME, hasNoValue = PROPERTY_VALUE)
    private String elementWithRequiredPropertyAndNoValue;

    @RequiredProperty(name = OTHER_PROPERTY_NAME)
    private String elementWithOtherRequiredProperty;
}