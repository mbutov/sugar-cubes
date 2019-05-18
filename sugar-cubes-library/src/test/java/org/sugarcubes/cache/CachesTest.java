package org.sugarcubes.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests {@link WeakKeysCaches} checking them to dispose entries.
 *
 * @author Maxim Butov
 */
public class CachesTest {

    @Test
    public void testCaches() {
        testCache(false, WeakKeysCaches.softValues());
        testCache(true, WeakKeysCaches.softValues());
        testCache(false, WeakKeysCaches.weakValues());
        testCache(true, WeakKeysCaches.weakValues());
    }

    private void testCache(boolean keepKeys, Map<Object, Object> cache) {

        List<Object> keys = new ArrayList<>();
        String key0 = null;

        for (int n = 0; ; n++) {
            String key = "Key " + n;
            if (key0 == null) {
                key0 = new String(key.toCharArray());
            }
            if (keepKeys) {
                keys.add(key);
            }
            cache.put(key, new byte[1024 * 1024]);
            if (cache.get(key0) == null) {
                break;
            }
        }

    }

}
