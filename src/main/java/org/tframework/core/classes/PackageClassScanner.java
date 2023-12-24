/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A {@link ClassScanner} implementation that scans for classes in packages. The {@link ClassGraph} library
 * is used to archive this functionality, which guarantees that:
 * <ul>
 *     <li>Classes both outside and inside JAR files are correctly picked up.</li>
 *     <li>Both outer and inner classes are found.</li>
 * </ul>
 */
public class PackageClassScanner implements ClassScanner {

	private static final int THREAD_COUNT = 5;

	private final Set<String> packageNames;

	/**
	 * Create a package class scanner that will scan in one package.
	 * @param packageName Valid package name.
	 */
	public PackageClassScanner(String packageName) {
		this.packageNames = Set.of(packageName);
	}

	/**
	 * Create a package class scanner that will scan in a list of packages.
	 * @param packageNames Valid package names.
	 */
	public PackageClassScanner(Set<String> packageNames) {
		this.packageNames = packageNames;
	}

	/**
	 * Performs the scan in the packages specified at construction time. If some classes could not
	 * be loaded, they will not be returned.
	 * @return List of classes in the packages.
	 */
	@Override
	public List<Class<?>> scanClasses() {
		ClassGraph classGraph = new ClassGraph()
				.enableClassInfo()
				.acceptPackages(packageNames.toArray(new String[] {}));
		ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
		try(ScanResult scanResult = classGraph.scan(executor, THREAD_COUNT)) {
			return scanResult.getAllClasses().loadClasses(true);
		}
	}
}
