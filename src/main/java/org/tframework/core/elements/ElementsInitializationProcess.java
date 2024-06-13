/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.PreConstructedElementContext;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.ElementContextAssembler;
import org.tframework.core.elements.context.assembler.MethodElementContextAssembler;
import org.tframework.core.elements.context.filter.ElementContextFilter;
import org.tframework.core.elements.context.filter.ElementContextFilterAggregator;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.postprocessing.ElementInstancePostProcessor;
import org.tframework.core.elements.postprocessing.ElementInstancePostProcessorAggregator;
import org.tframework.core.elements.scanner.ElementClassScanner;
import org.tframework.core.elements.scanner.ElementContextBundle;
import org.tframework.core.elements.scanner.ElementMethodScanner;
import org.tframework.core.elements.scanner.ElementScanner;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.properties.converters.PropertyConvertersFactory;
import org.tframework.core.utils.Constants;
import org.tframework.core.utils.LogUtils;

/**
 * This class is responsible for the elements initialization process. This process consists of the following steps:
 * <ul>
 *     <li>Scanning for elements (see {@link ElementScanner}s).</li>
 *     <li>Assembling {@link ElementContext}s (see {@link ElementContextAssembler}s).</li>
 *     <li>Filtering out elements using {@link ElementContextFilter}s.</li>
 *     <li>Initializes each element context (see {@link ElementContext#initialize()}).</li>
 * </ul>
 * The result of the process will be an {@link ElementsContainer} with unique {@link ElementContext}s.
 * <p><br>
 * The {@value ELEMENTS_INITIALIZATION_ENABLED_PROPERTY} can be set to {@code false}, which will
 * disable element initialization all together. By default, this property is treated as {@code true}, so
 * elements are initialized.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ElementsInitializationProcess {

    public static final String ELEMENTS_INITIALIZATION_ENABLED_PROPERTY =
            Constants.TFRAMEWORK_PROPERTIES_PREFIX + ".elements.enabled";
    private static final SinglePropertyValue ELEMENTS_INITIALIZATION_PROPERTY_DEFAULT = new SinglePropertyValue("true");

    /**
     * Initializes the elements.
     * @param input The {@link ElementsInitializationInput} containing the input data for the process.
     * @param contextBundle An {@link ElementContextBundle} which describes how to find and process {@link ElementContext}s.
     *                      (see {@link ElementsInitializationProcessFactory#createDefaultElementContextBundle(ElementsInitializationInput)}.
     * @return the {@link ElementsContainer} containing the assembled {@link ElementContext}s
     */
    public ElementsContainer initialize(ElementsInitializationInput input, ElementContextBundle contextBundle) {
        if(isElementInitializationDisabled(input.application().getPropertiesContainer())) {
            log.info("The element initialization is disabled. No elements will be scanned, dependency injection is disabled.");
            return ElementsContainer.empty();
        }

        var elementsContainer = ElementsContainer.empty();
        input.application().setElementsContainer(elementsContainer);

        DependencyResolutionInput dependencyResolutionInput = DependencyResolutionInput.builder()
                .elementsContainer(elementsContainer)
                .propertiesContainer(input.application().getPropertiesContainer())
                .build();

        assembleElementContexts(elementsContainer, contextBundle, dependencyResolutionInput);
        addPreConstructedElementContexts(
                elementsContainer,
                input.application(),
                input.preConstructedElementData(),
                dependencyResolutionInput
        );
        log.info("Successfully assembled a total of {} element contexts", elementsContainer.elementCount());

        filterElementContext(elementsContainer, input.application());
        log.info("A total of {} element contexts survived after filtering", elementsContainer.elementCount());

        var postProcessors = ElementUtils.initAndGetElementsEagerly(elementsContainer, ElementInstancePostProcessor.class);
        log.debug("Found {} post-processors to apply to element instances: {}", postProcessors.size(), LogUtils.objectClassNames(postProcessors));
        var postProcessorAggregator = ElementInstancePostProcessorAggregator.usingPostProcessors(postProcessors);
        elementsContainer.forEach(context -> context.setPostProcessor(postProcessorAggregator));

        elementsContainer.initializeElementContexts();
        log.info("Successfully initialized {} element contexts", elementsContainer.elementCount());

        return elementsContainer;
    }

    private boolean isElementInitializationDisabled(PropertiesContainer properties) {
        var booleanPropertyConverter = PropertyConvertersFactory.getConverterByType(Boolean.class);
        var elementInitializationEnabled = properties.getPropertyValueObject(
                ELEMENTS_INITIALIZATION_ENABLED_PROPERTY, ELEMENTS_INITIALIZATION_PROPERTY_DEFAULT
        );
        return !booleanPropertyConverter.convert(elementInitializationEnabled);
    }

    /**
     * Uses the scanners in the {@link ElementContextBundle} to find elements,
     * then assembles {@link ElementContext}s from them. These will be added to the {@link ElementsContainer}.
     * <ul>
     *     <li>
     *         Each {@link ElementClassScanner} in the scanner bundle will find element classes.
     *         These will be assembled into {@link ElementContext}s using the {@link ClassElementContextAssembler}.
     *     </li>
     *     <li>
     *         Then, each {@link ElementMethodScanner} in the scanner bundle will find element methods.
     *         These will be assembled into {@link ElementContext}s using the {@link MethodElementContextAssembler}.
     *         See {@link #assembleMethodElementContexts(ElementsContainer, List, ElementContextBundle, DependencyResolutionInput)}.
     *     </li>
     * </ul>
     */
    private void assembleElementContexts(
            ElementsContainer elementsContainer,
            ElementContextBundle contextBundle,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        Set<ElementScanningResult<Class<?>>> allScannedClassElements = new HashSet<>();
        for(ElementClassScanner elementClassScanner : contextBundle.elementClassScanners()) {
            var scannedElements = elementClassScanner.scanElements();
            allScannedClassElements.addAll(scannedElements);
            log.debug("Scanned {} elements from class scanner '{}'", scannedElements, elementClassScanner.getClass().getName());
        }

        List<ElementContext> elementContexts = allScannedClassElements.stream()
                .map(scanResult -> contextBundle.classElementContextAssembler().assemble(scanResult, dependencyResolutionInput))
                .peek(elementsContainer::addElementContext)
                .toList();

        assembleMethodElementContexts(
                elementsContainer,
                elementContexts,
                contextBundle,
                dependencyResolutionInput
        );
    }

    /**
     * Uses the {@link ElementMethodScanner}s from the {@link ElementContextBundle} to find element methods.
     * These will be assembled into {@link ElementContext}s using the {@link MethodElementContextAssembler}s.
     */
    private void assembleMethodElementContexts(
            ElementsContainer elementsContainer,
            List<ElementContext> parentElementContexts,
            ElementContextBundle contextBundle,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        for(ElementContext parentElementContext : parentElementContexts) {
            contextBundle.methodElementContextAssembler().setParentElementContext(parentElementContext);

            Set<ElementScanningResult<Method>> allScannedMethodElements = new HashSet<>();
            for(var elementMethodScanner : contextBundle.elementMethodScanners()) {
                elementMethodScanner.setClassToScan(parentElementContext.getType());
                var scannedMethodElements = elementMethodScanner.scanElements();
                allScannedMethodElements.addAll(scannedMethodElements);

                if(!scannedMethodElements.isEmpty()) {
                    log.debug("Scanned {} elements from methods of parent element context '{}' ({})",
                            scannedMethodElements.size(), parentElementContext.getName(), parentElementContext.getType().getName());
                }
            }

            allScannedMethodElements.stream()
                    .map(scanResult -> contextBundle.methodElementContextAssembler().assemble(scanResult, dependencyResolutionInput))
                    .forEach(elementsContainer::addElementContext);
        }
    }

    private void addPreConstructedElementContexts(
            ElementsContainer elementsContainer,
            Application application,
            Set<PreConstructedElementData> preConstructedElementData,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        //certain objects are added by default as pre-constructed elements
        elementsContainer.addElementContext(PreConstructedElementContext.of(elementsContainer));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application.getProfilesContainer()));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application.getPropertiesContainer()));
        elementsContainer.addElementContext(PreConstructedElementContext.of(dependencyResolutionInput));

        //custom pre-constructed elements may be provided as well
        preConstructedElementData.forEach(data -> {
            log.debug("Custom pre-constructed element '{}' will be added to elements container.", data.name());
            var preConstructedContext = PreConstructedElementContext.of(data.preConstructedInstance(), data.name());
            if(data.overrideExistingElement()) {
                elementsContainer.overrideElementContext(preConstructedContext);
            } else {
                elementsContainer.addElementContext(preConstructedContext);
            }
        });
    }

    private void filterElementContext(ElementsContainer elementsContainer, Application application) {
        var filters = ElementUtils.initAndGetElementsEagerly(application.getElementsContainer(), ElementContextFilter.class);
        log.debug("Found {} filters to apply to element contexts: {}", filters.size(), LogUtils.objectClassNames(filters));
        var filterAggregator = ElementContextFilterAggregator.usingFilters(filters);

        List<ElementContext> discardedContexts = new LinkedList<>();

        elementsContainer.forEach(elementContext -> {
            if(filterAggregator.discardElementContext(elementContext, application)) {
                discardedContexts.add(elementContext);
                log.debug("The element context '{}' is filtered out, and marked for discarding", elementContext.getName());
            } else {
                log.debug("The element context '{}' survived filtering and will be kept", elementContext.getName());
            }
        });

        log.debug("A total of {} element contexts have been filtered out, and will be discarded", discardedContexts.size());
        discardedContexts.forEach(elementsContainer::removeElementContext);
    }

}
