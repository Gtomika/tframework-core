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
package org.tframework.core.reflection.annotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;

@Element //just to have an annotation to test
public class PreScannedAnnotationsTest {

    private final Class<?> source = this.getClass();
    private final Element annotation = source.getAnnotation(Element.class);
    private PreScannedAnnotations preScannedAnnotations;

    @BeforeEach
    public void setUp() {
        preScannedAnnotations = PreScannedAnnotations.empty(source);
    }

    @Test
    public void shouldAddAnnotation() {
        assertFalse(preScannedAnnotations.hasAnnotation(Element.class));
        preScannedAnnotations.add(annotation);
        assertTrue(preScannedAnnotations.hasAnnotation(Element.class));
    }

    @Test
    public void shouldGetAnnotation() {
        preScannedAnnotations.add(annotation);
        preScannedAnnotations.add(annotation);
        assertEquals(2, preScannedAnnotations.getAnnotations(Element.class).size());
    }

    @Test
    public void shouldGetAnnotationAsEmptyList_whenNoAnnotationsStored() {
        assertEquals(0, preScannedAnnotations.getAnnotations(Element.class).size());
    }

    @Test
    public void shouldGetAnnotationStrict() {
        preScannedAnnotations.add(annotation);

        var result = preScannedAnnotations.getAnnotationStrict(Element.class);
        assertTrue(result.isPresent());
        assertEquals(annotation, result.get());
    }

    @Test
    public void shouldGetEmptyOptional_whenNoAnnotationsStored() {
        assertFalse(preScannedAnnotations.getAnnotationStrict(Element.class).isPresent());
    }

    @Test
    public void shouldThrowException_whenMultipleAnnotationsFound() {
        preScannedAnnotations.add(annotation);
        preScannedAnnotations.add(annotation);
        assertThrows(MultipleAnnotationsScannedException.class, () -> preScannedAnnotations.getAnnotationStrict(Element.class));
    }
}
