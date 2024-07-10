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
import java.util.List;

/**
 * Contains the result of an annotation matching: {@link AnnotationMatcher#matches(Class, Annotation)}.
 * @param matches If there was a match.
 * @param matchedAnnotations All matched annotations. If {@code matches} is false, this list will be always empty.
 * @param <A> Type of the matched annotation.
 */
public record AnnotationMatchingResult<A extends Annotation>(
    boolean matches,
    List<A> matchedAnnotations
) {
}
