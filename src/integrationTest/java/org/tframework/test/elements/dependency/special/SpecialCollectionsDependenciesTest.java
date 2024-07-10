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
package org.tframework.test.elements.dependency.special;

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
