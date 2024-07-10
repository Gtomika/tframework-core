/*
Copyright 2023 Tamas Gaspar

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    public static final String MADE_UP_CLASS = "some.cool.package.CoolClass";

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
        assertThrows(ClassNotFoundException.class, () -> {
            ClassLoaderUtils.loadClass(MADE_UP_CLASS, this.getClass());
        });
    }

    @Test
    public void shouldReturnTrue_whenClassIsAvailable() {
        assertTrue(ClassLoaderUtils.isClassAvailable(TFramework.class.getName(), this.getClass()));
    }

    @Test
    public void shouldReturnFalse_whenClassIsNotAvailable() {
        assertFalse(ClassLoaderUtils.isClassAvailable(MADE_UP_CLASS, this.getClass()));
    }

}
