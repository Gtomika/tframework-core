package org.tframework.core.annotations;

import org.tframework.core.exceptions.TFrameworkException;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This exception is thrown when {@link ComposedAnnotationScanner} finds more than one
 * annotation in strict mode.
 */
public class MultipleAnnotationsScannedException extends TFrameworkException {

    public static final String TEMPLATE = "At most 1 annotation was allowed, but found %d: %s";

    public MultipleAnnotationsScannedException(List<? extends Annotation> scannedAnnotations) {
        super(TEMPLATE.formatted(scannedAnnotations.size(), scannedAnnotations));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }

}
