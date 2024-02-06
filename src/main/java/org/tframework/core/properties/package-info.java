/**
 * The core properties related classes. Properties are used to configure the framework.
 * There are 2 ways to input properties:
 * <ul>
 *     <li>YAML property files placed in the resources.</li>
 *     <li>Directly specified properties (through environmental variables, CLI arguments, etc).</li>
 * </ul>
 * <h3>Property files</h3>
 * Property files are in the YAML format. Reading properties from files follows this process:
 * <ul>
 *     <li>The files are found by {@link org.tframework.core.properties.filescanners.PropertyFileScanner}s.</li>
 *     <li>They are parsed by {@link org.tframework.core.properties.yamlparsers.YamlParser}s.</li>
 *     <li>The parsed contents are turned into properties by {@link org.tframework.core.properties.extractors.PropertiesExtractor}s.</li>
 * </ul>
 * Finally, the extracted properties are placed into the {@link org.tframework.core.properties.PropertiesContainer}.
 * <h3>Directly specified properties.</h3>
 * Directly specified properties are processed after the ones from files and can override those.
 * <ul>
 *     <li>{@link org.tframework.core.properties.scanners.PropertyScanner}s find the raw properties.</li>
 *     <li>The raw properties are parsed by {@link org.tframework.core.properties.parsers.PropertyParser}s.</li>
 * </ul>
 * The properties are then added to the {@link org.tframework.core.properties.PropertiesContainer}.
 * @see org.tframework.core.properties.PropertiesInitializationProcess
 */
package org.tframework.core.properties;
