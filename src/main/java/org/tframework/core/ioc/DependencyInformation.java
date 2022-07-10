package org.tframework.core.ioc;

import lombok.Builder;
import lombok.Data;
import org.tframework.core.ioc.constants.InjectionType;

/**
 * Collects information about a dependency in a managed entity. This does not contain
 * an actual instance of the dependency.
 */
@Data
@Builder
public class DependencyInformation {

    /**
     * Name of the dependency, which is a name of a managed entity;
     * For example, if managed entity 'A' has an {@link org.tframework.core.ioc.annotations.Injected}
     * field of another managed entity 'B', then this will be the entity name of 'B'.
     */
    private final String managedEntityName;

    /**
     * Stores how the injection takes place.
     */
    private final InjectionType injectionType;

    /**
     * Stores what is that name of the variable, which is annotated with
     * {@link org.tframework.core.ioc.annotations.Injected}. Depending on the value of
     * {@link #injectionType}, this can be a field of a constructor parameter.
     */
    private final String variableName;

    /**
     * Flag to store whether this dependency was resolved or not. Resolved means that
     * an actual instance of the dependency was injected into the variable {@link #variableName}.
     */
    private boolean resolved = false;

}
