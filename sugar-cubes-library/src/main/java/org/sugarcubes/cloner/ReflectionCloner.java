package org.sugarcubes.cloner;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.sugarcubes.arg.Arg;
import org.sugarcubes.builder.collection.SetBuilder;
import org.sugarcubes.reflection.XField;
import org.sugarcubes.reflection.XReflection;

/**
 * Implementation of {@link Cloner} which copies objects fields using Reflection API.
 *
 * @author Maxim Butov
 */
public class ReflectionCloner extends AbstractCloner {

    /**
     * Object factory.
     */
    private final ClonerObjectFactory objectFactory;

    /**
     * Set of fields modifiers which are excluded when copying.
     */
    private int excludedModifiers = Modifier.STATIC;

    /**
     * Some immutable classes.
     */
    private final Set<Class> immutableClasses = SetBuilder.<Class>hashSet()
        .addAll(new Class[] {
            Class.class,

            String.class,

            Boolean.class, Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class,

            BigInteger.class, BigDecimal.class,

            Duration.class, Instant.class, LocalDate.class, LocalDateTime.class, LocalTime.class, MonthDay.class,
            OffsetDateTime.class, OffsetTime.class, Period.class, Year.class, YearMonth.class,
            ZonedDateTime.class, ZoneOffset.class,

            URI.class, URL.class,

            UUID.class,

            Pattern.class,
        })
        .build();

    /**
     * Cache with (class, fields to copy) entries.
     */
    private final Map<Class, Collection<XField<?>>> fieldCache = new WeakHashMap<>();

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
    public ReflectionCloner(ClonerObjectFactory objectFactory) {
        Arg.notNull(objectFactory, "objectFactory is null");
        this.objectFactory = objectFactory;
    }

    /**
     * @return the object factory
     */
    public ClonerObjectFactory getObjectFactory() {
        return objectFactory;
    }

    /**
     * Sets the modifiers excluded (so the fields with this modifiers will not be copied).
     * This method clears previously set modifiers.
     * Example: {@code Cloner cloner = new ReflectionClonerClassic().excludeOnly(Modifier.STATIC | Modifier.TRANSIENT); }
     *
     * @param modifiers modifier, combination of {@link Modifier} constants
     *
     * @return same cloner
     */
    public ReflectionCloner excludeOnly(int modifiers) {
        excludedModifiers = modifiers;
        return this;
    }

    /**
     * Sets the modifier excluded (so the fields with this modifier will not be copied).
     * This method does not clear previously set modifiers.
     * Example: {@code Cloner cloner = new ReflectionClonerClassic().exclude(Modifier.STATIC).exclude(Modifier.TRANSIENT); }
     *
     * @param modifier modifier, one of {@link Modifier} constant
     *
     * @return same cloner
     */
    public ReflectionCloner exclude(int modifier) {
        return excludeOnly(excludedModifiers | modifier);
    }

    /**
     * Register immutable types which will not be cloned and just copied via reference.
     *
     * @param type immutable type
     * @param others immutable types
     *
     * @return same cloner
     */
    public ReflectionCloner withImmutable(Class<?> type, Class<?>... others) {
        immutableClasses.add(type);
        immutableClasses.addAll(Arrays.asList(others));
        return this;
    }

    /**
     * Checks whether the object of class is immutable (i.e. may be cloned by reference).
     *
     * @param clazz class to check
     * @return true if the object of the class is immutable
     */
    protected boolean isImmutable(Class clazz) {
        return clazz.isPrimitive() || clazz.isEnum() || immutableClasses.contains(clazz);
    }

    @Override
    protected Object doClone(Object object) {
        if (object == null || isImmutable(object.getClass())) {
            return object;
        }
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
        for (XField field : getCopyableFields(from.getClass())) {
            copyField(from, into, field, cloned);
        }
    }

    protected void copyField(Object from, Object into, XField<Object> field, Map<Object, Object> cloned) {
        Object value = field.get(from);
        Object valueClone = isImmutable(field.getReflectionObject().getType()) ? value : doClone(value, cloned);
        field.set(into, valueClone);
    }

    protected Collection<XField<?>> getCopyableFields(Class<?> clazz) {
        return fieldCache.computeIfAbsent(clazz, this::findCopyableFields);
    }

    protected List<XField<?>> findCopyableFields(Class<?> clazz) {
        List<XField<?>> fields = XReflection.of(clazz).getFields()
            .filter(field -> !field.isModifier(excludedModifiers))
            .map(XField::withNoFinal)
            .collect(Collectors.toList());
        return fields;
    }

}
