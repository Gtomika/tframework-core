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
package org.tframework.core.properties.group;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.tframework.core.elements.annotations.InjectProperty;

/**
 * This annotation can be used to specify properties in certain situations, for example when using
 * {@link PropertyGroup} to load properties into a class.
 * <p><br>
 * <b>Do not confuse</b> this annotation with {@link InjectProperty}. That one is used to inject properties
 * into elements, while this one is used to specify properties in certain situations.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * Property specification. The exact meaning of this value depends on the context in which
     * the annotation is used.
     */
    String value();
}
