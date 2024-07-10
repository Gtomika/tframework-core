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
package org.tframework.core.elements.dependency.handler;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecialElementDependencyHandlerFactory {

    /**
     * Creates a {@link SpecialDependencyHandlerAggregator} with a default list of
     * {@link SpecialElementDependencyHandler}s for the framework to use.
     */
    public static SpecialDependencyHandlerAggregator createDefaultHandlerAggregator() {
        var handlers = List.of(
                new ListDependencyHandler(),
                new ArrayDependencyHandler(),
                new OptionalDependencyHandler(),
                new StringMapDependencyHandler()
        );
        return SpecialDependencyHandlerAggregator.usingHandlers(handlers);
    }
}
