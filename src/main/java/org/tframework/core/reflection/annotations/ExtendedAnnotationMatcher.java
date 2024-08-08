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
import java.lang.annotation.Repeatable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;

/**
 * The annotation matcher implements a sort of 'extended equality' check between annotations, including
 * special cases such as the {@link Repeatable} annotation.
 * See {@link ExtendedAnnotationMatcher#matches(Class, Annotation)} for the detailed matching rules.
 * @see AnnotationMatchingResult
 */
@Slf4j
@Element
@RequiredArgsConstructor
public class ExtendedAnnotationMatcher implements AnnotationMatcher {

    private final RepeatedAnnotationHandler repeatedAnnotationHandler;

    /**
     * Performs annotation matching between the expected annotation, and the provided one.
     * Annotations will match if:
     * <ul>
     *     <li>
     *         The expected and the provided annotations have the same type.
     *         The provided annotation will be returned in the result.
     *      </li>
     *     <li>
     *         The provided annotation is a repeatable version of the expected one.
     *         The repetitions will be returned in the result.
     *     </li>
     * </ul>
     * Example matching with expected annotation {@code @TestAnnotationA}:
     * <pre>{@code
     * var matcher = new AnnotationMatcher();
     * TestAnnotationA annotationA = ... //obtain instance of this annotation
     * var result = matcher.matches(TestAnnotationA.class, annotationA);
     * assertTrue(result.matches());
     * }</pre>
     * If {@code @TestAnnotationA} is repeatable with container {@code @RepeatedTestAnnotationA}, that can also be matched:
     * <pre>{@code
     * var matcher = new AnnotationMatcher();
     * RepeatedTestAnnotationA repeatedAnnotationA = ... //obtain instance of this annotation
     * var result = matcher.matches(TestAnnotationA.class, repeatedAnnotationA);
     * assertTrue(result.matches());
     * }</pre>
     * @param expectedAnnotationClass The class that should be matched (expected annotation type).
     * @param annotationToMatch The annotation to match against.
     * @return An {@link AnnotationMatchingResult} with the results.
     */
    @SuppressWarnings("unchecked")
    public <A extends Annotation> AnnotationMatchingResult<A> matches(
            Class<A> expectedAnnotationClass,
            Annotation annotationToMatch
    ) {
        //direct equality
        if(expectedAnnotationClass.equals(annotationToMatch.annotationType())) {
            //this case is safe because we just checked that their types are equal
            return new AnnotationMatchingResult<>(true, List.of((A) annotationToMatch));
        }

        //dealing with @Repeatable: have to check if 'annotationToMatch' is a repeated version of the expected annotation
        var repeatableVersion = repeatedAnnotationHandler.extractRepeatableVersion(expectedAnnotationClass);
        if(repeatableVersion.isPresent() && repeatableVersion.get().equals(annotationToMatch.annotationType())) {
            var repeatedAnnotations = repeatedAnnotationHandler.extractRepeatedAnnotations(
                    annotationToMatch, expectedAnnotationClass
            );
            return new AnnotationMatchingResult<A>(true, repeatedAnnotations);
        }

        return new AnnotationMatchingResult<>(false, List.of());
    }
}
