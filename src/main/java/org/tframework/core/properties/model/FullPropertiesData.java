package org.tframework.core.properties.model;

import lombok.Data;

import java.util.Map;

/**
 * Model class for the data in the entirety of the {@code properties.yaml} file, including
 * global properties and flavor properties.
 */
@Data
public class FullPropertiesData {

    /**
     * The global properties.
     * <ul>
     *     <li>Keys are the property names.</li>
     *     <li>Values are the property values, can be of multiple types.</li>
     * </ul>
     */
    private Map<String, Object> global;

    /**
     * The flavor specific properties.
     * <ul>
     *     <li>Keys are the names of the flavors.</li>
     *     <li>Values are the properties for the flavors.</li>
     * </ul>
     */
    private Map<String, Map<String, Object>> flavors;
}
