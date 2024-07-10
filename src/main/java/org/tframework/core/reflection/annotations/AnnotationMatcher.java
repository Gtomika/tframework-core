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
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Annotation;

/**
 * Performs matching of different annotations. Annotation matching can be considered as an extension
 * over simple annotation type equality.
 * @see AnnotationMatchingResult
 * @see AnnotationMatchersFactory
 */
public interface AnnotationMatcher {

    /**
     * Performs the annotation matching: determines if the {@code annotationToMatch} can be matched
     * to the type {@code expectedAnnotationClass}.
     * @param annotationToMatch The annotation to match against the {@code expectedAnnotationClass}.
     * @return An {@link AnnotationMatchingResult} with the match status.
     */
    <A extends Annotation> AnnotationMatchingResult<A> matches(Class<A> expectedAnnotationClass, Annotation annotationToMatch);

}
