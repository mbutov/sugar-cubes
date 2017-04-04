package org.sugarcubes.cloner;

/**
 * Factory for creating different {@link Cloner}s.
 *
 * {@link ReflectionCloner} and {@link UnsafeReflectionCloner} each are ~100 times faster than
 * {@link SerializableCloner} and can deal with non-serializable objects.
 *
 * @author Maxim Butov
 */
public class Cloners {

    /**
     * Returns an instance of {@link ReflectionCloner} with specified object factory.
     *
     * @param objectFactory object factory
     *
     * @return {@link ReflectionCloner} instance
     *
     * @see ReflectionCloner#ReflectionCloner(ObjectFactory)
     */
    public static Cloner reflection(ObjectFactory objectFactory) {
        return new ReflectionCloner(objectFactory);
    }

    /**
     * Returns an instance of {@link ReflectionCloner} with default object factory.
     *
     * @return {@link ReflectionCloner} instance
     *
     * @see ReflectionCloner#ReflectionCloner()
     */
    public static Cloner reflection() {
        return new ReflectionCloner();
    }

    /**
     * Returns an instance of {@link SerializableCloner}.
     *
     * @return {@link SerializableCloner} instance
     */
    public static Cloner serialization() {
        return SerializableCloner.INSTANCE;
    }

    /**
     * Returns an instance of {@link UnsafeReflectionCloner}.
     *
     * @return {@link UnsafeReflectionCloner} instance
     */
    public static Cloner unsafe() {
        return UnsafeReflectionCloner.INSTANCE;
    }

}
