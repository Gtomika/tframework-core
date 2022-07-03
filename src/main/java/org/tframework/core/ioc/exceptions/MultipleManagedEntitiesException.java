/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

/** Thrown when it is not possible to decide which managed entity to return or inject. */
public class MultipleManagedEntitiesException extends TFrameworkRuntimeException {

    public MultipleManagedEntitiesException(Class<?> clazz) {
        super(
                String.format(
                        "Multiple managed entities with type '%s' were found.", clazz.getName()));
    }
}
