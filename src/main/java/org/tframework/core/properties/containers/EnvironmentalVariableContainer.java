package org.tframework.core.properties.containers;

import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.exceptions.NotConstructibleException;
import org.tframework.core.properties.annotations.InjectEnvironmentalVariable;
import org.tframework.core.properties.exceptions.EnvironmentalVariableException;

import java.util.Optional;

/**
 * Stores reference to an environmental variable. This container connect the IoC with environmental
 * variables, allowing them to be injected using the {@link InjectEnvironmentalVariable} annotation.
 */
public class EnvironmentalVariableContainer extends AbstractContainer<String> {

    /**
     * Name of the referenced variable.
     */
    private final String variableName;

    /**
     * Create environmental variable container.
     * @param variableName Variable name.
     * @throws EnvironmentalVariableException If the variable with referenced name does not exist.
     */
    public EnvironmentalVariableContainer(String variableName) throws EnvironmentalVariableException {
        super(ManagingType.PROPERTY, variableName, String.class);
        String value = System.getenv(variableName);
        if(value == null) {
            throw new EnvironmentalVariableException(String.format(
                    "Environmental variable with name '%s' not found.", variableName));
        }
        this.variableName = variableName;
    }

    /**
     * Gets the value of the variable.
     * @throws NotConstructibleException If the variable does not exist. Not expected to be thrown here, existence is
     * checked at construction time.
     */
    @Override
    public String grabInstance() throws NotConstructibleException {
        String value = System.getenv(variableName);
        return Optional.ofNullable(value).orElseThrow(() -> NotConstructibleException.customNotConstructibleException(
                String.format("Environmental variable with name '%s' not found.", variableName)
        ));
    }
}
