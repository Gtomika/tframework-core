/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.utils;

import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Various utilities to measure elapsed time.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimerUtils {

    /**
     * Calculates the amount of milliseconds between two {@link Instant}s.
     */
    public static long msBetween(Instant start, Instant end) {
        return Duration.between(start, end).toMillis();
    }

}
