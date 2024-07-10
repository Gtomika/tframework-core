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
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.tframework.core.elements.annotations.Element;

@Element
public class SimpleFieldFilter implements FieldFilter {

    @Override
    public boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    @Override
    public boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }
}
