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
package org.tframework.core.properties;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when attempting to retrieve a property which does not exist.
 */
public class PropertyNotFoundException extends TFrameworkException {

    private static final String TEMPLATE = "Property with name '%s' does not exist";

    public PropertyNotFoundException(String propertyName) {
        super(TEMPLATE.formatted(propertyName));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
