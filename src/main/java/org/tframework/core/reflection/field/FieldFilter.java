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
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;

/**
 * Filters a collection of {@link Field}s (usually produced by a {@link FieldScanner})
 * based on certain criteria, such as having an annotation.
 */
public interface FieldFilter {

    /**
     * Checks if the given fields has the static modifier.
     */
    boolean isStatic(Field field);

    /**
     * Checks if the given field has the final modifier.
     */
    boolean isFinal(Field field);
}
