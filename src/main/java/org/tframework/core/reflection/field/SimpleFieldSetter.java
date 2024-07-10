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
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;

@Slf4j
@Element
public class SimpleFieldSetter implements FieldSetter {

    @Override
    public void setFieldValue(Object object, Field field, Object value) {
        try {
            if(field.canAccess(object)) {
                field.set(object, value);
            } else {
                field.setAccessible(true);
                field.set(object, value);
                field.setAccessible(false);
            }
        } catch (Exception e) {
            throw new FieldSettingException(field, object.getClass(), e);
        }
    }
}
