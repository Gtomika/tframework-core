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
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * A special {@link ElementSource} for pre-constructed elements.
 */
public record PreConstructedElementSource(
        Object preConstructedInstance
) implements ElementSource {

    @Override
    public AnnotatedElement annotatedSource() {
        return preConstructedInstance.getClass();
    }

    @Override
    public List<Parameter> elementConstructionParameters() {
        //this information is unknown, because this element is constructed outside the framework
        return List.of();
    }
}
