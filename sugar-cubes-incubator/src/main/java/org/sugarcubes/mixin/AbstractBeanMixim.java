package org.sugarcubes.mixin;

import java.util.HashMap;
import java.util.Map;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class AbstractBeanMixim implements BeanMixin {

    private final Map<String, Object> properties = new HashMap<>();

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

}
