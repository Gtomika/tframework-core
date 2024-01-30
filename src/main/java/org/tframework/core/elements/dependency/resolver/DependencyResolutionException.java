package org.tframework.core.elements.dependency.resolver;

import lombok.Builder;
import org.tframework.core.TFrameworkException;
import org.tframework.core.utils.LogUtils;

import java.util.List;

/**
 * Thrown when no available {@link BasicDependencyResolver} is able to resolve a dependency.
 */
@Builder
public class DependencyResolutionException extends TFrameworkException {

    private static final String TEMPLATE = """
            Failed to resolve required dependency!
            - Dependency declared as: %s
            - Dependency type: %s
            - Used resolvers: %s
            """;

    private final String declaredAs;
    private final Class<?> dependencyType;
    private final List<DependencyResolver> usedResolvers;

    private DependencyResolutionException(
            String declaredAs,
            Class<?> dependencyType,
            List<DependencyResolver> usedResolvers
    ) {
        super(TEMPLATE.formatted(declaredAs, dependencyType.getName(), LogUtils.objectClassNames(usedResolvers)));
        this.declaredAs = declaredAs;
        this.dependencyType = dependencyType;
        this.usedResolvers = usedResolvers;
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
