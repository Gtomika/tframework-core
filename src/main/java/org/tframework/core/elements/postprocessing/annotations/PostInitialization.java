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
package org.tframework.core.elements.postprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be placed on element's methods, to execute them after the element is fully
 * initialized (all dependencies are injected). For a method to be eligible, it needs to fulfil these requirements:
 * <ul>
 *     <li>Must be public.</li>
 *     <li>Must not be static, because the element instance is used to invoke it.</li>
 *     <li>Must not be abstract, needs a body to be invoked.</li>
 *     <li>Must not have any parameters, because it would be unclear where to get values for them.</li>
 *     <li>It may have a return type, but that will be ignored.</li>
 * </ul>
 * Multiple methods may be annotated inside a single element. In this case, all of them will be executed
 * in the order the framework finds them (this order cannot be controlled).
 *
 * <pre>{@code
 * @Element
 * public class MyElement {
 *
 *     @InjectElement
 *     private MyDependency myDependency;
 *
 *     public MyElement() {
 *         //myDependency is not yet field injected here!
 *         //myDependency.doStuff(); -> null pointer exception
 *     }
 *
 *     @PostInitialization
 *     public void init() {
 *         //element is initialized here
 *         myDependency.doStuff();
 *     }
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostInitialization {
}
