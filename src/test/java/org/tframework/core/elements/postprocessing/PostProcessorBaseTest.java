/*
Copyright 2024 Tamas Gaspar

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
package org.tframework.core.elements.postprocessing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

@ExtendWith(MockitoExtension.class)
public class PostProcessorBaseTest {

    @Mock
    protected ProfilesContainer profilesContainer;

    @Mock
    protected PropertiesContainer propertiesContainer;

    @Mock
    protected ElementsContainer elementsContainer;

    @Mock
    protected ElementContext elementContext;

    protected Application application;

    @BeforeEach
    public void setUpPostProcessorBase() {
        application = Application.builder()
                .profilesContainer(profilesContainer)
                .propertiesContainer(propertiesContainer)
                .elementsContainer(elementsContainer)
                .name("test-application")
                .rootClass(this.getClass())
                .build();
    }
}
