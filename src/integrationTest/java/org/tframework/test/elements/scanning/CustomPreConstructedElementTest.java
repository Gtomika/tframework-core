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
