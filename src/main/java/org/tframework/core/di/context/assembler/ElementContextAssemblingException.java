/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context.assembler;

import org.tframework.core.TFrameworkException;

/**
 * Exception thrown when an error occurs while assembling an {@link org.tframework.core.di.context.ElementContext}.
 */
public class ElementContextAssemblingException extends TFrameworkException {

    private static final String TEMPLATE = """
            An error occurred while assembling the element context.
            - Element class: %s
            - Declared as '%s' in '%s'
            - Message: %s""";

    public ElementContextAssemblingException(Class<?> elementClass, String declaredAs, String declaredIn, String message) {
        super(String.format(TEMPLATE, elementClass.getName(), declaredAs, declaredIn, message));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
