/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.di.context.ElementContext;
import org.tframework.core.di.context.assembler.ElementContextAssembler;
import org.tframework.core.di.scanner.ElementScanner;

@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DependencyInjectionProcess {

    private final ElementContextAssembler<Class<?>> classElementContextAssembler;

    public ElementsContainer initialize(List<ElementScanner<Class<?>>> elementClassScanners) {
        List<ElementContext> elementContexts = new ArrayList<>();

        elementClassScanners.forEach(elementClassScanner -> {
            var assembledElementContexts = elementClassScanner.scanElements()
                    .stream()
                    .map(classElementContextAssembler::assemble)
                    .toList();

            //TODO: check for duplicate element names: this must be implemented inside ElementContainer
            //TODO: create ElementContext-s from methods and add them to the container

            elementContexts.addAll(assembledElementContexts);
        });

        return ElementsContainer.fromElementContexts(elementContexts);
    }

}
