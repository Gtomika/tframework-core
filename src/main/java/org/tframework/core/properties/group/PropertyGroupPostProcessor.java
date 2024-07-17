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

import java.lang.reflect.Field;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.postprocessing.ElementInstancePostProcessor;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.field.FieldSetter;

/**
 * This {@link ElementInstancePostProcessor} is responsible for processing {@link PropertyGroup}
 * annotated element instances by injecting the groups properties into its fields.
 */
@Slf4j
@Element
@RequiredArgsConstructor
public class PropertyGroupPostProcessor implements ElementInstancePostProcessor {

    private final AnnotationScanner annotationScanner;
    private final FieldSetter fieldSetter;

    @Override
    public void postProcessInstance(Application application, ElementContext elementContext, Object instance) {
        annotationScanner.scanOneStrict(elementContext.getSource().annotatedSource(), PropertyGroup.class)
                .ifPresent(propertyGroup -> processPropertyGroup(
                        elementContext,
                        instance,
                        application.getPropertiesContainer(),
                        propertyGroup
                ));
    }

    private void processPropertyGroup(
            ElementContext elementContext,
            Object instance,
            PropertiesContainer propertiesContainer,
            PropertyGroup propertyGroupAnnotation
    ) {
        log.debug("Property group element '{}': Processing property group '{}'...",
                elementContext.getName(), propertyGroupAnnotation.name());

        elementContext.getFields().forEach(field -> {
            String propertyName = propertyGroupAnnotation.name() + "." + findPropertyNameInGroup(field);
            log.debug("Property group element '{}': Attempting to inject property '{}' into field '{}'",
                    elementContext.getName(), propertyName, field.getName());

            var propertyValue = propertiesContainer.getPropertyValue(propertyName, field.getType());
            fieldSetter.setFieldValue(instance, field, propertyValue);
            log.debug("Property group element '{}': Injected property '{}' with value '{}' into field '{}'",
                    elementContext.getName(), propertyName, propertyValue, field.getName());
        });
    }

    /*
    Possible improvement: we can apply some logic here instead of just getting field name.
    Like removing underscore, hyphen, checking for camel case etc.
     */
    private String findPropertyNameInGroup(Field field) {
        return annotationScanner.scanOneStrict(field, Property.class)
                .map(Property::value)
                .orElse(field.getName());
    }
}
