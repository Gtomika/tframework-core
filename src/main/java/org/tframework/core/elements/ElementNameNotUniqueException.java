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
package org.tframework.core.elements;

import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;

/**
 * Thrown when the framework detects that an element name is not unique.
 */
public class ElementNameNotUniqueException extends TFrameworkException {

    private static final String TEMPLATE = """
            Element name '%s' is not unique. Element names must be unique across the application.
            Please select a different name for one of the duplicate elements using '@Element(name = "yourName")'.
            - Existing element context: %s
            - Duplicate element context: %s""";

    public ElementNameNotUniqueException(ElementContext existingContext, ElementContext duplicateContext) {
        super(TEMPLATE.formatted(existingContext.getName(), existingContext, duplicateContext));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
