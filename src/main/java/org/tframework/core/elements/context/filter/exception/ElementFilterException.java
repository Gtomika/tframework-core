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
package org.tframework.core.elements.context.filter.exception;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when an error occurs while filtering elements
 */
public class ElementFilterException extends TFrameworkException {

    private static final String TEMPLATE = "An error occurred while filtering elements: %s";

    public ElementFilterException(String message) {
        super(TEMPLATE.formatted(message));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
