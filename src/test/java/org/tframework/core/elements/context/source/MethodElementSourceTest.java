/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.source;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.context.ElementContext;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MethodElementSourceTest {

    @Mock
    private ElementContext parentElementContext;

//    private static final ElementContext TEST_CONTEXT = new SingletonElementContext(
//            "test", String.class, new ClassElementSource(String.class.getConstructors()[0]));

    public String testMethod(Integer x, File y) {
        return "testMethod";
    }

    @Test
    void shouldReturnElementConstructionParameters() throws Exception {
        var method = this.getClass().getMethod("testMethod", Integer.class, File.class);
        var source = new MethodElementSource(method, parentElementContext);

        var parameters = source.elementConstructionParameters();
        assertEquals(2, parameters.size());
        assertEquals(Integer.class, parameters.get(0).getType());
        assertEquals(File.class, parameters.get(1).getType());
    }
}
