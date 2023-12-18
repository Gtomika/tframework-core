package org.tframework.core.annotations;

import org.tframework.core.exceptions.TFrameworkException;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This exception is thrown when {@link ComposedAnnotationScanner} finds more than one
 * annotation in strict mode.
 */
public class MultipleAnnotationsScannedException extends TFrameworkException {

    public MultipleAnnotationsScannedException(List<? extends Annotation> scannedAnnotations) {
        super("At most 1 annotation was allowed, but found %d: %s".formatted(
                scannedAnnotations.size(), scannedAnnotations
        ));
    }

}
