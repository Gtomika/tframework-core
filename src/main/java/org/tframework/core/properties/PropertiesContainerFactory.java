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
package org.tframework.core.properties;

import java.util.List;
import lombok.NonNull;

/**
 * Creates instances of the {@link PropertiesContainer}.
 */
public class PropertiesContainerFactory {

    /**
     * Creates a {@link PropertiesContainer} from the given list of properties.
     * @param properties Properties list to create the container from, cannot be null.
     */
    public static PropertiesContainer fromProperties(@NonNull List<Property> properties) {
        return new PropertiesContainer(properties);
    }

    /**
     * Creates an empty {@link PropertiesContainer}.
     */
    public static PropertiesContainer empty() {
        return fromProperties(List.of());
    }

}
