/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

/**
 * A property value is either a single value, or a collection of values.
 * @see SinglePropertyValue
 * @see ListPropertyValue
 */
public sealed interface PropertyValue permits SinglePropertyValue, ListPropertyValue {
}
