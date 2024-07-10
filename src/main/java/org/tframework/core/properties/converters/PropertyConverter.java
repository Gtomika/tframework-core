/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.properties.converters;

import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.PropertyValue;

/**
 * The property converter transforms the {@link PropertyValue} into the supported type. Notes:
 * <ul>
 *     <li>
 *         You can define your own converts to support any types, by implementing this interface, and marking
 *         your converter an an {@link Element}.
 *     </li>
 *     <li>
 *         Due to the early initialization of these converter elements, filtering or post-processing
 *         and of course property conversion for them is not available. It is recommended to keep these converters
 *         simple, stateless and without dependencies.
 *     </li>
 *     <li>
 *         In your custom implementation, in case of an error, you can throw {@link PropertyConversionException} directly,
 *         or you can throw any other exception, which will be wrapped in a {@link PropertyConversionException}.
 *     </li>
 * </ul>
 * For examples, you can see {@link BooleanPropertyConverter} or {@link IntegerPropertyConverter}.
 * @param <T> The type this converter produces.
 */
public interface PropertyConverter<T> {

    /**
     * Convert the given {@link PropertyValue} into the supported type.
     * @param propertyValue The property value to convert.
     * @return The converted value.
     * @throws PropertyConversionException If the conversion failed.
     */
    T convert(PropertyValue propertyValue);

    /**
     * @return The type this converter produces. If this converter should support primitive types as well,
     * the <b>wrapper</b> class should be returned here.
     */
    Class<T> getType();

}
