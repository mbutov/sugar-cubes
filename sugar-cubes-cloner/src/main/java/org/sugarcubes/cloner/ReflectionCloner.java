package org.sugarcubes.cloner;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Implementation of {@link Cloner} which copies objects fields using Reflection API.
 *
 * @author Maxim Butov
 */
public class ReflectionCloner extends AbstractCloner {

    /**
     * Object factory.
     */
    private final ObjectFactory objectFactory;

    /**
     * Copy of {@link Modifier#SYNTHETIC} value.
     */
    private static final int SYNTHETIC_MODIFIER = 0x00001000;

    /**
     * Set of modifiers which are excluded when copying.
     */
    private int excludedModifiers = Modifier.STATIC | Modifier.TRANSIENT;

    /**
     * Cache with (class, fields to copy) entries.
     */
    private final Map<Class, Collection<Field>> fieldCache = new WeakHashMap<>();

    /**
     * Default constructor. Uses {@link ObjenesisObjectFactory} if Objenesis library is present, or
     * {@link ReflectionObjectFactory} oterwise.
     */
    public ReflectionCloner() {
        this(ObjenesisUtils.isObjenesisAvailable() ? new ObjenesisObjectFactory() : new ReflectionObjectFactory());
    }

    /**
     * Creates an instance with the specified object factory.
     *
     * @param objectFactory object factory to use
     */
    public ReflectionCloner(ObjectFactory objectFactory) {
        Objects.requireNonNull(objectFactory, "objectFactory is null");
        this.objectFactory = objectFactory;
    }

    /**
     * @return the object factory
     */
    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    /**
     * Checks if the modifier is excluded from copying.
     *
     * @param modifier modifier to check, should be one of {@link Modifier} constant
     *
     * @return true if the modifier excluded
     *
     * @see #isFinalExcluded()
     * @see #isTransientExcluded()
     * @see #isSyntheticExcluded()
     */
    public boolean isModifierExcluded(int modifier) {
        return (excludedModifiers & modifier) == modifier;
    }

    /**
     * Sets the modifier excluded (so the fields with this modifier will not be copied) or not.
     *
     * @param modifier modifier, one of {@link Modifier} constant
     * @param value true to exclude, false to include
     *
     * @see #setFinalExcluded(boolean)
     * @see #setTransientExcluded(boolean)
     * @see #setSyntheticExcluded(boolean)
     */
    public void setModifierExcluded(int modifier, boolean value) {
        if (value) {
            excludedModifiers |= modifier;
        }
        else {
            excludedModifiers &= ~modifier;
        }
        fieldCache.clear();
    }

    /**
     * @return true if the final fields are not copied
     */
    public boolean isFinalExcluded() {
        return isModifierExcluded(Modifier.FINAL);
    }

    /**
     * Sets the exclusion mode for final fields.
     *
     * @param finalExcluded true to exclude final fields, false to include
     */
    public void setFinalExcluded(boolean finalExcluded) {
        setModifierExcluded(Modifier.FINAL, finalExcluded);
    }

    /**
     * @return true if the transient fields are not copied
     */
    public boolean isTransientExcluded() {
        return isModifierExcluded(Modifier.TRANSIENT);
    }

    /**
     * Sets the exclusion mode for transient fields.
     *
     * @param transientExcluded true to exclude final fields, false to include
     */
    public void setTransientExcluded(boolean transientExcluded) {
        setModifierExcluded(Modifier.TRANSIENT, transientExcluded);
    }

    /**
     * @return true if the synthetic fields are not copied
     */
    public boolean isSyntheticExcluded() {
        return isModifierExcluded(SYNTHETIC_MODIFIER);
    }

    /**
     * Sets the exclusion mode for synthetic fields.
     *
     * @param syntheticExcluded true to exclude final fields, false to include
     */
    public void setSyntheticExcluded(boolean syntheticExcluded) {
        setModifierExcluded(SYNTHETIC_MODIFIER, syntheticExcluded);
    }

    @Override
    protected Object doClone(Object object) {
        return doClone(object, new IdentityHashMap());
    }

    /**
     * Deep-clones the object.
     *
     * @param object object to clone
     * @param cloned map of previously cloned objects, we need it to avoid cloning one object several times
     * and to break cycles in object graph
     *
     * @return clone
     */
    protected Object doClone(Object object, Map cloned) {
        if (object == null || isImmutable(object.getClass())) {
            return object;
        }
        else {
            Object clone = cloned.get(object);
            if (clone == null) {
                clone = object.getClass().isArray() ? doCloneArray(object, cloned) : doCloneObject(object, cloned);
            }
            return clone;
        }
    }

    /**
     * Clones the array.
     * @param object array
     * @param cloned map of previously cloned objects
     * @return clone
     */
    protected Object doCloneArray(Object object, Map cloned) {
        Class componentType = object.getClass().getComponentType();
        int length = Array.getLength(object);
        Object clone = Array.newInstance(componentType, length);
        cloned.put(object, clone);
        if (isImmutable(componentType)) {
            System.arraycopy(object, 0, clone, 0, length);
        }
        else {
            Object[] sourceArray = (Object[]) object;
            Object[] cloneArray = (Object[]) clone;
            for (int k = 0; k < length; k++) {
                cloneArray[k] = doClone(sourceArray[k], cloned);
            }
        }
        return clone;
    }

    /**
     * Clones non-array object.
     * @param object object
     * @param cloned map of previously cloned objects
     * @return clone
     */
    protected Object doCloneObject(Object object, Map cloned) {
        Object clone = getObjectFactory().newInstance(object.getClass());
        cloned.put(object, clone);
        copyFields(object, clone, cloned);
        return clone;
    }

    protected void copyFields(Object from, Object into, Map cloned) {
        for (Field field : getCopyableFields(from.getClass())) {
            copyField(from, into, field, cloned);
        }
    }

    protected boolean shouldBeExcluded(int modifiers) {
        return (excludedModifiers & modifiers) != 0;
    }

    protected Collection<Field> getCopyableFields(Class clazz) {
        Collection<Field> fields = fieldCache.get(clazz);
        if (fields == null) {
            fields = new ArrayList<>();
            for (Class c = clazz; c != null; c = c.getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    int modifiers = field.getModifiers();
                    if (!shouldBeExcluded(modifiers)) {
                        setWritable(field, modifiers);
                        fields.add(field);
                    }
                }
            }
            fieldCache.put(clazz, fields);
        }
        return fields;
    }

    private static final Field MODIFIERS_FIELD;

    static {
        try {
            MODIFIERS_FIELD = Field.class.getDeclaredField("modifiers");
        }
        catch (NoSuchFieldException e) {
            throw new ClonerException("java.lang.reflect.Field.modifiers not found", e);
        }
        MODIFIERS_FIELD.setAccessible(true);
    }

    protected void setWritable(Field field, int modifiers) {
        field.setAccessible(true);
        if (Modifier.isFinal(modifiers)) {
            try {
                MODIFIERS_FIELD.set(field, ~Modifier.FINAL & modifiers);
            }
            catch (IllegalAccessException e) {
                throw new ClonerException(e);
            }
        }
    }

    protected void copyField(Object from, Object into, Field field, Map cloned) {
        try {
            Object value = field.get(from);
            Object valueClone = isImmutable(field.getType()) ? value : doClone(value, cloned);
            field.set(into, valueClone);
        }
        catch (IllegalAccessException e) {
            throw new ClonerException(e);
        }
    }

}
