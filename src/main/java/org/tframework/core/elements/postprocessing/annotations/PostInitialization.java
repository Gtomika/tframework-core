/* Licensed under Apache-2.0 2024. */
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
