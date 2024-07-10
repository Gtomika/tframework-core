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
package org.tframework.core.elements;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when an element is requested, but it is not found.
 */
public class ElementNotFoundException extends TFrameworkException {

    static final String HAS_NAME = "has name";
    static final String ASSIGNABLE_TO_TYPE = "is assignable to type";

    private static final String TEMPLATE = "Element not found which %s: '%s'";

    public ElementNotFoundException(String name) {
        super(TEMPLATE.formatted(HAS_NAME, name));
    }

    public ElementNotFoundException(Class<?> elementType) {
        super(TEMPLATE.formatted(ASSIGNABLE_TO_TYPE, elementType.getName()));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
