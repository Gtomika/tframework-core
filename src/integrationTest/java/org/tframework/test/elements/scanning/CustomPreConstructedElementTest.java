/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.scanning;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.tframework.core.TFrameworkRootClass;
import org.tframework.core.elements.PreConstructedElementData;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.test.junit5.TFrameworkExtension;

@TFrameworkRootClass
public class CustomPreConstructedElementTest {

    private static final Object CUSTOM_PRE_CONSTRUCTED_ELEMENT = new Object();

    @RegisterExtension
    public static TFrameworkExtension tFrameworkExtension = TFrameworkExtension.isolatedTFrameworkTest()
            .preConstructedElements(Set.of(
                    PreConstructedElementData.from(CUSTOM_PRE_CONSTRUCTED_ELEMENT)
            ))
            .build()
            .toJunit5Extension();

    @Test
    public void shouldRegisterCustomPreConstructedElement(@InjectElement Object customPreConstructedElement) {
        assertSame(CUSTOM_PRE_CONSTRUCTED_ELEMENT, customPreConstructedElement);
    }
}
