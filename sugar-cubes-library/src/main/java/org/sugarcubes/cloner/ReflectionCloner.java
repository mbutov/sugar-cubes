package org.sugarcubes.cloner;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

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
     * Set of modifiers which are excluded when copying.
     */
    private int excludedModifiers = Modifier.STATIC;

    /**
     * Cache with (class, fields to copy) entries.
     */
    private final Map<Class, Collection<Field>> fieldCache = new WeakHashMap<>();

    /**
     * Default constructor. Uses {@link ObjenesisObjectFactory} if Objenesis library is present, or
     * {@link ReflectionObjectFactory} otherwise.
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
     * Sets the modifiers excluded (so the fields with this modifiers will not be copied).
     * This method clears previously set modifiers.
     * Example: {@code Cloner cloner = new ReflectionCloner().excludeOnly(Modifier.STATIC | Modifier.TRANSIENT); }
     *
     * @param modifiers modifier, combination of {@link Modifier} constants
     */
    public ReflectionCloner excludeOnly(int modifiers) {
        excludedModifiers = modifiers;
        return this;
    }

    /**
     * Sets the modifier excluded (so the fields with this modifier will not be copied).
     * This method does not clear previously set modifiers.
     * Example: {@code Cloner cloner = new ReflectionCloner().exclude(Modifier.STATIC).exclude(Modifier.TRANSIENT); }
     *
     * @param modifier modifier, one of {@link Modifier} constant
     */
    public ReflectionCloner exclude(int modifier) {
        return excludeOnly(excludedModifiers | modifier);
    }

    @Override
    protected Object doClone(Object object) {
        return doClone(object, new IdentityHashMap<>());
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
    protected Object doClone(Object object, Map<Object, Object> cloned) {
        if (object == null || isImmutable(object.getClass())) {
            return object;
        }
        else {
            return cloned.computeIfAbsent(object, obj -> doCloneObjectOrArray(obj, cloned));
        }
    }

    protected Object doCloneObjectOrArray(Object object, Map<Object, Object> cloned) {
        return object.getClass().isArray() ? doCloneArray(object, cloned) : doCloneObject(object, cloned);
    }

    /**
     * Clones the array.
     *
     * @param object array
     * @param cloned map of previously cloned objects
     *
     * @return clone
     */
    protected Object doCloneArray(Object object, Map<Object, Object> cloned) {
        Class componentType = object.getClass().getComponentType();
        int length = Array.getLength(object);
        Object clone = Array.newInstance(componentType, length);
        cloned.put(object, clone);
        if (isImmutable(componentType)) {
            System.arraycopy(object, 0, clone, 0, length);
        }
        else {
            for (int k = 0; k < length; k++) {
                Array.set(clone, k, doClone(Array.get(object, k), cloned));
            }
        }
        return clone;
    }

    /**
     * Clones non-array object.
     *
     * @param object object
     * @param cloned map of previously cloned objects
     *
     * @return clone
     */
    protected Object doCloneObject(Object object, Map<Object, Object> cloned) {
        Object clone = getObjectFactory().newInstance(object.getClass());
        cloned.put(object, clone);
        copyFields(object, clone, cloned);
        return clone;
    }

    protected void copyFields(Object from, Object into, Map<Object, Object> cloned) {
        for (Field field : getCopyableFields(from.getClass())) {
            copyField(from, into, field, cloned);
        }
    }

    protected void copyField(Object from, Object into, Field field, Map<Object, Object> cloned) {
        try {
            Object value = field.get(from);
            Object valueClone = isImmutable(field.getType()) ? value : doClone(value, cloned);
            field.set(into, valueClone);
        }
        catch (IllegalAccessException e) {
            throw new ClonerException(e);
        }
    }

    protected boolean isCopyable(Field field) {
        return (field.getModifiers() & excludedModifiers) == 0;
    }

    protected Collection<Field> getCopyableFields(Class clazz) {
        return fieldCache.computeIfAbsent(clazz, this::findCopyableFields);
    }

    protected List<Field> findCopyableFields(Class clazz) {
        List<Class> inheritance = new ArrayList<>();
        for (Class c = clazz; c != null; c = c.getSuperclass()) {
            inheritance.add(c);
        }
        List<Field> fields = inheritance.stream()
            .map(Class::getDeclaredFields)
            .flatMap(Arrays::stream)
            .filter(this::isCopyable)
            .collect(Collectors.toList());
        fields.forEach(this::setWritable);
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

    protected void setWritable(Field field) {
        field.setAccessible(true);
        int modifiers = field.getModifiers();
        if (Modifier.isFinal(modifiers)) {
            try {
                MODIFIERS_FIELD.set(field, ~Modifier.FINAL & modifiers);
            }
            catch (IllegalAccessException e) {
                throw new ClonerException(e);
            }
        }
    }

}
