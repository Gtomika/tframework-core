package org.tframework.core.ioc.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

/**
 * Thrown when an illegal dependency relation was discovered.
 */
public class InvalidDependencyException extends TFrameworkRuntimeException {

    /**
     * Create dependency exception.
     * @param managedEntityName The name of the entity which has the dependency.
     * @param dependencyName The name of the entity which is the dependency.
     * @param reason Reason why this dependency is invalid.
     */
    public InvalidDependencyException(
            String managedEntityName,
            String dependencyName,
            String reason
    ) {
        super(String.format("The managed entity '%s' has an illegal or failed dependency: '%s'. Reason: %s",
                managedEntityName, dependencyName, reason));
    }

    /**
     * Create dependency exception.
     * @param managedEntityName The name of the entity which has the dependency.
     * @param dependencyName The name of the entity which is the dependency.
     * @param cause Exception that triggered this invalid dependency exception.
     */
    public InvalidDependencyException(
            String managedEntityName,
            String dependencyName,
            Exception cause
    ) {
        super(String.format("The managed entity '%s' has an illegal or failed dependency: '%s'.",
                managedEntityName, dependencyName), cause);
    }

    public InvalidDependencyException(String cause) {
        super(cause);
    }
}
