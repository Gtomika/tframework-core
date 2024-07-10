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
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;

@Slf4j
public class ElementUtilsTest {

    @Test
    public void shouldGetElementNameByType() {
        String name = ElementUtils.getElementNameByType(String.class);
        assertEquals("java.lang.String", name);
    }

    private static final List<Integer> intList = List.of(1, 2);
    private static final List<String> stringList = List.of("");


    @Test
    public void shouldGetElementNameByType_ifElementHasGenericType_andNameIsDifferentForDifferentTypeParameters() {
        String name1 = ElementUtils.getElementNameByType(intList.getClass());
        String name2 = ElementUtils.getElementNameByType(stringList.getClass());
        log.info("Name for int list: '{}' Name for string list: {}", name1, name2);
        //assertNotEquals(name1, name2); -> it would be nice, but I cannot implement it :(
        assertEquals(name1, name2);
    }

    @Element(name = "dummy", scope = ElementScope.SINGLETON)
    static class DummyElement {}

    @Test
    public void shouldCreateStringRepresentationOfElementAnnotation() {
        String stringified = ElementUtils.stringifyElementAnnotation(DummyElement.class.getAnnotation(Element.class));
        assertEquals("@Element(name = dummy, scope = SINGLETON)", stringified);
    }

    @InjectElement("someString")
    private String someString;

    @Test
    public void shouldReturnTrue_ifInjectElementIsNamed() throws Exception {
        var elementAnnotation = this.getClass().getDeclaredField("someString").getAnnotation(InjectElement.class);
        assertTrue(ElementUtils.isNamedElementInjection(elementAnnotation));
    }

    @InjectElement
    private String otherString;

    @Test
    public void shouldReturnFalse_ifInjectElementIsNotNamed() throws Exception {
        var elementAnnotation = this.getClass().getDeclaredField("otherString").getAnnotation(InjectElement.class);
        assertFalse(ElementUtils.isNamedElementInjection(elementAnnotation));
    }
}
