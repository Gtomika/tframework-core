/**
 * Application flavors are basically profiles, which can be used to pass
 * different properties, managed entities and more based on the selected flavors.
 * <ul>
 *     <li>Flavor names can have only english lower case letters, such as {@code debug} or {@code release}.</li>
 *     <li>There does not need to be any active flavor.</li>
 *     <li>There can be multiple selected flavors.</li>
 * </ul>
 * There are multiple ways on can set active favors, but they must be set before application startup,
 * they cannot be changed at runtime.
 * <ul>
 *     <li>
 *         By setting the value of the {@code TFRAMEWORK_ACTIVE_FLAVOR} environmental variable. Supports even multiple
 *         flavors in a comma separated list
 *     </li>
 *     <li>
 *         By setting the {@code org.tframework.core.active_flavor} global property. Similarly to previous methods, this
 *         can also be used with comma separated flavor list.
 *     </li>
 * </ul>
 */
package org.tframework.core.flavors;