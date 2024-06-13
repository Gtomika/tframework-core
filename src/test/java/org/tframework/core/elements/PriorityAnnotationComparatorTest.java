/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.reflection.annotations.AnnotationScanner;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class PriorityAnnotationComparatorTest {

    @Mock
    private AnnotationScanner annotationScanner;

    private PriorityAnnotationComparator comparator;

    @BeforeEach
    public void setUp() {
        comparator = new PriorityAnnotationComparator(annotationScanner);

        when(annotationScanner.scanOneStrict(TestClass1.class, Priority.class))
                .thenReturn(Optional.of(TestClass1.class.getAnnotation(Priority.class)));
        when(annotationScanner.scanOneStrict(TestClass2.class, Priority.class))
                .thenReturn(Optional.of(TestClass2.class.getAnnotation(Priority.class)));
        when(annotationScanner.scanOneStrict(TestClass3.class, Priority.class))
                .thenReturn(Optional.empty());
    }

    @Test
    public void shouldCompareCorrectly_whenBothObjectsAreAnnotated() {
        var e1 = Mockito.mock(ElementContext.class);
        doReturn(TestClass1.class).when(e1).getType();
        var e2 = Mockito.mock(ElementContext.class);
        doReturn(TestClass2.class).when(e2).getType();

        var result = comparator.compare(e1, e2);
        assertTrue(result > 0);
    }

    @Test
    public void shouldCompareCorrectly_whenOneObjectIsAnnotated() {
        var e1 = Mockito.mock(ElementContext.class);
        doReturn(TestClass1.class).when(e1).getType();
        var e2 = Mockito.mock(ElementContext.class);
        doReturn(TestClass3.class).when(e2).getType();

        var result = comparator.compare(e1, e2);
        assertTrue(result < 0);
    }

    @Priority(1)
    static class TestClass1 {
    }

    @Priority(2)
    static class TestClass2 {
    }

    /**
     * Defaults to {@link Priority#DEFAULT}
     */
    static class TestClass3 {
    }
}
