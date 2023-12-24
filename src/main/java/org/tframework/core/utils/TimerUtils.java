/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Various utilities to measure elapsed time.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimerUtils {

    /**
     * Calculates the amount of milliseconds between two {@link Instant}s.
     */
    public static long msBetween(Instant start, Instant end) {
        return Duration.between(start, end).toMillis();
    }

}
