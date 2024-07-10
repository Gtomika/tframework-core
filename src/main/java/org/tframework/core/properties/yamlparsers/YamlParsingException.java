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
package org.tframework.core.properties.yamlparsers;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when the provided content that should be valid YAML cannot be parsed.
 */
public class YamlParsingException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to parse YAML:\n%s";

    public YamlParsingException(String yaml, Throwable cause) {
        super(TEMPLATE.formatted(yaml), cause);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
