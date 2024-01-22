package org.tframework.core.elements.dependency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DependencyUtilsTest {

    private static final String DEPENDENCY_DECLARED_AS = "Unit test";

    @Mock
    private DependencyResolver dependencyResolver;

    private List<DependencyResolver> dependencyResolvers;
    private DependencyDefinition dependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        dependencyResolvers = List.of(dependencyResolver);
        Field someField = this.getClass().getDeclaredField("someString");
        dependencyDefinition = DependencyDefinition.fromField(someField);
    }

    @Test
    void shouldResolveDependency() {
        String dependencyValue = "someValue";
        when(dependencyResolver.resolveDependency(dependencyDefinition))
                .thenReturn(Optional.of(dependencyValue));

        Object resolvedDependency = DependencyUtils.resolveDependency(
                dependencyDefinition,
                DEPENDENCY_DECLARED_AS,
                dependencyResolvers
        );

        assertEquals(dependencyValue, resolvedDependency);
    }

    @Test
    void shouldThrowDependencyResolutionException_whenDependencyCannotBeResolved() {
        when(dependencyResolver.resolveDependency(dependencyDefinition))
                .thenReturn(Optional.empty());

        var e = assertThrows(DependencyResolutionException.class, () -> DependencyUtils.resolveDependency(
                        dependencyDefinition,
                        DEPENDENCY_DECLARED_AS,
                        dependencyResolvers
                )
        );

        String expectedMessage = e.getMessageTemplate().formatted(
                DEPENDENCY_DECLARED_AS,
                dependencyDefinition.dependencyType().getName(),
                LogUtils.objectClassNames(dependencyResolvers)
        );
        assertEquals(expectedMessage, e.getMessage());
    }

    private String someString;
}