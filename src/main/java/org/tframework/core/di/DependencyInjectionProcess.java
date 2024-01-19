/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.di.context.ElementContext;
import org.tframework.core.di.context.assembler.ClassElementContextAssembler;
import org.tframework.core.di.context.assembler.ElementContextAssembler;
import org.tframework.core.di.context.assembler.MethodElementContextAssembler;
import org.tframework.core.di.scanner.ElementClassScanner;
import org.tframework.core.di.scanner.ElementMethodScanner;
import org.tframework.core.di.scanner.ElementScanner;
import org.tframework.core.di.scanner.ElementScannersBundle;

/**
 * This class is responsible for the dependency injection process. This process consists of the following steps:
 * <ul>
 *     <li>Scanning for elements (see {@link ElementScanner}s).</li>
 *     <li>Assembling {@link ElementContext}s (see {@link ElementContextAssembler}s).</li>
 *     <li>TODO</li>
 * </ul>
 * The result of the process will be an {@link ElementsContainer} with unique {@link ElementContext}s.
 */
@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DependencyInjectionProcess {

    private final ClassElementContextAssembler classElementContextAssembler;
    private final MethodElementContextAssembler methodElementContextAssembler;

    /**
     * Initializes the dependency injection process.
     * @param scannersBundle An {@link ElementScannersBundle} containing the {@link ElementScanner}s to be used.
     * @return the {@link ElementsContainer} containing the assembled {@link ElementContext}s
     */
    public ElementsContainer initialize(ElementScannersBundle scannersBundle) {
        ElementsContainer elementsContainer = ElementsContainer.empty();

        assembleElementContexts(elementsContainer, scannersBundle);

        return elementsContainer;
    }

    /**
     * Uses the scanners in the {@link ElementScannersBundle} to find elements,
     * then assembles {@link ElementContext}s from them. These will be added to the {@link ElementsContainer}.
     * <ul>
     *     <li>
     *         Each {@link ElementClassScanner} in the scanner bundle will find element classes.
     *         These will be assembled into {@link ElementContext}s using the {@link ClassElementContextAssembler}.
     *     </li>
     *     <li>
     *         Then, each {@link ElementMethodScanner} in the scanner bundle will find element methods.
     *         These will be assembled into {@link ElementContext}s using the {@link MethodElementContextAssembler}.
     *         See {@link #assembleMethodElementContexts(ElementsContainer, List, List)}.
     *     </li>
     * </ul>
     */
    private void assembleElementContexts(ElementsContainer elementsContainer, ElementScannersBundle scannersBundle) {
        for(ElementClassScanner elementClassScanner : scannersBundle.elementClassScanners()) {
            List<ElementContext> elementContexts = elementClassScanner.scanElements()
                    .stream()
                    .map(classElementContextAssembler::assemble)
                    .peek(elementsContainer::addElementContext)
                    .toList();

            log.debug("Assembled {} element contexts from class scanner '{}'", elementContexts.size(), elementClassScanner.getClass().getName());
            assembleMethodElementContexts(elementsContainer, elementContexts, scannersBundle.elementMethodScanners());
        }

        log.debug("Assembled a total of {} element contexts", elementsContainer.elementCount());
    }

    /**
     * Uses the {@link ElementMethodScanner}s from the {@link ElementScannersBundle} to find element methods.
     * These will be assembled into {@link ElementContext}s using the {@link MethodElementContextAssembler}s.
     */
    private void assembleMethodElementContexts(
            ElementsContainer elementsContainer,
            List<ElementContext> parentElementContexts,
            List<ElementMethodScanner> elementMethodScanners
    ) {
        for(ElementContext parentElementContext : parentElementContexts) {
            methodElementContextAssembler.setParentElementContext(parentElementContext);

            for(var elementMethodScanner : elementMethodScanners) {
                elementMethodScanner.setClassToScan(parentElementContext.getType());

                List<ElementContext> elementContexts = elementMethodScanner.scanElements().stream()
                        .map(methodElementContextAssembler::assemble)
                        .peek(elementsContainer::addElementContext)
                        .toList();

                if(!elementContexts.isEmpty()) {
                    log.debug("Assembled {} element contexts from methods of parent element context '{}' ({})",
                            elementContexts.size(), parentElementContext.getName(), parentElementContext.getType().getName());
                }
            }
        }
    }


}
