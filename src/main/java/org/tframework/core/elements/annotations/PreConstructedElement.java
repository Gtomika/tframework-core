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
import java.lang.annotation.Target;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;

/**
 * This is a marker annotation for those classes that are pre-constructed by the framework, yet they
 * will be added to the {@link ElementsContainer} the same way as normal elements are, and can be used as dependencies.
 * <p><br>
 * Usually, the instances of the elements are created by the responsible {@link ElementContext},
 * but in case of these special elements, they are created beforehand.
 * <p><br>
 * Because of the special nature in which these elements are constructed, injecting dependencies at construction time is not
 * possible (the framework is not responsible for creating the instance). However, field injection is still possible.
 */
@Documented
@Target(ElementType.TYPE)
public @interface PreConstructedElement {
}
