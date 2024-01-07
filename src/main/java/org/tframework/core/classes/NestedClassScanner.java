/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This {@link ClassScanner} implementation scans all nested classes of a given class.
 * The class itself will also be included.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class NestedClassScanner implements ClassScanner {

    private final Class<?> classToScan;

    /**
     * Use reflection to detect and return the nested classes of the one provided at construction time.
     */
    @Override
    public Set<Class<?>> scanClasses() {
        HashSet<Class<?>> classes = new HashSet<>();
        classes.add(classToScan);
        classes.addAll(Arrays.asList(classToScan.getDeclaredClasses()));
        return classes;
    }
}
