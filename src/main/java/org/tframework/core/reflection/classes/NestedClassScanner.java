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
package org.tframework.core.reflection.classes;

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
