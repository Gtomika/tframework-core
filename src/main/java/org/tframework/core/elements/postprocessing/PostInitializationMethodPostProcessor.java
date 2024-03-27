/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.postprocessing.annotations.PostInitialization;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodInvoker;
import org.tframework.core.reflection.methods.MethodScanner;

/**
 * This {@link ElementInstancePostProcessor} is responsible for finding methods of
 * elements that are annotated with {@link PostInitialization}, and invoke them.
 * @see PostInitialization
 */
@Slf4j
@Builder(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostInitializationMethodPostProcessor implements ElementInstancePostProcessor {

    private final AnnotationScanner annotationScanner;
    private final MethodScanner methodScanner;
    private final MethodFilter methodFilter;
    private final MethodInvoker methodInvoker;

    @Override
    public void postProcessInstance(ElementContext elementContext, Object instance) {
        var allMethods = methodScanner.scanMethods(elementContext.getType());

        methodFilter.filterByAnnotation(
                allMethods,
                PostInitialization.class,
                annotationScanner,
                false
        ).stream()
                .map(AnnotationFilteringResult::annotationSource)
                .filter(method -> isValidPostConstructMethod(elementContext, method))
                .peek(method -> log.debug("Element context '{}', method '{}': valid post-initialization method",
                        elementContext.getName(), method.getName()))
                .forEach(method -> methodInvoker.invokeMethodWithNoParametersAndIgnoreResult(instance, method));
    }

    private boolean isValidPostConstructMethod(ElementContext elementContext, Method method) {
        List<String> problems = new LinkedList<>();

        if(!methodFilter.isPublic(method)) {
            problems.add("method is not public");
        }
        if(!methodFilter.isStatic(method)) {
            problems.add("method is static");
        }
        if(!methodFilter.isPublic(method)) {
            problems.add("method abstract");
        }
        if(!methodFilter.isPublic(method)) {
            problems.add("method has parameters");
        }

        if(problems.isEmpty()) {
            //having a return value is not a problem, but it is pointless
            if(!methodFilter.hasVoidReturnType(method)) {
                log.warn("Element context '{}', post-initialization method '{}': has return value, which will be ignored",
                        elementContext.getName(), method.getName());
            }
            return true;
        } else {
            log.warn("Element context '{}', post-initialization method '{}': problems found, this method will not be invoked: {}",
                    elementContext.getName(), method.getName(), problems);
            return false;
        }
    }
}
