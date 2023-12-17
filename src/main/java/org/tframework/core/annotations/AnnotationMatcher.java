package org.tframework.core.annotations;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The annotation matcher implements a sort of 'extended equality' check between annotations, called annotation matching.
 * See {@link AnnotationMatcher#matches(Annotation)} for the detailed matching rules.
 * @param <A> Type of the expected annotation that will be matched against other annotations.
 * @see AnnotationMatchingResult
 */
@Slf4j
public class AnnotationMatcher<A extends Annotation> {

    private final Class<A> expectedAnnotationClass;
    private final Optional<Class<? extends Annotation>> repeatableVersion;

    /**
     * Creates an annotation matcher for a specific annotation.
     * @param expectedAnnotationClass The {@link Class} of the expected annotation, to match against.
     */
    public AnnotationMatcher(Class<A> expectedAnnotationClass) {
        this.expectedAnnotationClass = expectedAnnotationClass;

        if(expectedAnnotationClass.isAnnotationPresent(Repeatable.class)) {
            Repeatable repeatable = expectedAnnotationClass.getAnnotation(Repeatable.class);
            repeatableVersion = Optional.of(repeatable.value());
        } else {
            repeatableVersion = Optional.empty();
        }
    }

    /**
     * Performs annotation matching between the expected annotation, specified at construction time, and the one
     * provided as a parameter. Annotations will match if:
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
     * var matcher = new AnnotationMatcher<>(TestAnnotationA.class);
     * TestAnnotationA annotationA = ... //obtain instance of this annotation
     * var result = matcher.matches(annotationA);
     * assertTrue(result.matches());
     * }</pre>
     * If {@code @TestAnnotationA} is repeatable with container {@code @RepeatedTestAnnotationA}, that can also be matched:
     * <pre>{@code
     * var matcher = new AnnotationMatcher<>(TestAnnotationA.class);
     * RepeatedTestAnnotationA repeatedAnnotationA = ... //obtain instance of this annotation
     * var result = matcher.matches(repeatedAnnotationA);
     * assertTrue(result.matches());
     * }</pre>
     * @param annotationToCheck The annotation to match against.
     * @return An {@link AnnotationMatchingResult} with the results.
     */
    public AnnotationMatchingResult<A> matches(Annotation annotationToCheck) {
        //direct equality
        if(expectedAnnotationClass.equals(annotationToCheck.annotationType())) {
            //this case is safe because we just checked that their types are equal
            return new AnnotationMatchingResult<>(true, List.of((A) annotationToCheck));
        }

        //dealing with @Repeatable: have to check if 'annotationToCheck' is a repeated version of the expected annotation
        if(repeatableVersion.isPresent() && repeatableVersion.get().equals(annotationToCheck.annotationType())) {
            return new AnnotationMatchingResult<>(true, extractRepeatedAnnotations(annotationToCheck));
        }

        return new AnnotationMatchingResult<>(false, List.of());
    }

    private List<A> extractRepeatedAnnotations(Annotation annotationToCheck) {
        //we know that 'annotationToCheck' is containing annotation for @Repeatable 'expectedAnnotationClass'
        //this means that it is guaranteed to have a field 'value'
        //the 'value' field is going to contain the repeated annotation with type 'expectedAnnotationClass'.

        try {
            //if 'annotationToCheck' is indeed a @Repeatable container for 'expectedAnnotationClass', this invocation and cast are safe
            A[] containedAnnotations = (A[]) annotationToCheck.annotationType().getMethod("value").invoke(annotationToCheck);
            return Arrays.asList(containedAnnotations);
        } catch (Exception e) {
            log.error("Failed to get contained annotations from '{}'. Double check that this annotation is " +
                    "a container for @Repeatable!", annotationToCheck.annotationType(), e);
            return List.of();
        }
    }
}
