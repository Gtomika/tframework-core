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

import java.util.List;
import java.util.stream.Collectors;
import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;

/**
 * Thrown when an element is requested by type, however there
 * are multiple choices of elements that can be selected.
 */
public class AmbiguousElementTypeException extends TFrameworkException {

    private static final String TEMPLATE = """
            There are multiple elements that are assignable to the type: '%s'
            %s""";

    public AmbiguousElementTypeException(Class<?> ambiguousType, List<ElementContext> assignableElements) {
        super(TEMPLATE.formatted(
                ambiguousType.getName(),
                assignableElements.stream()
                        .map(element -> "- " + element.toString())
                        .collect(Collectors.joining("\n"))
        ));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
