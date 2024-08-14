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
package org.tframework.test.shutdown;

import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.events.CoreEvents;
import org.tframework.core.events.annotations.Subscribe;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

/*
This test relies on the `tframework-test` module shutting down the
test application after the tests are finished.
 */
@Slf4j
@IsolatedTFrameworkTest
public class ApplicationShutdownTest {

    @Element
    public static class ElementWithImportantResource {

        private boolean importantResourceOpened;

        @Subscribe(CoreEvents.APPLICATION_INITIALIZED)
        public void open(Application application) {
            importantResourceOpened = true;
            log.info("Important resource opened.");
        }

        @Subscribe(CoreEvents.APPLICATION_SHUTTING_DOWN)
        public void close(Application application) {
            importantResourceOpened = false;
            log.info("Important resource closed.");
        }
    }

    @Test
    public void shouldBeOpenDuringTests(@InjectElement ElementWithImportantResource element) {
        //the resource will be opened here, because shutdown is not invoked yet
        assertTrue(element.importantResourceOpened);
    }

    //TODO: how to test that the resource is closed after the test?
}
