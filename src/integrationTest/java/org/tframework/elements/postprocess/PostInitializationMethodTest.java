/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.postprocess;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.postprocessing.annotations.PostInitialization;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class PostInitializationMethodTest {

    private final List<String> data;

    public PostInitializationMethodTest() {
        data = new LinkedList<>();
    }

    @PostInitialization
    public void addData1() {
        data.add("data1");
    }

    @PostInitialization
    public boolean addData2() { //return value will be ignored
        return data.add("data2");
    }

    @Test
    public void shouldExecutePostInitializationMethods() {
        assertTrue(data.contains("data1"));
        assertTrue(data.contains("data2"));
    }

}
