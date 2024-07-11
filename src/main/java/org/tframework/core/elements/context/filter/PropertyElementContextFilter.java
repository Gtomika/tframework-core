package org.tframework.core.elements.context.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.filter.annotation.RequiredProperty;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertyNotFoundException;
import org.tframework.core.reflection.annotations.AnnotationScanner;

/**
 * An {@link ElementContextFilter} that can discard element contexts based on the properties set.
 * The rules are documented in the {@link RequiredProperty} annotation.
 */
@Slf4j
@Element
@RequiredArgsConstructor
public class PropertyElementContextFilter implements ElementContextFilter {

    private final AnnotationScanner annotationScanner;

    @Override
    public boolean discardElementContext(ElementContext elementContext, Application application) {
        var properties = application.getPropertiesContainer();
        return annotationScanner.scan(elementContext.getSource().annotatedSource(), RequiredProperty.class)
                .stream()
                .anyMatch(requiredProperty -> !requiredPropertyFulfilled(requiredProperty, properties, elementContext));
    }

    private boolean requiredPropertyFulfilled(
            RequiredProperty requiredProperty,
            PropertiesContainer properties,
            ElementContext elementContext
    ) {
        try {
            String propertyValue = properties.getPropertyValue(requiredProperty.name(), String.class);
            log.debug("Element context '{}' requires property '{}', which is present",
                    elementContext.getName(), requiredProperty.name());

            boolean fulfillsHasValue = fulfillsHasValue(requiredProperty, elementContext, propertyValue);
            boolean fulfillsHasNoValue = fulfillsHasNoValue(requiredProperty, elementContext, propertyValue);
            return fulfillsHasValue && fulfillsHasNoValue;
        } catch (PropertyNotFoundException e) {
            log.debug("Element context '{}' requires property '{}' to be present, but it is not",
                    elementContext.getName(), requiredProperty.name());
            return false;
        }
    }

    private boolean fulfillsHasValue(
            RequiredProperty requiredProperty,
            ElementContext elementContext,
            String propertyValue
    ) {
        boolean attributeSet = attributeSet(requiredProperty.hasValue());
        boolean fulfillsHasValue = !attributeSet || requiredProperty.hasValue().equals(propertyValue);
        if(attributeSet) {
            log.debug("Element context '{}' requires property '{}' to have value '{}', and it has value '{}'",
                    elementContext.getName(), requiredProperty.name(), requiredProperty.hasValue(), propertyValue);
        }
        return fulfillsHasValue;
    }

    private boolean fulfillsHasNoValue(
            RequiredProperty requiredProperty,
            ElementContext elementContext,
            String propertyValue
    ) {
        boolean attributeSet = attributeSet(requiredProperty.hasNoValue());
        boolean fulfillsHasNoValue = !attributeSet || !requiredProperty.hasNoValue().equals(propertyValue);
        if(attributeSet) {
            log.debug("Element context '{}' requires property '{}' to not have value '{}', and it has value '{}'",
                    elementContext.getName(), requiredProperty.name(), requiredProperty.hasNoValue(), propertyValue);
        }
        return fulfillsHasNoValue;
    }

    private boolean attributeSet(String attribute) {
        return !attribute.equals(RequiredProperty.VALUE_NOT_SET);
    }
}
