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
package org.tframework.core.properties.group;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.postprocessing.PostProcessorBaseTest;
import org.tframework.core.reflection.annotations.PreScannedAnnotations;
import org.tframework.core.reflection.field.FieldSetter;

@MockitoSettings(strictness = Strictness.LENIENT)
public class PropertyGroupPostProcessorTest extends PostProcessorBaseTest {

    private static final String PROPERTY_GROUP_NAME = "my.props";
    private static final String PROPERTY_NAME = "custom-property-name";
    private static final String NAMED_PROPERTY_VALUE = "namedPropertyValue";
    private static final String DEFAULT_PROPERTY_VALUE = "defaultPropertyValue";

    @Mock
    private FieldSetter fieldSetter;

    @Mock
    private ElementSource elementSource;

    @InjectMocks
    private PropertyGroupPostProcessor postProcessor;

    private MyProps myProps;
    private Field namedPropertyField;
    private Field defaultPropertyField;

    @BeforeEach
    void setUp() throws Exception {
        myProps = new MyProps();
        namedPropertyField = MyProps.class.getDeclaredField("myNamedProp");
        defaultPropertyField = MyProps.class.getDeclaredField("myDefaultProp");

        when(elementContext.getAnnotationsOnFields()).thenReturn(Map.of(
                defaultPropertyField, toPreScanned(defaultPropertyField),
                namedPropertyField, toPreScanned(namedPropertyField)
        ));
        when(elementContext.getSource()).thenReturn(elementSource);
        when(elementContext.getName()).thenReturn("myPropsElement");
        when(elementSource.annotatedSource()).thenReturn(MyProps.class);
    }

    @Test
    public void shouldNotProcessInstance_whenPropertyGroupAnnotationIsNotPresent() {
        var elementSourceAnnotations = PreScannedAnnotations.empty(elementSource.annotatedSource());
        when(elementContext.getAnnotationsOnElementSource()).thenReturn(elementSourceAnnotations);

        postProcessor.postProcessInstance(application, elementContext, myProps);

        verifyNoInteractions(fieldSetter);
    }

    @Test
    public void shouldProcessInstance_whenPropertyGroupAnnotationIsPresent() {
        var elementSourceAnnotations = PreScannedAnnotations.empty(elementSource.annotatedSource());
        elementSourceAnnotations.add(MyProps.class.getAnnotation(PropertyGroup.class));
        when(elementContext.getAnnotationsOnElementSource()).thenReturn(elementSourceAnnotations);

        when(propertiesContainer.getPropertyValue(PROPERTY_GROUP_NAME + "." + PROPERTY_NAME, String.class))
                .thenReturn(NAMED_PROPERTY_VALUE);
        when(propertiesContainer.getPropertyValue(PROPERTY_GROUP_NAME + ".myDefaultProp", String.class))
                .thenReturn(DEFAULT_PROPERTY_VALUE);

        when(propertiesContainer.getPropertyValue(PROPERTY_GROUP_NAME + "." + PROPERTY_NAME, String.class))
                .thenReturn(NAMED_PROPERTY_VALUE);
        when(propertiesContainer.getPropertyValue(PROPERTY_GROUP_NAME + ".myDefaultProp", String.class))
                .thenReturn(DEFAULT_PROPERTY_VALUE);

        postProcessor.postProcessInstance(application, elementContext, myProps);

        verify(fieldSetter).setFieldValue(myProps, namedPropertyField, NAMED_PROPERTY_VALUE);
        verify(fieldSetter).setFieldValue(myProps, defaultPropertyField, DEFAULT_PROPERTY_VALUE);
    }

    private PreScannedAnnotations toPreScanned(Field field) {
        var annotations = PreScannedAnnotations.empty(field);
        if(field.isAnnotationPresent(Property.class)) {
            annotations.add(field.getAnnotation(Property.class));
        }
        return annotations;
    }

    @PropertyGroup(name = PROPERTY_GROUP_NAME)
    static class MyProps {

        @Property(PROPERTY_NAME)
        private String myNamedProp;

        private String myDefaultProp;
    }
}
