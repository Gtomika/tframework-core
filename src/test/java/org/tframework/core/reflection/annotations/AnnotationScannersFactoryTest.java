/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.annotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class AnnotationScannersFactoryTest {

    @Test
    public void shouldCreateComposedAnnotationScanner_withExtendedMatcher() {
        var scanner = AnnotationScannersFactory.createComposedAnnotationScanner();
        assertNotNull(scanner);
        assertEquals(ExtendedAnnotationMatcher.class, scanner.getAnnotationMatcher().getClass());
    }

}
