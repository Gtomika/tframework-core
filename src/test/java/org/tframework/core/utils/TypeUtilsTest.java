/* Licensed under Apache-2.0 2024. */
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
