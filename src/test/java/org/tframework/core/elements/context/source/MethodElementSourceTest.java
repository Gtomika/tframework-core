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
package org.tframework.core.elements.context.source;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.context.ElementContext;

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
