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
package org.tframework.core.properties.group;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be used to inject property groups into elements. A property group is
 * a collection of properties that share the same prefix. For example this is the {@code my.props}
 * group with properties {@code a} and {@code b}:
 *
 * <pre>{@code
 * my:
 *   props:
 *     a: 1
 *     b: 2
 * }</pre>
 *
 * This annotation can be used to create elements that automatically load all the properties in a group
 * into a single object. To load the property group from the example above, you would create a class like this:
 *
 * <pre>{@code
 * @Element
 * @PropertyGroup(name = "my.props")
 * public class MyProps {
 *     private int a;
 *     private int b;
 * }
 * }</pre>
 *
 * The properties of the group will be the fields of the class, mapped by name. The fields can have any
 * visibility. Property conversions will be applied. To provide different names for the fields,
 * you can use {@link Property}:
 *
 * <pre>{@code
 * @Element
 * @PropertyGroup(name = "my.props")
 * public class MyProps {
 *     @Property("a") private int aValue;
 *     @Property("b") private int bValue;
 * }
 * }</pre>
 *
 * The {@code MyProps} element can then be injected into any other element.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyGroup {

    /**
     * The name of the property group. This is the common prefix of the properties.
     */
    String name();
}
