/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.annotations.AnnotationScanner;

@ExtendWith(MockitoExtension.class)
class DefaultClassFilterTest {

	private final DefaultClassFilter defaultClassFilter = new DefaultClassFilter();

	@Mock
	private AnnotationScanner annotationScanner;

	@Test
	public void shouldFilterByAnnotation() {
		List<Class<?>> classes = List.of(TestFilterClass1.class, TestFilterClass2.class);

		when(annotationScanner.hasAnnotation(TestFilterClass1.class, TestAnnotation.class)).thenReturn(false);
		when(annotationScanner.hasAnnotation(TestFilterClass2.class, TestAnnotation.class)).thenReturn(true);
		var filteredClasses = defaultClassFilter.filterByAnnotation(classes, TestAnnotation.class, annotationScanner);

		assertEquals(1, filteredClasses.size());
		assertTrue(filteredClasses.stream().anyMatch(clazz -> clazz.getName().equals(TestFilterClass2.class.getName())));
	}

	@Test
	public void shouldFilterByInterface() {
		List<Class<?>> classes = List.of(TestFilterClass1.class, TestFilterClass2.class);
		var filteredClasses = defaultClassFilter.filterByInterface(classes, TestInterface.class);

		assertEquals(1, filteredClasses.size());
		assertTrue(filteredClasses.stream().anyMatch(clazz -> clazz.getName().equals(TestFilterClass1.class.getName())));
	}

	@Test
	public void shouldThrowNotInterfaceException_ifFilterByInterfaceParamIsNotInterface() {
		List<Class<?>> classes = List.of(TestFilterClass1.class, TestFilterClass2.class);

		NotAnInterfaceException exception = assertThrows(NotAnInterfaceException.class, () -> {
			defaultClassFilter.filterByInterface(classes, DefaultClassFilterTest.class);
		});

		assertEquals(
				exception.getMessageTemplate().formatted(DefaultClassFilterTest.class.getName()),
				exception.getMessage()
		);
	}

	interface TestInterface {}

	@Retention(RetentionPolicy.RUNTIME)
	@interface TestAnnotation {}

	static class TestFilterClass1 implements TestInterface {}

	@TestAnnotation
	static class TestFilterClass2 {}

}
