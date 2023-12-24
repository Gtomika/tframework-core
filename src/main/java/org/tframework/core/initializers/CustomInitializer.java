/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import java.util.Set;

/**
 * Initializers define tasks are run at application startup.
 */
public interface CustomInitializer {

	/**
	 * The method that will be executed at application startup. Any type of runtime exception can be thrown
	 * from the initializer. An exception here means that the initialization process failed, and the
	 * application cannot start.
	 */
	void initialize();

	/**
	 * Defines the other initializers that this one depends on. Use this method to set execution order between
	 * initializers. Details about dependencies:
	 * <ul>
	 *     <li>Listing an initializer as it's own dependency is not allowed.</li>
	 *     <li>Setting circular dependencies is not allowed.</li>
	 *     <li>You need to list only <b>direct</b> dependencies here.</li>
	 * </ul>
	 * Initializers that have no dependency relationship can run in any order of each other, even parallel.
	 * @return Set of initializer dependencies.
	 */
	default Set<Class<? extends CustomInitializer>> dependsOn() {
		return Set.of();
	}

}
