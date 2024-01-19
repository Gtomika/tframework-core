/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.di.annotations.Element;
import org.tframework.core.di.context.source.ClassElementSource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementContextTestUtils {

    @Element
    public static class ParentElement {}

    public static final ElementContext PARENT_ELEMENT_CONTEXT = ElementContext.from(
            ParentElement.class.getAnnotation(Element.class),
            ParentElement.class,
            new ClassElementSource(ParentElement.class.getDeclaredConstructors()[0])
    );

}