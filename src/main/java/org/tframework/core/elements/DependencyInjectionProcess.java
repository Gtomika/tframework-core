/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.PreConstructedElementContext;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.ElementContextAssembler;
import org.tframework.core.elements.context.assembler.MethodElementContextAssembler;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.scanner.ElementClassScanner;
import org.tframework.core.elements.scanner.ElementMethodScanner;
import org.tframework.core.elements.scanner.ElementScanner;
import org.tframework.core.elements.scanner.ElementScannersBundle;
import org.tframework.core.elements.scanner.ElementScannersFactory;

import java.util.List;

/**
 * This class is responsible for the dependency injection process. This process consists of the following steps:
 * <ul>
 *     <li>Scanning for elements (see {@link ElementScanner}s).</li>
 *     <li>Assembling {@link ElementContext}s (see {@link ElementContextAssembler}s).</li>
 *     <li>Initializes each element context (see {@link ElementContext#initialize()}).</li>
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
     * @param input The {@link DependencyInjectionInput} containing the input data for the process.
     * @return the {@link ElementsContainer} containing the assembled {@link ElementContext}s
     */
    public ElementsContainer initialize(DependencyInjectionInput input) {
        var classElementScanners = ElementScannersFactory.createDefaultElementClassScanners(input);
        var methodElementScanners = ElementScannersFactory.createDefaultElementMethodScanners(input);
        var bundle = ElementScannersBundle.builder()
                .elementClassScanners(classElementScanners)
                .elementMethodScanners(methodElementScanners)
                .build();
        return initialize(input, bundle);
    }

    //this should be used by the other 'initialize' method and during tests
    //here we can provide the components instead of creating a default one
    ElementsContainer initialize(DependencyInjectionInput input, ElementScannersBundle scannersBundle) {
        var elementsContainer = ElementsContainer.empty();
        DependencyResolutionInput dependencyResolutionInput = DependencyResolutionInput.builder()
                .elementsContainer(elementsContainer)
                .propertiesContainer(input.application().getPropertiesContainer())
                .build();

        assembleElementContexts(elementsContainer, scannersBundle, dependencyResolutionInput);
        addPreConstructedElementContexts(elementsContainer, input.application());
        log.debug("Successfully assembled a total of {} element contexts", elementsContainer.elementCount());

        elementsContainer.initializeElementContexts();
        log.info("Successfully initialized {} element contexts", elementsContainer.elementCount());

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
     *         See {@link #assembleMethodElementContexts(ElementsContainer, List, List, DependencyResolutionInput)}.
     *     </li>
     * </ul>
     */
    private void assembleElementContexts(
            ElementsContainer elementsContainer,
            ElementScannersBundle scannersBundle,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        for(ElementClassScanner elementClassScanner : scannersBundle.elementClassScanners()) {
            List<ElementContext> elementContexts = elementClassScanner.scanElements()
                    .stream()
                    .map(scanResult -> classElementContextAssembler.assemble(scanResult, dependencyResolutionInput))
                    .peek(elementsContainer::addElementContext)
                    .toList();

            log.debug("Assembled {} element contexts from class scanner '{}'", elementContexts.size(), elementClassScanner.getClass().getName());
            assembleMethodElementContexts(
                    elementsContainer,
                    elementContexts,
                    scannersBundle.elementMethodScanners(),
                    dependencyResolutionInput
            );
        }
    }

    /**
     * Uses the {@link ElementMethodScanner}s from the {@link ElementScannersBundle} to find element methods.
     * These will be assembled into {@link ElementContext}s using the {@link MethodElementContextAssembler}s.
     */
    private void assembleMethodElementContexts(
            ElementsContainer elementsContainer,
            List<ElementContext> parentElementContexts,
            List<ElementMethodScanner> elementMethodScanners,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        for(ElementContext parentElementContext : parentElementContexts) {
            methodElementContextAssembler.setParentElementContext(parentElementContext);

            for(var elementMethodScanner : elementMethodScanners) {
                elementMethodScanner.setClassToScan(parentElementContext.getType());

                List<ElementContext> elementContexts = elementMethodScanner.scanElements().stream()
                        .map(scanResult -> methodElementContextAssembler.assemble(scanResult, dependencyResolutionInput))
                        .peek(elementsContainer::addElementContext)
                        .toList();

                if(!elementContexts.isEmpty()) {
                    log.debug("Assembled {} element contexts from methods of parent element context '{}' ({})",
                            elementContexts.size(), parentElementContext.getName(), parentElementContext.getType().getName());
                }
            }
        }
    }

    private void addPreConstructedElementContexts(ElementsContainer elementsContainer, Application application) {
        elementsContainer.addElementContext(PreConstructedElementContext.of(elementsContainer));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application.getProfilesContainer()));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application.getPropertiesContainer()));
    }

}
