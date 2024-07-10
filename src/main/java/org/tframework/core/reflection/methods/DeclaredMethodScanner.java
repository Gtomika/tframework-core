/*
Copyright 2024 Tamas Gaspar

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
package org.tframework.core.reflection.methods;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.tframework.core.elements.annotations.Element;

/**
 * A reasonable default implementation for {@link MethodScanner} that uses
 * reflections to find methods on a single class. Only methods declared on the
 * class itself will be returned (for example, methods on the superclass will not be found).
 */
@Element
public class DeclaredMethodScanner implements MethodScanner {

    @Override
    public Set<Method> scanMethods(Class<?> classToScan) {
        return Arrays.stream(classToScan.getDeclaredMethods())
                .collect(Collectors.toSet());
    }
}
