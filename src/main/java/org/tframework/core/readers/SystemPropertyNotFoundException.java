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
package org.tframework.core.readers;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when a {@link SystemPropertyReader} cannot find a system property.
 */
public class SystemPropertyNotFoundException extends TFrameworkException {

    private static final String TEMPLATE = "System property '%s' not found.";

    public SystemPropertyNotFoundException(String systemPropertyName) {
        super(TEMPLATE.formatted(systemPropertyName));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }

}
