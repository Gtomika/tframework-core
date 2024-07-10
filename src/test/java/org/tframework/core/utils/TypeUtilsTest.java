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
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.dependency.DependencyDefinition;

public class TypeUtilsTest {

    @InjectElement
    private List<Integer> genericListDependency;

    @Test
    public void shouldGetDependencyTypeParameter_whenInputIsField() throws Exception {
        var field = this.getClass().getDeclaredField("genericListDependency");
        var listType = TypeUtils.getTypeParameter(DependencyDefinition.fromField(field));
        assertEquals(Integer.class, listType);
    }

    private void someMethod(List<String> genericListDependency) {}

    @Test
    public void shouldGetDependencyTypeParameter_whenInputIsMethodParameter() throws Exception {
        var method = this.getClass().getDeclaredMethod("someMethod", List.class);
        var parameter = method.getParameters()[0];
        var listType = TypeUtils.getTypeParameter(DependencyDefinition.fromParameter(parameter));
        assertEquals(String.class, listType);
    }

}
