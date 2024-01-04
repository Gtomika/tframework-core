/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.converters;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * The factory to {@link PropertyConverter}s by type.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyConvertersFactory {

    private static final List<PropertyConverter<?>> propertyConverters = new ArrayList<>();

    static {
        propertyConverters.addAll(createDefaultPropertyConverters());
    }

    private static List<PropertyConverter<?>> createDefaultPropertyConverters() {
        return List.of(
                new BooleanPropertyConverter(),
                new StringPropertyConverter(),
                new ListPropertyConverter()
        );
    }

    /**
     * Get the {@link PropertyConverter} for the given type.
     * @param type The type to get the converter for, cannot be null.
     * @param <T> Type that the returned converter produces.
     * @throws PropertyConverterNotFoundException If no converter for the given type was found.
     */
    @SuppressWarnings("unchecked")
    public static <T> PropertyConverter<T> getConverter(@NonNull Class<T> type) {
        return (PropertyConverter<T>) propertyConverters.stream()
                .filter(propertyConverter -> propertyConverter.getType().equals(type))
                .findAny()
                .orElseThrow(() -> new PropertyConverterNotFoundException(type));
    }

}
