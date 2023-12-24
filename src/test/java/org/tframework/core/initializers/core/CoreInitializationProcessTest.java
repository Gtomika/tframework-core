/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.Application;
import org.tframework.core.profiles.InvalidProfileException;
import org.tframework.core.profiles.Profiles;

@ExtendWith(MockitoExtension.class)
class CoreInitializationProcessTest {

	private CoreInitializationProcess coreInitializationProcess;

	@Mock
	private ProfilesCoreInitializer profilesCoreInitializer;

	@BeforeEach
	public void setUp() {
		coreInitializationProcess = new CoreInitializationProcess(
				profilesCoreInitializer
		);
	}

	@Test
	public void shouldPerformCoreInitialization() {
		var expectedResult = new CoreInitializationResult(
				Application.builder()
						.profiles(new Profiles(Set.of("a, b")))
						.build()
		);

		when(profilesCoreInitializer.initialize(any())).thenReturn(expectedResult.application().profiles());

		CoreInitializationInput input = CoreInitializationInput.builder()
				.args(new String[]{"testArg"})
				.build();

		var actualResult = coreInitializationProcess.performCoreInitialization(input);

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void shouldThrowInitializationException_whenACoreInitializerThrowsException() {
		var cause = new InvalidProfileException("invalid!");
		when(profilesCoreInitializer.initialize(any()))
				.thenThrow(cause);

		CoreInitializationInput input = CoreInitializationInput.builder()
				.args(new String[]{"testArg"})
				.build();

		var exception = assertThrows(InitializationException.class, () -> {
			coreInitializationProcess.performCoreInitialization(input);
		});

		assertEquals(
				exception.getMessageTemplate().formatted(cause.getClass().getName()),
				exception.getMessage()
		);
		assertEquals(
				cause.getMessage(),
				exception.getCause().getMessage()
		);
	}

}
