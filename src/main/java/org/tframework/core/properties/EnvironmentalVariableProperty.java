/* Licensed under Apache-2.0 2022. */
package org.tframework.core.properties;

/**
 * Specialized property which "stores" an environmental variable and can be used to
 * retrieve its value.
 */
public class EnvironmentalVariableProperty extends Property<String> {

    public EnvironmentalVariableProperty(String name) {
        super(name, System.getenv(name));
    }

    @Override
    public String getValue() throws NullPointerException {
        if(value == null) {
            throw new NullPointerException(String.format("The environmental variable '%s' has no value.", name));
        }
        return value;
    }

}
