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
package org.tframework.core.elements.context;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.assembler.ElementAssembler;
import org.tframework.core.elements.assembler.ElementAssemblersFactory;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.postprocessing.ElementInstancePostProcessorAggregator;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.annotations.PreScannedAnnotations;
import org.tframework.core.reflection.field.SimpleFieldScanner;
import org.tframework.core.reflection.methods.DeclaredMethodScanner;

/**
 * A wrapper for an element, that keeps track of all the data of this element, and
 * all currently known instances as well. It handles element lifecycle, and is responsible
 * for creating new instances of the element.
 */
@Slf4j
@Getter
public abstract class ElementContext {

    /**
     * Unique name of the element.
     */
    protected final String name;

    /**
     * Type of the element (which is the type of it's {@link #source}).
     */
    protected final Class<?> type;

    /**
     * {@link ElementScope} of the element.
     */
    protected final ElementScope scope;

    /**
     * {@link ElementSource} of the element. Contains information about where the element was found.
     */
    protected final ElementSource source;

    /**
     * The {@link ElementAssembler} that is responsible for creating instances of this element.
     */
    protected final ElementAssembler elementAssembler;

    protected final DependencyResolutionInput dependencyResolutionInput;

    /**
     * {@link PreScannedAnnotations} that were found on the element source. Can be
     * used to reduce the amount of scanning that needs to be done.
     */
    protected PreScannedAnnotations annotationsOnElementSource;

    /**
     * {@link PreScannedAnnotations} that were found on the elements construction time dependencies.
     * Such as constructor parameters or method parameters.
     */
    protected Map<Parameter, PreScannedAnnotations> annotationsOnConstructionParams;

    /**
     * {@link PreScannedAnnotations} that were found on the methods of the element.
     */
    protected Map<Method, PreScannedAnnotations> annotationsOnMethods;

    /**
     * {@link PreScannedAnnotations} that were found on the fields of the element.
     */
    protected Map<Field, PreScannedAnnotations> annotationsOnFields;

    @Setter
    private ElementInstancePostProcessorAggregator postProcessor;

    /**
     * Creates a new element context. To activate this context, {@link #initialize()} must also be called.
     * @param name The name of the element. If this is {@link Element#NAME_NOT_SPECIFIED}, then a name
     *             will be assigned based on the type, using {@link ElementUtils#getElementNameByType(Class)}.
     * @param type Type of the element.
     * @param scope {@link ElementScope} of this element.
     * @param source {@link ElementSource} describing where this element was found.
     * @param dependencyResolutionInput Input that enables this context to resolve its own dependencies.
     */
    protected ElementContext(
            @NonNull String name,
            @NonNull Class<?> type,
            @NonNull ElementScope scope,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        this.name = Element.NAME_NOT_SPECIFIED.equals(name) ? ElementUtils.getElementNameByType(type) : name;
        this.type = type;
        this.scope = scope;
        this.source = source;
        this.elementAssembler = initializeElementAssembler(dependencyResolutionInput);
        this.dependencyResolutionInput = dependencyResolutionInput;
        preScanAnnotations();
    }

    private ElementAssembler initializeElementAssembler(DependencyResolutionInput dependencyResolutionInput) {
        return ElementAssemblersFactory.createElementAssembler(this, dependencyResolutionInput);
    }

    private void preScanAnnotations() {
        var annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
        log.trace("Pre-scanning annotations on element source: {}", source.annotatedSource());
        annotationsOnElementSource = PreScannedAnnotations.fromScanned(
                source.annotatedSource(), annotationScanner.scan(source.annotatedSource())
        );

        var methodScanner = new DeclaredMethodScanner();
        var methods = methodScanner.scanMethods(type);
        log.trace("Pre-scanning annotations on element {} methods", methods.size());
        annotationsOnMethods = preScan(methods, annotationScanner);

        var fieldScanner = new SimpleFieldScanner();
        var fields = fieldScanner.getAllFields(type);
        log.trace("Pre-scanning annotations on element {} fields", fields.size());
        annotationsOnFields = preScan(fields, annotationScanner);

        var params = source.elementConstructionParameters();
        log.trace("Pre-scanning annotations on element {} construction parameters", params.size());
        annotationsOnConstructionParams = preScan(params, annotationScanner);
    }

    private <E extends AnnotatedElement> Map<E, PreScannedAnnotations> preScan(Collection<E> toScan, AnnotationScanner annotationScanner) {
        return toScan.stream().collect(Collectors.toMap(
                element -> element,
                element -> PreScannedAnnotations.fromScanned(element, annotationScanner.scan(element))
        ));
    }

    /**
     * Initializes this element context so that it is ready to create instances of the element.
     * This method should handle gracefully if the element is already initialized previously.
     */
    public abstract void initialize();

    /**
     * Requests this element context to return with an instance of the element. The instance will
     * be post-processed and initialized.
     * Depending on the implementation, this might reuse an existing instance, or create a new one.
     */
    public Object requestInstance() {
        return requestInstance(ElementDependencyGraph.empty());
    }

    /**
     * Requests this element context to return with an instance of the element, continuing the
     * dependency resolution process with the given {@link ElementDependencyGraph}. The instance will
     * be post-processed and initialized.
     * @param dependencyGraph The {@link ElementDependencyGraph} to use for dependency resolution.
     */
    public Object requestInstance(ElementDependencyGraph dependencyGraph) {
        var instanceRequest = requestInstanceInternal(dependencyGraph);
        if(!instanceRequest.reused()) {
            postProcessInstance(instanceRequest.instance());
        }
        return instanceRequest.instance();
    }

    /**
     * Request an instance of this element. Depending on the implementation, this may create a new one, or
     * re-use an exiting one. It is not the responsibility of this method to perform and post-processing
     * or initialization on the instance.
     * @param dependencyGraph The {@link ElementDependencyGraph} to use for dependency resolution.
     * @return An {@link InstanceRequest} with details about the returned instance.
     */
    protected abstract InstanceRequest requestInstanceInternal(ElementDependencyGraph dependencyGraph);

    protected void postProcessInstance(Object instance) {
        if(postProcessor != null) {
            postProcessor.postProcessInstance(dependencyResolutionInput.application(), this, instance);
        } else {
            log.debug("No post-processor set for element context '{}', instance will not be post-processed.", name);
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ElementContext elementContext && elementContext.name.equals(name);
    }

    @Override
    public String toString() {
        return "ElementContext{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", scope=" + scope +
                ", source=" + source +
                '}';
    }
}
