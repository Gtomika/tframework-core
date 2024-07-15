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
package org.tframework.core.elements.postprocessing;

import org.tframework.core.Application;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;

/**
 * Performs post-processing of a newly created element instance. This may include modifications
 * to the instance, or simply invoking certain methods of it. Notes:
 * <ul>
 *     <li>
 *         You can define your own custom post processors as {@link Element}s.
 *         These will be applied, as well as the built-in ones.
 *     </li>
 *     <li>
 *         You can use {@link Priority} to specify the order of execution of multiple post processors.
 *         It is recommended not to use very high or low values, as they may match with built-in processors
 *         and cause unexpected behavior.
 *     </li>
 * </ul>
 * In case of defining custom post processors, make sure they are stateless, as they will be shared
 * between all element instances. Also note that since post-processor elements and any dependencies
 * they require are eagerly initialized, <b>they will not be post-processed</b> by themselves. So make sure they do not
 * require any post-processing themselves, such as field injection or method invocation.
 */
public interface ElementInstancePostProcessor {

    /**
     * Performs the post-processing.
     * @param application The {@link Application} which can be used to access anything like
     *                    profiles, properties or other elements.
     * @param elementContext The {@link ElementContext} to which the instance belongs.
     * @param instance The newly created instance. It may be modified during this process.
     */
    void postProcessInstance(Application application, ElementContext elementContext, Object instance);

}
