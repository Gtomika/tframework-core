/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MultiValueMapTest {

    private static final String TEST_KEY = "test";
    private static final int TEST_VALUE = 3;
    private static final int TEST_VALUE_2 = 12;

    private MultiValueMap<String, Integer> multiValueMap;

    @BeforeEach
    public void setUp() {
        multiValueMap = new MultiValueMap<>();
    }

    @Test
    public void shouldPutValue_ifKeyHasNoValues() {
        multiValueMap.putValue(TEST_KEY, TEST_VALUE);

        List<Integer> values = multiValueMap.get(TEST_KEY);
        assertEquals(1, values.size());
        assertTrue(values.contains(TEST_VALUE));
    }

    @Test
    public void shouldPutValue_ifKeyHasValues() {
        multiValueMap.putValue(TEST_KEY, TEST_VALUE);
        multiValueMap.putValue(TEST_KEY, TEST_VALUE_2);

        List<Integer> values = multiValueMap.get(TEST_KEY);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(List.of(TEST_VALUE, TEST_VALUE_2)));
    }

    @Test
    public void shouldRemoveValue_ifKeyHasNoValues_andReturnFalse() {
        assertFalse(multiValueMap.removeValue(TEST_KEY, TEST_VALUE));
    }

    @Test
    public void shouldRemoveValue_ifKeyHasValues_andReturnTrue() {
        multiValueMap.putValue(TEST_KEY, TEST_VALUE);
        multiValueMap.putValue(TEST_KEY, TEST_VALUE_2);

        boolean removed = multiValueMap.removeValue(TEST_KEY, TEST_VALUE);
        List<Integer> values = multiValueMap.get(TEST_KEY);

        assertTrue(removed);
        assertEquals(1, values.size());
        assertTrue(values.contains(TEST_VALUE_2));
    }

    @Test
    public void shouldGetEmptyList_whenKeyHasNoValues() {
        assertTrue(multiValueMap.getOrEmptyList(TEST_KEY).isEmpty());
    }

    @Test
    public void shouldGetValues_whenKeyHasValues() {
        multiValueMap.putValue(TEST_KEY, TEST_VALUE);
        multiValueMap.putValue(TEST_KEY, TEST_VALUE_2);

        List<Integer> values = multiValueMap.getOrEmptyList(TEST_KEY);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(List.of(TEST_VALUE, TEST_VALUE_2)));
    }

}
