/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link ClassScanner} implementation that scans for classes in packages. The {@link ClassGraph} library
 * is used to archive this functionality, which guarantees that:
 * <ul>
 *     <li>Classes both outside and inside JAR files are correctly picked up.</li>
 *     <li>Both outer and inner classes are found.</li>
 *     <li>Wildcards are supported inside the package names. For example {@code some.*.stuff}</li>
 * </ul>
 * There is no need to specify subpackages, because all subpackages are automatically scanned.
 */
@Slf4j
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PackageClassScanner implements ClassScanner {

    private static final int THREAD_COUNT = 5;

    private Set<String> packageNames;
    private Set<String> rejectedPackages;

    PackageClassScanner(Set<String> packageNames) {
        this.packageNames = packageNames;
        this.rejectedPackages = Set.of();
    }

    /**
     * Performs the scan in the packages specified at construction time. If some classes could not
     * be loaded, they will not be returned.
     * @return {@link Set} of classes in the packages.
     */
    @Override
    public Set<Class<?>> scanClasses() {
        ClassGraph classGraph = new ClassGraph()
                .enableClassInfo()
                .acceptPackages(safeToArray(packageNames))
                .rejectPackages(safeToArray(rejectedPackages));

        try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            try(ScanResult scanResult = classGraph.scan(executor, THREAD_COUNT)) {
                return scanResult.getAllClasses().stream()
                        .map(info -> {
                            try {
                                return info.loadClass(); //important to use class graph's own loader here
                            } catch (IllegalArgumentException e) {
                                log.warn("Could not load class '{}'", info.getName(), e);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
            }
        }
    }

    private String[] safeToArray(Set<String> set) {
        if(set == null) {
            return new String[0];
        }
        return set.toArray(new String[0]);
    }
}
