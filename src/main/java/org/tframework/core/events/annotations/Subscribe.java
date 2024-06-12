/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method of an element as a subscriber to a topic. This method must fulfill the following requirements:
 * <ul>
 *     <li>It must be public.</li>
 *     <li>It must be non-static.</li>
 *     <li>It must have a single parameter of any type.</li>
 * </ul>
 * In order to avoid confusing behavior, it is recommended for any method not to subscribe
 * to more than one topic. If a method subscribes to more than one topic, it will receive events from all of them,
 * but it will not be possible to determine which topic the event was published to.
 * For example, the following method is a valid subscriber:
 * <pre>{@code
 * @Element
 * public class MyElement {
 *
 *    @Subscribe("my-topic")
 *    public void onMyTopic(Object payload) {
 *    // Do something with the payload
 *    }
 * }
 * }</pre>
 * The parameter of the method will be the payload of the event that is published to the topic. The type of the parameter
 * can be any type, as long as the payload of the event is of the same type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    /**
     * The topic to subscribe to.
     */
    String value();

}
