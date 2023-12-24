/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.readers;

import java.nio.file.Path;
import java.util.Map;

public interface PropertyReader {

    Map<String, Object> readPropertiesYaml(Path yamlPath);

}
