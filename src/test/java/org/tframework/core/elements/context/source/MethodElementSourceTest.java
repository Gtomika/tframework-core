/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.source;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.SingletonElementContext;

class MethodElementSourceTest {

    private static final ElementContext TEST_CONTEXT = new SingletonElementContext(
            "test", String.class, new ClassElementSource(String.class.getConstructors()[0]));

    public String testMethod(Integer x, File y) {
        return "testMethod";
    }

    @Test
    void shouldReturnElementConstructionParameters() throws Exception {
        var method = this.getClass().getMethod("testMethod", Integer.class, File.class);
        var source = new MethodElementSource(method, TEST_CONTEXT);

        var parameters = source.elementConstructionParameters();
        assertEquals(2, parameters.size());
        assertEquals(Integer.class, parameters.get(0).getType());
        assertEquals(File.class, parameters.get(1).getType());
    }
}
