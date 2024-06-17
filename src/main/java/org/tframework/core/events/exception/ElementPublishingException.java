/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events.exception;

import java.lang.reflect.Method;
import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;

public class ElementPublishingException extends TFrameworkException {

    public static final String TEMPLATE = "Failed to publish to element context '%s', method '%s': %s";

    private final ElementContext elementContext;
    private final Method method;
    private final Exception cause;

    public ElementPublishingException(ElementContext elementContext, Method method, Exception cause) {
        super(TEMPLATE.formatted(elementContext.getName(), method.getName(), cause.getMessage()), cause);
        this.elementContext = elementContext;
        this.method = method;
        this.cause = cause;
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
