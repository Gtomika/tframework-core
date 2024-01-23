/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.source;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.utils.LogUtils;

/**
 * An {@link ElementSource} for elements that are defined by a method (the method is annotated
 * by {@link org.tframework.core.elements.annotations.Element}).
 * @param method The method that defines the element. It must not be null.
 * @param parentElementContext The {@link ElementContext} of element whose class contains {@code method}.
 *                             This parent element context is required to be able to invoke {@code method}.
 */
public record MethodElementSource(
        @NonNull Method method,
        @NonNull ElementContext parentElementContext
) implements ElementSource {

    @Override
    public List<Parameter> elementConstructionParameters() {
        return Arrays.asList(method.getParameters());
    }

    @Override
    public String toString() {
        return "MethodElementSource{" + "method=" + LogUtils.niceExecutableName(method) +
                ", parentElementContext=" + parentElementContext.getName() + "}";
    }
}
