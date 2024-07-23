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
import org.tframework.core.elements.context.filter.FilteringRound;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.modifier.ElementsContainerModifier;
import org.tframework.core.elements.modifier.ElementsContainerModifierAggregator;
import org.tframework.core.elements.postprocessing.ElementInstancePostProcessor;
import org.tframework.core.elements.postprocessing.ElementInstancePostProcessorAggregator;
import org.tframework.core.elements.scanner.ElementClassScanner;
import org.tframework.core.elements.scanner.ElementContextBundle;
import org.tframework.core.elements.scanner.ElementMethodScanner;
import org.tframework.core.elements.scanner.ElementScanner;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.converters.PropertyConverter;
import org.tframework.core.properties.converters.PropertyConverterAggregator;
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
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ElementsInitializationProcess {

    /**
     * Initializes the elements.
     * @param input The {@link ElementsInitializationInput} containing the input data for the process.
     * @param contextBundle An {@link ElementContextBundle} which describes how to find and process {@link ElementContext}s.
     *                      (see {@link ElementsInitializationProcessFactory#createDefaultElementContextBundle(ElementsInitializationInput)}.
     * @return the {@link ElementsContainer} containing the assembled {@link ElementContext}s
     */
    public ElementsContainer initialize(ElementsInitializationInput input, ElementContextBundle contextBundle) {
        var elementsContainer = ElementsContainer.empty();
        input.application().setElementsContainer(elementsContainer);

        DependencyResolutionInput dependencyResolutionInput = DependencyResolutionInput.builder()
                .application(input.application())
                .build();

        assembleElementContexts(elementsContainer, contextBundle, dependencyResolutionInput);
        addPreConstructedElementContexts(
                elementsContainer,
                input.application(),
                input.preConstructedElementData()
        );
        log.info("Successfully assembled a total of {} element contexts", elementsContainer.elementCount());

        addPropertyConverters(input.application().getPropertiesContainer(), elementsContainer);

        filterElementContext(elementsContainer, input.application());
        log.info("A total of {} element contexts survived after filtering", elementsContainer.elementCount());

        processElementContainer(elementsContainer, dependencyResolutionInput);

        findElementInstancePostProcessors(elementsContainer);

        elementsContainer.initializeElementContexts();
        log.info("Successfully initialized {} element contexts", elementsContainer.elementCount());

        return elementsContainer;
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
            Set<PreConstructedElementData> preConstructedElementData
    ) {
        //certain objects are added by default as pre-constructed elements
        elementsContainer.addElementContext(PreConstructedElementContext.of(elementsContainer));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application.getProfilesContainer()));
        elementsContainer.addElementContext(PreConstructedElementContext.of(application.getPropertiesContainer()));

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

    @SuppressWarnings("unchecked")
    private void addPropertyConverters(PropertiesContainer propertiesContainer, ElementsContainer elementsContainer) {
        var propertyConverters = (List<PropertyConverter<?>>) (List<?>) ElementUtils.getElementInstances(elementsContainer, PropertyConverter.class);
        log.debug("Found {} property converters: {}", propertyConverters.size(), LogUtils.objectClassNames(propertyConverters));
        var propertyConverterAggregator = PropertyConverterAggregator.usingConverters(propertyConverters);
        propertiesContainer.setPropertyConverterAggregator(propertyConverterAggregator);
    }

    private void filterElementContext(ElementsContainer elementsContainer, Application application) {
        var filters = ElementUtils.getElementInstances(application.getElementsContainer(), ElementContextFilter.class);
        log.debug("Found {} filters to apply to element contexts: {}", filters.size(), LogUtils.objectClassNames(filters));
        var filterAggregator = ElementContextFilterAggregator.usingFilters(filters);

        Set<ElementContext> allDiscardedContexts = new HashSet<>();

        for(FilteringRound round: FilteringRound.values()) {
            log.debug("Applying filters in round {}", round);
            List<ElementContext> discardedContextsInRound = new LinkedList<>();

            elementsContainer.forEach(context -> filterContext(application, filterAggregator, context, discardedContextsInRound, round));
            discardedContextsInRound.forEach(elementsContainer::removeElementContext);
            allDiscardedContexts.addAll(discardedContextsInRound);
        }

        log.debug("A total of {} element contexts have been filtered out, and will be discarded", allDiscardedContexts.size());
    }

    private void filterContext(
            Application application,
            ElementContextFilterAggregator aggregator,
            ElementContext context,
            List<ElementContext> discardedContexts,
            FilteringRound round
    ) {
        if(aggregator.discardElementContext(context, application, round)) {
            discardedContexts.add(context);
            log.debug("The element context '{}' is filtered out, and marked for discarding", context.getName());
        }
    }

    private void processElementContainer(ElementsContainer elementsContainer, DependencyResolutionInput dependencyResolutionInput) {
        var processors = ElementUtils.getElementInstances(elementsContainer, ElementsContainerModifier.class);
        log.debug("Found {} processors to apply to element container: {}", processors.size(), LogUtils.objectClassNames(processors));

        var aggregator = ElementsContainerModifierAggregator.usingProcessors(processors);
        aggregator.modifyElementsContainer(elementsContainer, dependencyResolutionInput);
    }

    private void findElementInstancePostProcessors(ElementsContainer elementsContainer) {
        var postProcessors = ElementUtils.getElementInstances(elementsContainer, ElementInstancePostProcessor.class);
        log.debug("Found {} post-processors to apply to element instances: {}", postProcessors.size(), LogUtils.objectClassNames(postProcessors));

        var postProcessorAggregator = ElementInstancePostProcessorAggregator.usingPostProcessors(postProcessors);
        elementsContainer.forEach(context -> context.setPostProcessor(postProcessorAggregator));
    }
}
