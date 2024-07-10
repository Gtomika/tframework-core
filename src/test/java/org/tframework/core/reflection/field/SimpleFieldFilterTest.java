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
package org.tframework.core.reflection.field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;

public class SimpleFieldFilterTest {

    private final AnnotationScanner annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
    private final SimpleFieldFilter filter = new SimpleFieldFilter();

    @Test
    public void shouldFilterByStatic() throws Exception {
        assertFalse(filter.isStatic(TestClass.class.getDeclaredField("field1")));
        assertTrue(filter.isStatic(TestClass.class.getDeclaredField("field2")));
    }

    @Test
    public void shouldFilterByFinal() throws Exception {
        assertTrue(filter.isFinal(TestClass.class.getDeclaredField("field1")));
        assertFalse(filter.isFinal(TestClass.class.getDeclaredField("field2")));
    }

    static class TestClass {

        public final String field1 = "value1";

        public static String field2;

    }

}
