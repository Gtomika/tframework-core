/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.utils.ClassLoaderUtils;

/**
 * A {@link ClassScanner} implementation that scans for classes in packages. The {@link ClassGraph} library
 * is used to archive this functionality, which guarantees that:
 * <ul>
 *     <li>Classes both outside and inside JAR files are correctly picked up.</li>
 *     <li>Both outer and inner classes are found.</li>
 * </ul>
 */
@Slf4j
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PackageClassScanner implements ClassScanner {

    private static final int THREAD_COUNT = 5;

    private final Set<String> packageNames;

    /**
     * Performs the scan in the packages specified at construction time. If some classes could not
     * be loaded, they will not be returned.
     * @return {@link Set} of classes in the packages.
     */
    @Override
    public Set<Class<?>> scanClasses() {
        ClassGraph classGraph = new ClassGraph()
                .enableClassInfo()
                .acceptPackages(packageNames.toArray(new String[] {}));
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        try(ScanResult scanResult = classGraph.scan(executor, THREAD_COUNT)) {
            return scanResult.getAllClasses().stream()
                    .map(info -> {
                        try {
                            return ClassLoaderUtils.loadClass(info.getName(), PackageClassScanner.class);
                        } catch (ClassNotFoundException e) {
                            log.warn("Could not load class '{}'", info.getName(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
    }
}
