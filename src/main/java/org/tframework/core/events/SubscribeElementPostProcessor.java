/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.postprocessing.ElementInstancePostProcessor;
import org.tframework.core.events.annotations.Subscribe;
import org.tframework.core.events.exception.ElementPublishingException;
import org.tframework.core.events.exception.ElementSubscriptionException;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodInvocationException;
import org.tframework.core.reflection.methods.MethodInvoker;
import org.tframework.core.utils.LogUtils;

@Slf4j
@Element
@RequiredArgsConstructor
public class SubscribeElementPostProcessor implements ElementInstancePostProcessor {

    private final AnnotationScanner annotationScanner;
    private final MethodFilter methodFilter;
    private final MethodInvoker methodInvoker;
    private final EventManager eventManager;

    @Override
    public void postProcessInstance(ElementContext elementContext, Object instance) {
        methodFilter.filterByAnnotation(
                elementContext.getMethods(),
                Subscribe.class,
                annotationScanner,
                true
        ).stream()
                .peek(result -> {
                    checkValidSubscribeMethod(elementContext, result.annotationSource());
                    log.debug("Element context '{}', method '{}': valid subscribe method",
                            elementContext.getName(), LogUtils.niceExecutableName(result.annotationSource()));
                })
                .forEach(method -> subscribe(elementContext, method, instance));
    }

    private void checkValidSubscribeMethod(ElementContext elementContext, Method method) {
        List<String> problems = new LinkedList<>();

        if(!methodFilter.isPublic(method)) {
            problems.add("method is not public");
        }
        if(methodFilter.isStatic(method)) {
            problems.add("method is static");
        }
        if(methodFilter.isAbstract(method)) {
            problems.add("method is abstract");
        }
        if(!methodFilter.hasExactlyOneParameter(method)) {
            problems.add("method does not have exactly one parameter");
        }

        if(!problems.isEmpty()) {
            throw new ElementSubscriptionException(elementContext, method, problems);
        }
    }

    private void subscribe(
            ElementContext elementContext,
            AnnotationFilteringResult<Subscribe, Method> result,
            Object instance
    ) {
        Subscribe subscribeAnnotation = result.annotation();
        Method method = result.annotationSource();

        UUID subscriptionId = eventManager.subscribe(subscribeAnnotation.value(), payload -> {
            try {
                methodInvoker.invokeMethodWithOneParameterAndIgnoreResult(instance, method, payload);
            } catch (MethodInvocationException e) {
                throw new ElementPublishingException(elementContext, method, e);
            }
        });
        log.debug("Element context '{}', method '{}': subscribed to topic '{}', subscription ID '{}'",
                elementContext.getName(), LogUtils.niceExecutableName(method), subscribeAnnotation.value(), subscriptionId);
    }
}
