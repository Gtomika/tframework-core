/* Licensed under Apache-2.0 2022. */
package org.tframework.core.properties.annotations;

/**
 * Indicates that the type is a {@link org.tframework.core.properties.matchers.PropertyMatcher}. Classes annotated
 * with this will be picked up by the {@link org.tframework.core.properties.PropertyScanner} and used as matchers.
 * <p>
 * A class annotated with this must conform with all requirements specified at
 * {@link org.tframework.core.properties.matchers.PropertyMatcher}.
 */
public @interface PropertyMatcherImplementation {}
