package org.sugarcubes.s2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ZHandles {

    private List<Object> objects;

    public ZHandles() {
         clear();
    }

    public Object get(int index) {
        return objects.get(index);
    }

    public Consumer<Object> add() {
        return add(null);
    }

    public Consumer<Object> add(Object obj) {
        int index = objects.size();
        objects.add(obj);
        return newObj -> objects.set(index, newObj);
    }

    public void clear() {
        objects = new ArrayList<>();
    }

}
