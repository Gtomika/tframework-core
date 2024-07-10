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
package org.tframework.core.elements.assembler;

import lombok.Builder;
import org.tframework.core.TFrameworkException;

/**
 * Thrown when an {@link ElementAssembler} encounters a problem while constructing
 * an instance of the element.
 */
@Builder
public class ElementAssemblingException extends TFrameworkException {

    public static final String TEMPLATE = """
            Failed to assemble element!
            - Element name: %s
            - Element type: %s
            - Assembler: %s
            - Assembling from: %s
            """;

    private final String elementName;
    private final Class<?> elementType;
    private final Class<? extends ElementAssembler> assemblerClass;
    private final String assembledFrom;
    private final Throwable cause;

    private ElementAssemblingException(
            String elementName,
            Class<?> elementType,
            Class<? extends ElementAssembler> assemblerClass,
            String assembledFrom,
            Throwable cause
    ) {
        super(TEMPLATE.formatted(elementName, elementType.getName(), assemblerClass.getName(), assembledFrom), cause);
        this.elementName = elementName;
        this.elementType = elementType;
        this.assemblerClass = assemblerClass;
        this.assembledFrom = assembledFrom;
        this.cause = cause;
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
