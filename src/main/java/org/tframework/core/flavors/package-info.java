/**
 * Application flavors are basically profiles, which can be used to pass
 * different properties, managed entities and more based on the selected flavors.
 * <ul>
 *     <li>
 *         Flavor names can have only english lower case letters and underscores, such as {@code debug}, {@code release}
 *         or {@code integration_test}.
 *     </li>
 *     <li>There does not need to be any active flavor.</li>
 *     <li>There can be multiple selected flavors.</li>
 * </ul>
 * There are multiple ways on can set active favors, but they must be set before application startup,
 * they cannot be changed at runtime.
 * <ul>
 *     <li>
 *         By setting the value of the {@code TFRAMEWORK_CORE_ACTIVE_FLAVORS} environmental variable. Supports even multiple
 *         flavors in a comma separated list.
 *     </li>
 * </ul>
 */
package org.tframework.core.flavors;