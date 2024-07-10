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
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.utils.LogUtils;

/**
 * An {@link ElementSource} for elements that are defined by a class (the class is annotated by {@link Element}).
 * @param elementClass The class that was marked as an element.
 * @param constructor The constructor of the source class that will be used to create the element instance(s).
 *                    It must not be null.
 */
public record ClassElementSource(
        @NonNull Class<?> elementClass,
        @NonNull Constructor<?> constructor
) implements ElementSource {

    @Override
    public AnnotatedElement annotatedSource() {
        return elementClass;
    }

    @Override
    public List<Parameter> elementConstructionParameters() {
        return Arrays.asList(constructor.getParameters());
    }

    @Override
    public String toString() {
        return "ClassElementSource{class=" + elementClass.getName() +
                ", constructor=" + LogUtils.niceExecutableName(constructor) + "}" ;
    }
}
