/* Licensed under Apache-2.0 2023. */
package org.tframework.core.readers;

import org.tframework.core.TFrameworkException;

public class EnvironmentVariableNotFoundException extends TFrameworkException {

    private static final String TEMPLATE = "The environmental variable with name '%s' does not exist";

    public EnvironmentVariableNotFoundException(String variableName) {
        super(TEMPLATE.formatted(variableName));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
