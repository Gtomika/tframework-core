/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.assembler;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.ElementConstructor;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.constructor.ConstructorFilter;
import org.tframework.core.reflection.constructor.ConstructorScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * An {@link ElementContextAssembler} that assembles {@link ElementContext}s from {@link Class}es, that
 * were annotated with {@link org.tframework.core.elements.annotations.Element}. This class performs the following steps:
 * <ul>
 *     <li>Find the appropriate constructor for the element class. See {@link #findAppropriateConstructor(Class)}</li>
 *     <li>Creates {@link org.tframework.core.elements.context.source.ClassElementSource} for the element.</li>
 *     <li>Assembles the {@link ElementContext}.</li>
 * </ul>
 */
@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ClassElementContextAssembler implements ElementContextAssembler<Class<?>> {

    static final String DECLARED_AS = "class";
    static final String NO_PUBLIC_CONSTRUCTORS_ERROR = "No public constructors found.";
    static final String MULTIPLE_PUBLIC_CONSTRUCTORS_ERROR = "Multiple public constructors found. Please annotate" +
            " the appropriate one with " + ElementConstructor.class.getName();
    static final String MULTIPLE_ANNOTATED_CONSTRUCTORS_ERROR = "Found several constructors annotated with " +
            ElementConstructor.class.getName() + ". Only one is allowed.";
    static final String NOT_INSTANTIABLE_ERROR = "Element class is not instantiable. Please make sure it is not an " +
            "interface, abstract class, annotation or enum.";

    private final ConstructorScanner constructorScanner;
    private final ConstructorFilter constructorFilter;
    private final AnnotationScanner annotationScanner;

    @Override
    public ElementContext assemble(
            ElementScanningResult<Class<?>> scanningResult,
            DependencyResolutionInput dependencyResolutionInput
    ) throws ElementContextAssemblingException {
        var elementClass = scanningResult.annotationSource();
        validateElementType(elementClass);
        var elementSource = new ClassElementSource(findAppropriateConstructor(elementClass));
        log.trace("Created element source for element class '{}': {}", elementClass.getName(), elementSource);

        Element elementAnnotation = scanningResult.elementAnnotation();
        ElementContext elementContext = ElementContext.from(
                elementAnnotation, elementClass, elementSource, dependencyResolutionInput
        );
        log.debug("Created element context for element class '{}' annotated with '{}'",
                elementClass.getName(), ElementUtils.stringifyElementAnnotation(elementAnnotation));
        return elementContext;
    }

    /**
     * Validates that the element type can be used to assemble an element context. The rules of validation are:
     * <ul>
     *     <li>Must not be anything that cannot be instantiated: interface, abstract class, annotation, enum.</li>
     * </ul>
     */
    private void validateElementType(Class<?> elementType) {
        if(elementType.isInterface() || Modifier.isAbstract(elementType.getModifiers()) ||
                elementType.isAnnotation() || elementType.isEnum()) {
            throw new ElementContextAssemblingException(elementType, DECLARED_AS, elementType.getName(), NOT_INSTANTIABLE_ERROR);
        }
    }

    /**
     * Finds the appropriate constructor for the element class. The rules of finding the appropriate constructor are:
     * <ul>
     *     <li>Only public constructors are checked.</li>
     *     <li>If there is only one public constructor, that one will be used.</li>
     *     <li>
     *         If there are multiple public constructors, the one annotated with
     *         {@link ElementConstructor} will be used.
     *     </li>
     *     <li>
     *         Multiple annotated constructors are not allowed, and will result in an
     *         {@link ElementContextAssemblingException}.
     *     </li>
     * </ul>
     * @param elementClass The element class to find the appropriate constructor for.
     * @throws ElementContextAssemblingException If the appropriate constructor could not be selected.
     */
    private Constructor<?> findAppropriateConstructor(Class<?> elementClass) {
        var publicConstructors = constructorFilter.filterPublicConstructors(constructorScanner.getAllConstructors(elementClass));
        if(publicConstructors.isEmpty()) {
            throw new ElementContextAssemblingException(elementClass, DECLARED_AS, elementClass.getName(), NO_PUBLIC_CONSTRUCTORS_ERROR);
        }
        log.trace("Found {} public constructors for element class '{}'", publicConstructors.size(), elementClass.getName());

        //if only 1 public constructor, use that one
        if(publicConstructors.size() == 1) {
            log.trace("Only 1 public constructor found for element class '{}', using that one.", elementClass.getName());
            return publicConstructors.stream().findAny().get();
        }

        var annotatedConstructors = constructorFilter.filterByAnnotation(
                publicConstructors,
                ElementConstructor.class,
                annotationScanner,
                false //a constructor may be marked multiple times with the same annotation, but it does not matter
        )
                .stream()
                .map(AnnotationFilteringResult::annotationSource)
                .toList();
        log.trace("Found {} constructors annotated, for element class '{}'", annotatedConstructors.size(), elementClass.getName());

        //there are multiple public constructors, but none of them are annotated with @ElementConstructor
        if(annotatedConstructors.isEmpty()) {
            throw new ElementContextAssemblingException(elementClass, DECLARED_AS, elementClass.getName(), MULTIPLE_PUBLIC_CONSTRUCTORS_ERROR);
        }

        //there are multiple public constructors, and several of them is annotated with @ElementConstructor
        if(annotatedConstructors.size() > 1) {
            throw new ElementContextAssemblingException(elementClass, DECLARED_AS, elementClass.getName(), MULTIPLE_ANNOTATED_CONSTRUCTORS_ERROR);
        }

        //there is exactly one public constructor that is annotated with @ElementConstructor, use that
        log.trace("Found exactly 1 public constructor annotated with @ElementConstructor, for element class '{}', using that one.", elementClass.getName());
        return annotatedConstructors.stream()
                .findAny()
                .get();
    }

}
