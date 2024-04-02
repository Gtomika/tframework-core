/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import java.lang.reflect.Field;
import java.util.List;
import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;

/**
 * Thrown when {@link FieldInjectionPostProcessor} encounters issues or invalid data during field injection.
 */
public class FieldInjectionException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to inject into the field '%s' of element '%s': %s";

    public FieldInjectionException(Field field, ElementContext elementContext, List<String > problems) {
        super(TEMPLATE.formatted(field.getName(), elementContext.getName(), String.join(", ", problems)));
    }

    public FieldInjectionException(Field field, ElementContext elementContext, Exception cause) {
        super(TEMPLATE.formatted(field.getName(), elementContext.getName(), cause.getMessage()), cause);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
