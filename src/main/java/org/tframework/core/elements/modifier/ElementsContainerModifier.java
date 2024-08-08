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
package org.tframework.core.elements.modifier;

import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * This interface provides access to the {@link ElementsContainer} of the application,
 * allowing to programmatically add or remove elements from the container. To use this
 * functionality, you can create a class that implements this interface and mark
 * it as an {@link Element}. It will be picked up and used by the framework.
 */
public interface ElementsContainerModifier {

    /**
     * Process the {@link ElementsContainer}. This method is called after all element contexts have been
     * scanner and also filtered, but before a full initialization of the container.
     * @param elementsContainer The container to process.
     * @param dependencyResolutionInput The input for the dependency resolution process. This is provided
     *                                  as an argument to allow the processor to add elements to the container.
     */
    void modifyElementsContainer(
            ElementsContainer elementsContainer,
            DependencyResolutionInput dependencyResolutionInput
    );
}
