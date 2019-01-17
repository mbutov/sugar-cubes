package org.sugarcubes.reflection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@link Map} implementation which does not serialize its state.
 *
 * @author Maxim Butov
 */
public class XClassCache<K, V> extends HashMap<K, V> {

    private static class PersistentObject implements Serializable {

        private static final long serialVersionUID = 1L;

        private Object readResolve() {
            return new XClassCache<>();
        }

    }

    private Object writeReplace() {
        return new PersistentObject();
    }

}
