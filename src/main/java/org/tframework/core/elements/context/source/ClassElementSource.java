/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.source;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.utils.LogUtils;

/**
 * An {@link ElementSource} for elements that are defined by a class (the class is annotated by {@link Element}).
 * @param elementClass The class that was marked as an element.
 * @param constructor The constructor of the source class that will be used to create the element instance(s).
 *                    It must not be null.
 */
public record ClassElementSource(
        @NonNull Class<?> elementClass,
        @NonNull Constructor<?> constructor
) implements ElementSource {

    @Override
    public AnnotatedElement annotatedSource() {
        return elementClass;
    }

    @Override
    public List<Parameter> elementConstructionParameters() {
        return Arrays.asList(constructor.getParameters());
    }

    @Override
    public String toString() {
        return "ClassElementSource{class=" + elementClass.getName() +
                ", constructor=" + LogUtils.niceExecutableName(constructor) + "}" ;
    }
}
