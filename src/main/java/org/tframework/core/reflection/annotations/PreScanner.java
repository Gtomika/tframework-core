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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import org.tframework.core.utils.MultiValueMap;

/**
 * This class scans annotations from a given source, and stores them in a {@link PreScannedAnnotations}.
 * @param <T> The type of {@link AnnotatedElement} that the annotations were scanned from.
 */
public class PreScanner<T extends AnnotatedElement> {

    /**
     * Scans the annotations of a given source, and stores them in a {@link PreScannedAnnotations}.
     * @param source The source {@link AnnotatedElement} of the annotations, like a class or method.
     * @param scanner An {@link AnnotationScanner} to scan the annotations.
     */
    public PreScannedAnnotations<T> preScan(T source, AnnotationScanner scanner) {
        MultiValueMap<Class<? extends Annotation>, Annotation> annotations = new MultiValueMap<>();
        scanAnnotations(source, scanner, annotations);
        return new PreScannedAnnotations<>(source, annotations);
    }

    private void scanAnnotations(
            AnnotatedElement source,
            AnnotationScanner scanner,
            MultiValueMap<Class<? extends Annotation>, Annotation> annotations
    ) {
        //TODO
    }

    private List<? extends Annotation> safeScanAnnotationInstances(
            AnnotatedElement source,
            AnnotationScanner scanner,
            Class<? extends Annotation> annotationType
    ) {
        try {
            return scanner.scan(source, annotationType);
        } catch (Exception e) {
            return List.of();
        }
    }
}
