/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import java.lang.annotation.Annotation;
import org.tframework.core.TFrameworkException;

/**
 * This exception is thrown when the composed annotation scanning encounters
 * an unsupported annotation that it cannot scan for.
 * @see ComposedAnnotationScanner
 */
public class UnsupportedAnnotationException extends TFrameworkException {

	private static final String TEMPLATE = "Composed annotation scanning is not supported for annotation '%s'";

	public UnsupportedAnnotationException(Class<? extends Annotation> annotationClass) {
		super(TEMPLATE.formatted(annotationClass.getName()));
	}

	@Override
	public String getMessageTemplate() {
		return TEMPLATE;
	}
}
