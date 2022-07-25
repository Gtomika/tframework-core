/**
 * Contains the classes for the TFramework property system. To use this system, have
 * the file {@code properties.yaml} in the {@code src/main/resources} folder. This file contains
 * the application properties, both the global ones, and the flavor specified ones.
 *
 * <h2>Custom location for the properties file</h2>
 * If you are storing the {@code properties.yaml} file in a custom location, the environmental variable
 * {@code TFRAMEWORK_CORE_PROPERTIES_LOCATION} can be used to specify a path where the properties file
 * can be found.
 *
 * <h2>Properties file format</h2>
 * This is the expected format of the {@code properties.yaml} file:
 * <pre><code>
 * global:
 *   # global properties (all flavors)
 * flavors:
 *   debug:
 *     # properties to use on example flavor 'debug'
 *   release:
 *     # properties to use on example flavor 'release'
 * </code></pre>
 * Flavor specific properties will override the global ones if that flavor is active. If multiple flavors are active,
 * and they specify different values for the same property, this will raise an exception.
 *
 * <h2>Custom and builtin properties</h2>
 * The TFramework has multiple builtin properties that can be used to control some of its behaviour, but custom
 * properties can also be defines and later used at runtime.
 *
 * <h2>Property naming</h2>
 * Custom property names can contain only lowercase english letters, underscores and dots, and should be similar to package names,
 * like {@code com.example.my_property=3}.
 *
 * <h2>Environmental variables</h2>
 * It is possible to reference environmental variables in the properties file, using the following syntax:
 * <pre><code>
 * my.custom.property = env-var(MY_CUSTOM_PROPERTY)
 * </code></pre>
 *
 * <h2>Using properties at runtime</h2>
 * //TODO document this
 */
package org.tframework.core.properties;