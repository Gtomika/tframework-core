/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.annotations;

import org.tframework.core.TFrameworkException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

/**
 * This exception is thrown when {@link AnnotationScanner} finds more than one
 * annotation in strict mode.
 */
public class MultipleAnnotationsScannedException extends TFrameworkException {

    private static final String TEMPLATE = "At most 1 annotation was allowed on component '%s', but found %d: %s";

    public MultipleAnnotationsScannedException(
            AnnotatedElement annotatedElement,
            List<? extends Annotation> scannedAnnotations
    ) {
        super(TEMPLATE.formatted(annotatedElement, scannedAnnotations.size(), scannedAnnotations));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }

}
