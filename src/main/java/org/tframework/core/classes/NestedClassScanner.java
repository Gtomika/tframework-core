/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import java.util.Arrays;
import java.util.List;

/**
 * This {@link ClassScanner} implementation scans all nested classes of a given class.
 * The class itself will <b>not</b> be included.
 */
public class NestedClassScanner implements ClassScanner {

	private final Class<?> classToScan;

	/**
	 * Create a scanner that will scan for the nested classes inside {@code classToScan}.
	 */
	public NestedClassScanner(Class<?> classToScan) {
		this.classToScan = classToScan;
	}

	/**
	 * Use reflection to detect and return the nested classes of the one provided at construction time.
	 */
	@Override
	public List<Class<?>> scanClasses() {
		return Arrays.stream(classToScan.getNestMembers())
				.filter(clazz -> !clazz.equals(classToScan))
				.toList();
	}
}
