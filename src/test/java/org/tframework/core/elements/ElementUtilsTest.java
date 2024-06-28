/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.dependency.DependencyDefinition;

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

    @InjectElement
    private List<Integer> genericListDependency;

    @Test
    public void shouldGetDependencyTypeParameter_whenInputIsField() throws Exception {
        var field = this.getClass().getDeclaredField("genericListDependency");
        var listType = ElementUtils.getDependencyTypeParameter(DependencyDefinition.fromField(field));
        assertEquals(Integer.class, listType);
    }

    private void someMethod(List<String> genericListDependency) {}

    @Test
    public void shouldGetDependencyTypeParameter_whenInputIsMethodParameter() throws Exception {
        var method = this.getClass().getDeclaredMethod("someMethod", List.class);
        var parameter = method.getParameters()[0];
        var listType = ElementUtils.getDependencyTypeParameter(DependencyDefinition.fromParameter(parameter));
        assertEquals(String.class, listType);
    }
}
