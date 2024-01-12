/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * A {@link ElementMethodScanner} that is able to find {@link org.tframework.core.di.annotations.Element}s
 * from the methods a classes. These classes are often found by {@link ElementClassScanner}s and
 * then passed to {@link ElementMethodScanner}s for further scanning of {@link org.tframework.core.di.annotations.Element}s.
 */
public interface ElementMethodScanner {

    Set<Method> scanElements();

}
