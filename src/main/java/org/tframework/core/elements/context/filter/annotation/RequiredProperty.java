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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be placed on elements so that they can be filtered out based on
 * properties. For example, to activate an element only when {@code my.property} is present:
 *
 * <pre>{@code
 * @Element
 * @RequiredProperty(name = "my.property")
 * public class SomeElement {}
 * }</pre>
 *
 * To activate an element only when {@code my.property} is set to {@code value}:
 *
 * <pre>{@code
 * @Element
 * @RequiredProperty(name = "my.property", hasValue = "value")
 * public class SomeElement {}
 * }</pre>
 *
 * You can freely combine this annotation with other filters, or use it multiple times on the same element.
 * These will be connected with a logical AND.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatedRequiredProperty.class)
public @interface RequiredProperty {

    String VALUE_NOT_SET = "";

    /**
     * Required property name to check for.
     */
    String name();

    /**
     * An optional property value to check for. If not set, the property must be present,
     * but its value is not checked.
     */
    String hasValue() default VALUE_NOT_SET;

    /**
     * An optional negated property value. If not set, the property must be present,
     * but its value is not checked.
     */
    String hasNoValue() default VALUE_NOT_SET;
}
