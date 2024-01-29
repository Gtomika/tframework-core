package org.tframework.core.elements.dependency.resolver;

/**
 * A marker interface for all kind of dependency resolvers.
 */
public sealed interface DependencyResolver permits BasicDependencyResolver, ElementDependencyResolver {
}
