/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.profiles.scanners.ProfileScanner;

/**
 * The profile merger combines the output of several {@link ProfileScanner}s. Use
 * {@link #merging(List)} to create a profile merger that combines output of the given scanners.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMerger {

    private final List<ProfileScanner> profileScanners;

    /**
     * Performs the merge of the given {@link ProfileScanner}s.
     * @return {@link Stream} of profiles combined from all the scanners, ready for further processing.
     */
    public Stream<String> mergeAndStream() {
        log.debug("The profile merger will combine the following profile scanners: {}", profileScanners
                .stream()
                .map(s -> s.getClass().getName())
                .toList());
        Set<String> profiles = new HashSet<>();

        for(ProfileScanner scanner: profileScanners) {
            profiles.addAll(scanner.scan());
        }

        return profiles.stream();
    }

    /**
     * Creates a {@link ProfileMerger} that combines the outputs of the given {@link ProfileScanner}s.
     */
    static ProfileMerger merging(@NonNull List<ProfileScanner> profileScanners) {
        return new ProfileMerger(profileScanners);
    }

}
