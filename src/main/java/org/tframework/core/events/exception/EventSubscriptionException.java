/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events.exception;

import java.lang.reflect.Method;
import java.util.List;
import lombok.Getter;
import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.events.annotations.Subscribe;

/**
 * Thrown when an error occurs while trying to subscribe a {@link Subscribe}
 * method of an element.
 */
@Getter
public class EventSubscriptionException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to subscribe element context '%s', method '%s': %s";

    private final ElementContext elementContext;
    private final Method method;
    private final List<String> errors;

    public EventSubscriptionException(ElementContext elementContext, Method method, List<String> errors) {
        super(TEMPLATE.formatted(elementContext.getName(), method.getName(), String.join(", ", errors)));
        this.elementContext = elementContext;
        this.method = method;
        this.errors = errors;
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
