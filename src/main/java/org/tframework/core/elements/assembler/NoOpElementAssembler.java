/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.assembler;

import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * A special {@link ElementAssembler} which just returns null. This is used
 * for elements which do not require assembling, such as pre-constructed ones.
 */
public class NoOpElementAssembler extends ElementAssembler {

    NoOpElementAssembler(String elementName, Class<?> elementType) {
        super(elementName, elementType);
    }

    @Override
    public Object assemble(ElementDependencyGraph dependencyGraph) throws ElementAssemblingException {
        return null;
    }
}
