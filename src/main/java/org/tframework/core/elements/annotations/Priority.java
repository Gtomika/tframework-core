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
package org.tframework.core.elements.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates priority (ordering) between certain elements. This is useful when you have multiple
 * elements of the same type, and you want to specify which one should be used first. Notes:
 * <ul>
 *     <li>
 *         It can be placed on element classes or methods.
 *     </li>
 *     <li>
 *         If the priority of two elements are equal, there is no guarantee of their usage order.
 *     </li>
 *     <li>
 *         Priority is usually an optional control mechanism. If not specified, it will use
 *         {@link #DEFAULT} during ordering.
 *     </li>
 * </ul>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Priority {

    /**
     * Default priority value to be used when this annotation is not present.
     */
    int DEFAULT = 0;

    /**
     * The lowest possible priority value. Used when the element should be used last.
     */
    int LOWEST = Integer.MIN_VALUE;

    /**
     * The highest possible priority value. Used when the element should be used first.
     */
    int HIGHEST = Integer.MAX_VALUE;

    /*
     * The priority value. The higher the value, the higher the priority.
     */
    int value();

}
