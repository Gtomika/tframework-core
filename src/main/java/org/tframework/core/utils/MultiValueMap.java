/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A simple wrapper around the map collection that allows to store multiple items
 * with the same key.
 * @param <K> Type of keys.
 * @param <V> Type of values.
 */
public class MultiValueMap<K, V> extends HashMap<K, List<V>> {

    /**
     * Adds a value to the given key. Unlike standard {@link java.util.Map#put(Object, Object)}, this implementation
     * can store multiple values for this key.
     */
    public void putValue(K key, V value) {
        var currentItems = getOrEmptyList(key);
        currentItems.add(value);
        put(key, currentItems);
    }

    /**
     * Removes a value from the given key.
     * @return True if the value was present, false if not.
     */
    public boolean removeValue(K key, V value) {
        var currentItems = getOrEmptyList(key);
        return currentItems.remove(value);
    }

    /**
     * Finds the values associated with the given key, or an empty list of there are
     * no values for this key.
     * @return List of values. Unlike {@link java.util.Map#get(Object)}, this will never return null.
     */
    public List<V> getOrEmptyList(K key) {
        return Optional.ofNullable(get(key)).orElse(new LinkedList<>());
    }

}
