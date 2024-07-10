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
package org.tframework.core.initializers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.elements.ElementsInitializationProcessFactory;
import org.tframework.core.profiles.ProfileInitializationProcessFactory;
import org.tframework.core.properties.PropertiesInitializationProcessFactory;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;

/**
 * Factory methods to instantiate {@link CoreInitializer}s and the {@link CoreInitializationProcess}.
 */
@TFrameworkInternal
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoreInitializationFactory {

    /**
     * Creates a {@link CoreInitializationProcess} that performs framework initialization.
     */
    public static CoreInitializationProcess createCoreInitializationProcess() {
        return new CoreInitializationProcess(
                createProfilesCoreInitializer(),
                createPropertiesCoreInitializer(),
                createDependencyInjectionCoreInitializer(),
                AnnotationScannersFactory.createComposedAnnotationScanner()
        );
    }

    private static ProfilesCoreInitializer createProfilesCoreInitializer() {
        var profilesProcess = ProfileInitializationProcessFactory.createProfileInitializationProcess();
        return new ProfilesCoreInitializer(profilesProcess);
    }

    private static PropertiesCoreInitializer createPropertiesCoreInitializer() {
        var propertiesProcess = PropertiesInitializationProcessFactory.createProfileInitializationProcess();
        return new PropertiesCoreInitializer(propertiesProcess);
    }

    private static ElementsCoreInitializer createDependencyInjectionCoreInitializer() {
        var dependencyInjectionProcess = ElementsInitializationProcessFactory.createElementInitializationProcess();
        return new ElementsCoreInitializer(dependencyInjectionProcess);
    }

}
