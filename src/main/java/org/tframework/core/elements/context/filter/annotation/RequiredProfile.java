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
package org.tframework.core.elements.context.filter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Can be placed on element classes or methods, to declare that the element
 * requires a profile (or profiles) to be activated. For example, if we want
 * {@code SomeElement} to be only active when profiles {@code a} and {@code b} are both set:
 *
 * <pre>{@code
 * @Element
 * @RequiredProfile({"a", "b"})
 * public class SomeElement {}
 * }</pre>
 *
 * This annotation will have no effect when placed on something that is not an element.
 * @see ForbiddenProfile
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredProfile {

    /**
     * The profiles that are required for the element to be activated.
     */
    String[] value();

}
