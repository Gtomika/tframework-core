/* Licensed under Apache-2.0 2023. */
package org.tframework.core.loop;

import java.util.Map;

public record Event(
		String name,
		Map<String, Object> metadata
) {

	public Event {
		if(name == null) {
			throw new IllegalArgumentException("Event name cannot be null.");
		}
		if(metadata == null) {
			throw new IllegalArgumentException("Event metadata cannot be null. Use empty instead.");
		}
	}

	public static Event create(String name) {
		return new Event(name, Map.of());
	}

	public static Event create(String name, Map<String, Object> metadata) {
		return new Event(name, metadata);
	}

}
