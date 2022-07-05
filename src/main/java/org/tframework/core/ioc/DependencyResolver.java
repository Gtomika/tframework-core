package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.ioc.exceptions.IocException;

/**
 * Investigates the managed entities and resolves the dependencies between them by creating
 * and injecting instances of managed entities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DependencyResolver {

    public static void resolveDependencies() throws IocException {

    }

}
