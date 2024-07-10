/*
Copyright 2023 Tamas Gaspar

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
package org.tframework.core.reflection.classes;

import java.util.Set;

/**
 * The class scanner is responsible for finding classes. How and where the scan
 * looks is implementation dependent.
 * @see ClassScannersFactory
 */
public interface ClassScanner {

    /**
     * Find and collect the classes. This operation may take time, so the result should be
     * saved for future use instead of calling this again and again.
     * @return {@link Set} of {@link Class} objects that this scanner found.
     */
    Set<Class<?>> scanClasses();

}
