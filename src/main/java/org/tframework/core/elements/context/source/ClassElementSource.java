/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.source;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.tframework.core.utils.LogUtils;

/**
 * An {@link ElementSource} for elements that are defined by a class (the class is annotated
 * by {@link org.tframework.core.elements.annotations.Element}).
 * @param constructor The constructor of the source class that will be used to create the element instance(s).
 *                    It must not be null.
 */
public record ClassElementSource(
        @NonNull Constructor<?> constructor
) implements ElementSource {

    @Override
    public List<Parameter> elementConstructionParameters() {
        return Arrays.asList(constructor.getParameters());
    }

    @Override
    public String toString() {
        return "ClassElementSource{constructor=" + LogUtils.niceExecutableName(constructor) + "}";
    }
}
