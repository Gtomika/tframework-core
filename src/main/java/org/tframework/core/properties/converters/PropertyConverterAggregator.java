/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.converters;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.utils.PrimitivesUtils;

/**
 * Combines several {@link PropertyConverter}s to find which one to use and apply
 * when a property needs to be converted to a specific type. The process is as follows:
 * <ol>
 *     <li>
 *         Check if there is a {@link PropertyConverter} where {@link PropertyConverter#getType()}
 *         is exactly the desired type. If yes, this will be used.
 *     </li>
 *     <li>
 *          If there is no exact match, check if there is a {@link PropertyConverter} where {@link PropertyConverter#getType()}
 *          is assignable to the given property type. If yes, this will be used.
 *     </li>
 *     <li>
 *         Multiple assignable converters will result in the first one found being used.
 *     </li>
 *     <li>
 *         If there is no exact or assignable match, a {@link PropertyConverterNotFoundException} is raised.
 *     </li>
 * </ol>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyConverterAggregator {

    private final List<PropertyConverter<?>> converters;

    /**
     * Convert the {@link PropertyValue} to the required type. See the class documentation for the rules.
     * @param propertyValue {@link PropertyValue} to convert.
     * @param requiredType Type to convert to.
     * @return The converted property.
     * @throws PropertyConverterNotFoundException If there was no matching {@link PropertyConverter}.
     */
    @SuppressWarnings("unchecked")
    public <T> T convert(PropertyValue propertyValue, Class<T> requiredType) throws PropertyConverterNotFoundException {
        var nonPrimitiveType = PrimitivesUtils.toWrapper(requiredType);
        PropertyConverter<?> converterToUse = findConverterWithExactType(nonPrimitiveType)
                .orElse(findConverterWithAssignableType(nonPrimitiveType)
                        .orElseThrow(() -> new PropertyConverterNotFoundException(requiredType)));
        return (T) converterToUse.convert(propertyValue);
    }

    private Optional<PropertyConverter<?>> findConverterWithExactType(Class<?> requiredType) {
        return converters.stream()
                .filter(converter -> converter.getType().equals(requiredType))
                .findFirst();
    }

    private Optional<PropertyConverter<?>> findConverterWithAssignableType(Class<?> requiredType) {
        return converters.stream()
                .filter(converter -> requiredType.isAssignableFrom(converter.getType()))
                .findFirst();
    }

    /**
     * Creates a new aggregator that will use the provided {@link PropertyConverter}s.
     */
    public static PropertyConverterAggregator usingConverters(@NonNull List<PropertyConverter<?>> converters) {
        return new PropertyConverterAggregator(converters);
    }

}
