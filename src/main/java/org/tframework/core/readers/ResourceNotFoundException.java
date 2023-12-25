/* Licensed under Apache-2.0 2023. */
package org.tframework.core.readers;

import org.tframework.core.TFrameworkException;

public class ResourceNotFoundException extends TFrameworkException {

    private static final String TEMPLATE = "Resource with path '%s' was not found";

    public ResourceNotFoundException(String resourceName) {
        super(TEMPLATE.formatted(resourceName));
    }

    public ResourceNotFoundException(String resourceName, Exception cause) {
        super(TEMPLATE.formatted(resourceName), cause);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
