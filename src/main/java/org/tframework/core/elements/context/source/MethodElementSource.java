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
package org.tframework.core.elements.context.source;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.utils.LogUtils;

/**
 * An {@link ElementSource} for elements that are defined by a method (the method is annotated by {@link Element}).
 * @param method The method that defines the element. It must not be null.
 * @param parentElementContext The {@link ElementContext} of element whose class contains {@code method}.
 *                             This parent element context is required to be able to invoke {@code method}.
 */
public record MethodElementSource(
        @NonNull Method method,
        @NonNull ElementContext parentElementContext
) implements ElementSource {

    @Override
    public AnnotatedElement annotatedSource() {
        return method;
    }

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
