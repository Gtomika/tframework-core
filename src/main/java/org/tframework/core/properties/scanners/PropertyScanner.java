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
package org.tframework.core.properties.scanners;

import java.util.List;

/**
 * Property scanners find raw property name-value pairs. It is not responsible for further processing
 * or validating the scanned raw values and usually passes on raw properties.
 * to a {@link org.tframework.core.properties.parsers.PropertyParser}.
 */
public interface PropertyScanner {

    /**
     * Finds a list of raw properties.
     */
    List<String> scanProperties();

    /**
     * An informative name from where this scanner looks up properties.
     */
    String sourceName();

}
