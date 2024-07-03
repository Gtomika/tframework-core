/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.dependency;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class SpecialCollectionsDependenciesTest {

    @Priority(2)
    @Element(name = "element-a")
    public String provideElementA() {
        return "a";
    }

    @Priority(1)
    @Element(name = "element-b")
    public String provideElementB() {
        return "b";
    }

    @InjectElement
    private List<String> stringElementsList;

    @InjectElement
    private String[] stringElementsArray;

    @InjectElement
    private Map<String, String> stringElementsMap;

    @Test
    public void shouldInjectCollectionSpecialDependencies() {
        assertEquals(List.of("a", "b"), stringElementsList);
        assertArrayEquals(new String[] {"a", "b"}, stringElementsArray);
        assertEquals(Map.of(
                "element-a", "a",
                "element-b", "b"
        ), stringElementsMap);
    }
}
