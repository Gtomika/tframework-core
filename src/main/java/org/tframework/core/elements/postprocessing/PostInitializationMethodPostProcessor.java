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
package org.tframework.core.elements.postprocessing;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.postprocessing.annotations.PostInitialization;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodInvoker;
import org.tframework.core.utils.LogUtils;

/**
 * This {@link ElementInstancePostProcessor} is responsible for finding methods of
 * elements that are annotated with {@link PostInitialization}, and invoke them. For the
 * rules of what methods can be annotated, see {@link PostInitialization}. This post processor
 * is executed after all other post processors, so that the element is fully initialized
 * before the post-initialization method is invoked.
 */
@Slf4j
@Element
@Priority(Priority.LOWEST)
@RequiredArgsConstructor
public class PostInitializationMethodPostProcessor implements ElementInstancePostProcessor {

    private final AnnotationScanner annotationScanner;
    private final MethodFilter methodFilter;
    private final MethodInvoker methodInvoker;

    @Override
    public void postProcessInstance(ElementContext elementContext, Object instance) {
        methodFilter.filterByAnnotation(
                elementContext.getMethods(),
                PostInitialization.class,
                annotationScanner,
                false
        ).stream()
                .map(AnnotationFilteringResult::annotationSource)
                .peek(method -> {
                    checkValidPostInitializationMethod(elementContext, method);
                    log.debug("Element context '{}', method '{}': valid post-initialization method",
                            elementContext.getName(), LogUtils.niceExecutableName(method));
                })
                .forEach(method -> invokePostInitializationMethod(elementContext, method, instance));
    }

    private void checkValidPostInitializationMethod(ElementContext elementContext, Method method) {
        List<String> problems = new LinkedList<>();

        if(!methodFilter.isPublic(method)) {
            problems.add("method is not public");
        }
        if(methodFilter.isStatic(method)) {
            problems.add("method is static");
        }
        if(methodFilter.isAbstract(method)) {
            problems.add("method abstract");
        }
        if(methodFilter.hasParameters(method)) {
            problems.add("method has parameters");
        }

        if(problems.isEmpty()) {
            //having a return value is not a problem, but it is pointless
            if(!methodFilter.hasVoidReturnType(method)) {
                log.warn("Element context '{}', post-initialization method '{}': has return value, which will be ignored",
                        elementContext.getName(), LogUtils.niceExecutableName(method));
            }
        } else {
            throw new PostInitializationMethodException(method, elementContext, problems);
        }
    }

    private void invokePostInitializationMethod(ElementContext elementContext, Method method, Object instance) {
        try {
            methodInvoker.invokeMethodWithNoParametersAndIgnoreResult(instance, method);
        } catch (Exception e) {
            throw new PostInitializationMethodException(method, elementContext, e);
        }
    }
}
