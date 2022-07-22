package org.tframework.core.ioc;

import lombok.Builder;
import lombok.Data;
import org.tframework.core.ioc.constants.InjectionType;
import org.tframework.core.ioc.containers.AbstractContainer;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * Collects information about a dependency in a managed entity.
 */
@Data
@Builder
public class DependencyInformation<T> {

    /**
     * Container of the dependency. Can be used to get instance of it.
     */
    private AbstractContainer<T> dependencyContainer;

    /**
     * Stores how the injection takes place.
     */
    private final InjectionType injectionType;

    /**
     * The field that has the {@link org.tframework.core.ioc.annotations.Injected} annotation. This will only
     * have value if this dependency is injected to a field, when {@link #injectionType} is {@link InjectionType#FIELD_INJECTION}.
     * Otherwise, it will be null.
     */
    @Nullable
    private final Field injectedField;

    /**
     * The parameter of the provider method ir constructor where this dependency must be injected. This will only
     * have value if this dependency is injected to a parameter, when {@link #injectionType} is {@link InjectionType#PROVIDER_INJECTION}
     * or {@link InjectionType#CONSTRUCTOR_INJECTION}.
     * Otherwise, it will be null.
     */
    @Nullable
    private final Parameter injectedParameter;

}
