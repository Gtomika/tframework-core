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

/**
 * Sets values into object's {@link Field}s.
 */
public interface FieldSetter {

    /**
     * Sets the field's value.
     * @param object The object whose field should be set.
     * @param field The {@link Field} to set. This must be a field of {@code object}.
     * @param value The value to set into {@code field}. This must be assignable to {@code field}'s type.
     * @throws FieldSettingException If an underlying exception occurred while setting the method.
     */
    void setFieldValue(Object object, Field field, Object value) throws FieldSettingException;

}
