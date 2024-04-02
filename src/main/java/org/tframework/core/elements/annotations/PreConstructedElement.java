/* Licensed under Apache-2.0 2024. */
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
