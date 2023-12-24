/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import java.util.List;

/**
 * A {@link ClassScanner} implementation that does not actually perform any scanning,
 * but rather returns the classes provided at construction time.
 */
public class FixedClassScanner implements ClassScanner {

	private final List<Class<?>> classes;

	/**
	 * Create a scanner that will return the provided classes on scan.
	 */
	public FixedClassScanner(List<Class<?>> classes) {
		this.classes = classes;
	}

	@Override
	public List<Class<?>> scanClasses() {
		return List.copyOf(classes);
	}
}
