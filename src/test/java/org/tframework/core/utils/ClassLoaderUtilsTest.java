/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tframework.core.utils.ResourceTestUtils.TEST_RESOURCE_CONTENT;
import static org.tframework.core.utils.ResourceTestUtils.TEST_RESOURCE_NAME;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tframework.core.TFramework;
import org.tframework.core.readers.ResourceNotFoundException;

class ClassLoaderUtilsTest {

    @Test
    public void shouldLoadResourceAsString_whenPresentInResourcesFolder() {
        String actualContent = ClassLoaderUtils.getResourceAsString(TEST_RESOURCE_NAME, this.getClass());
        assertEquals(TEST_RESOURCE_CONTENT, actualContent);
    }

    @Test
    public void shouldThrowResourceNotFoundException_whenResourceNotPresent() {
        String nonExistingResource = "non_existing.txt";
        var exception = assertThrows(ResourceNotFoundException.class, () -> {
            ClassLoaderUtils.getResourceAsString(nonExistingResource, this.getClass());
        });

        assertEquals(
                exception.getMessageTemplate().formatted(nonExistingResource),
                exception.getMessage()
        );
    }

    @Test
    public void shouldLoadResourceAsStream_whenPresentInResourcesFolder() throws IOException {
        try(InputStream is = ClassLoaderUtils.getResourceAsStream(TEST_RESOURCE_NAME, this.getClass())) {
            assertNotNull(is);
        }
    }

    @ParameterizedTest
    @ValueSource(classes = {String.class, ClassLoaderUtils.class, TFramework.class})
    public void shouldLoadClass(Class<?> classToLoad) throws ClassNotFoundException {
        var actualClass = ClassLoaderUtils.loadClass(classToLoad.getName(), this.getClass());
        assertEquals(classToLoad, actualClass);
    }

    @Test
    public void shouldThrowClassNotFoundException_whenClassNotFound() {
        String madeUpClass = "some.cool.package.CoolClass";
        assertThrows(ClassNotFoundException.class, () -> {
            ClassLoaderUtils.loadClass(madeUpClass, this.getClass());
        });
    }

}
