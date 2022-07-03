/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

public class NameNotUniqueException extends TFrameworkRuntimeException {

    public NameNotUniqueException(String name) {
        super(String.format("The managed entity name '%s' is not unique.", name));
    }
}
