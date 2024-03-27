/* Licensed under Apache-2.0 2024. */
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
