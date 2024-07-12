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
 * This annotation can be used to filter elements based on the existence of other elements.
 * You can specify the name of the required element or its type. The annotated element will
 * only be kept, if another element is found that matches the specified name or type.
 *
 * <pre>{@code
 * @Element
 * @RequiredElement(name = Baz.class)
 * @RequiredElement(name = "foo")
 * public class Bar {}
 * }</pre>
 *
 * {@code Bar} will only be kept if an element with the name {@code foo} and another one
 * with type {@code Baz} is found. As the example shows, this annotation can be used multiple times,
 * and they will be combined with a logical AND.
 * <p><br>
 * Note that this filter will be the last one applied. It will take into account previously filtered
 * elements. In the example above, if the element {@code foo} is filtered out, {@code Bar} will also
 * be removed.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatedRequiredElement.class)
public @interface RequiredElement {

    String NAME_NOT_PROVIDED = "";
    Class<?> TYPE_NOT_PROVIDED = Void.class;

    /**
     * The name of the required element. This is mutually exclusive with {@link #type()}.
     */
    String name() default NAME_NOT_PROVIDED;

    /**
     * The type of the required element. This is mutually exclusive with {@link #name()}.
     */
    Class<?> type() default Void.class; // will not accept TYPE_NOT_PROVIDED constant...

}
