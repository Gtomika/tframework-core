/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import java.lang.annotation.Annotation;
import java.util.List;
import org.tframework.core.annotations.AnnotationScanner;

/**
 * A reasonable default implementation for {@link ClassFilter}.
 */
public class DefaultClassFilter implements ClassFilter {

	@Override
	public List<Class<?>> filterByAnnotation(
			List<Class<?>> classes,
			Class<? extends Annotation> annotationClass,
			AnnotationScanner annotationScanner
	) {
		return classes.stream()
				.filter(clazz -> annotationScanner.hasAnnotation(clazz, annotationClass))
				.toList();
	}

	@Override
	public List<Class<?>> filterByInterface(List<Class<?>> classes, Class<?> interfaceClass) {
		checkIfInterface(interfaceClass);
		return classes.stream()
				.filter(interfaceClass::isAssignableFrom)
				.toList();
	}
}
