/* Licensed under Apache-2.0 2024. */
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
