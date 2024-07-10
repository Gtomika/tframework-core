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
package org.tframework.core.elements.context.assembler;

import org.tframework.core.TFrameworkException;

/**
 * Exception thrown when an error occurs while assembling an {@link org.tframework.core.elements.context.ElementContext}.
 */
public class ElementContextAssemblingException extends TFrameworkException {

    private static final String TEMPLATE = """
            An error occurred while assembling the element context.
            - Element type: %s
            - Declared as '%s' in '%s'
            - Message: %s""";

    public ElementContextAssemblingException(Class<?> elementType, String declaredAs, String declaredIn, String message) {
        super(String.format(TEMPLATE, elementType.getName(), declaredAs, declaredIn, message));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
