package org.sugarcubes.cloner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Object factory which uses no-arg constructor to create object.
 *
 * @author Maxim Butov
 */
public class ReflectionObjectFactory implements ObjectFactory {

    /**
     * Weak cache for default constructors.
     */
    private final Map<Class, Constructor> constructorCache = new WeakHashMap<>();

    /**
     * Returns no-arg constructor (visible or not) for the class.
     *
     * @param clazz class
     * @return no-arg constructor
     */
    private <T> Constructor<T> getNoArgConstructor(Class<T> clazz) {
        Constructor<T> constructor = constructorCache.get(clazz);
        if (constructor == null) {
            try {
                constructor = clazz.getDeclaredConstructor();
            }
            catch (NoSuchMethodException e) {
                throw new ClonerException("There's no no-arg constructor for the " + clazz, e);
            }
            constructor.setAccessible(true);
            constructorCache.put(clazz, constructor);
        }
        return constructor;
    }

    @Override
    public <T> T newInstance(Class<T> clazz) {
        try {
            return getNoArgConstructor(clazz).newInstance();
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ClonerException(e);
        }
    }

}
