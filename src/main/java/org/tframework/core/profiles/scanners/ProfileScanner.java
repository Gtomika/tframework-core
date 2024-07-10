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
package org.tframework.core.profiles.scanners;

import java.util.Set;

/**
 * A profile scanner is responsible to detect profiles from a single location,
 * which is implementation dependent: can be environmental variables, command line arguments, etc.
 * The scanner implementations should not attempt to clean or validate or process the profiles,
 * these are the responsibilities of other components.
 * @see ProfileScannersFactory
 */
public interface ProfileScanner {

    /**
     * Scan for the profiles.
     * @return A {@link Set} with the detected profiles. This must not be null: of no profiles were found, empty set
     * should be returned.
     */
    Set<String> scan();

}
