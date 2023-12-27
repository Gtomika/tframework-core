/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class AnnotationMatchersFactoryTest {

    @Test
    public void shouldCreateExtendedAnnotationMatcher() {
        var matcher = AnnotationMatchersFactory.createExtendedAnnotationMatcher();
        assertNotNull(matcher);
    }

}
