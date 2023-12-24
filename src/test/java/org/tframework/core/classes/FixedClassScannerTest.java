/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class FixedClassScannerTest {

	@Test
	public void shouldScanFixedClasses() {
		List<Class<?>> classes = List.of(String.class, Integer.class);
		var scanner = new FixedClassScanner(classes);

		var scannedClasses = scanner.scanClasses();
		assertEquals(classes, scannedClasses);
	}

}
