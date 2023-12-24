/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * The profile merger combines the output of several {@link ProfileScanner}s.
 */
@Slf4j
public class ProfileMerger {

	private final List<ProfileScanner> profileScanners;

	private ProfileMerger(@NonNull List<ProfileScanner> profileScanners) {
		this.profileScanners = profileScanners;
	}

	/**
	 * Performs the merge of the given {@link ProfileScanner}s.
	 * @return {@link Stream} of profiles combined from all the scanners, ready for further processing.
	 */
	public Stream<String> mergeAndStream() {
		log.debug("The profile merger will combine the following profile scanners: {}", profileScanners
				.stream()
				.map(s -> s.getClass().getName())
				.toList());
		Stream<String> profiles = Stream.empty();

		for(ProfileScanner scanner: profileScanners) {
			profiles = Stream.concat(profiles, scanner.scan().stream());
		}

		return profiles;
	}

	/**
	 * Creates a {@link ProfileMerger} that combines the outputs of the given {@link ProfileScanner}s.
	 */
	public static ProfileMerger merging(@NonNull List<ProfileScanner> profileScanners) {
		return new ProfileMerger(profileScanners);
	}

}
