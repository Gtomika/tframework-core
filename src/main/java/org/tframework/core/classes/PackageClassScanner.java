/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A {@link ClassScanner} implementation that scans for classes in packages. The {@link ClassGraph} library
 * is used to archive this functionality, which guarantees that:
 * <ul>
 *     <li>Classes both outside and inside JAR files are correctly picked up.</li>
 *     <li>Both outer and inner classes are found.</li>
 * </ul>
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PackageClassScanner implements ClassScanner {

    private static final int THREAD_COUNT = 5;

    private final Set<String> packageNames;

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
            //TODO: should this be changed to use ClassLoaderUtils.loadClass?
            return scanResult.getAllClasses().loadClasses(true);
        }
    }
}
