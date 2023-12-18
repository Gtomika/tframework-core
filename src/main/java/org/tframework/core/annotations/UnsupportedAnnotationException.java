package org.tframework.core.annotations;

import org.tframework.core.exceptions.TFrameworkException;

import java.lang.annotation.Annotation;

/**
 * This exception is thrown when the composed annotation scanning encounters
 * an unsupported annotation that it cannot scan for.
 * @see ComposedAnnotationScanner
 */
public class UnsupportedAnnotationException extends TFrameworkException {

    public static final String TEMPLATE = "Composed annotation scanning is not supported for annotation '%s'";

    public UnsupportedAnnotationException(Class<? extends Annotation> annotationClass) {
        super(TEMPLATE.formatted(annotationClass.getName()));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
