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
 * The element source is a common interface for classes that define where an element was discovered.
 * Depending on the source of the element, its construction may be different.
 */
public interface ElementSource {

    /**
     * @return That {@link AnnotatedElement} that was scanned, and that produced the element.
     */
    AnnotatedElement annotatedSource();

    /**
     * @return A list of {@link Parameter}s that are required to construct the element.
     * Classes responsible for constructing the element should use this list to determine
     * what kind of dependencies are required for element construction.
     */
    List<Parameter> elementConstructionParameters();

}
