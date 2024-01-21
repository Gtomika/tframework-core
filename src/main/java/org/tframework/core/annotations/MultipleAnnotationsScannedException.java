/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import org.tframework.core.TFrameworkException;

/**
 * This exception is thrown when {@link AnnotationScanner} finds more than one
 * annotation in strict mode.
 */
public class MultipleAnnotationsScannedException extends TFrameworkException {

    private static final String TEMPLATE = "At most 1 annotation was allowed, but found %d: %s";

    public MultipleAnnotationsScannedException(List<? extends Annotation> scannedAnnotations) {
        super(TEMPLATE.formatted(scannedAnnotations.size(), scannedAnnotations));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }

}
