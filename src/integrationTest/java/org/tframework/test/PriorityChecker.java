/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import java.util.LinkedHashSet;
import lombok.Getter;

@Getter
public class PriorityChecker {

    private final LinkedHashSet<String> actualOrder = new LinkedHashSet<>();

    public void save(String name) {
        actualOrder.add(name);
    }

}
