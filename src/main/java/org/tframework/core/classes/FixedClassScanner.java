/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A {@link ClassScanner} implementation that does not actually perform any scanning,
 * but rather returns the classes provided at construction time.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FixedClassScanner implements ClassScanner {

    private final List<Class<?>> classes;

    @Override
    public List<Class<?>> scanClasses() {
        return List.copyOf(classes);
    }
}
