/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context.source;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * The element source is a common interface for classes that define where an element was discovered.
 * Depending on the source of the element, its construction may be different.
 */
public interface ElementSource<T extends AnnotatedElement> {

    /**
     * @return A list of {@link Parameter}s that are required to construct the element.
     * Classes responsible for constructing the element should use this list to determine
     * what kind of dependencies are required for element construction.
     */
    List<Parameter> elementConstructionParameters();

}
