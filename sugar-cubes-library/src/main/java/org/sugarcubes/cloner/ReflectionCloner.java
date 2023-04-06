package org.sugarcubes.cloner;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sugarcubes.builder.collection.SetBuilder;
import org.sugarcubes.check.Checks;
import org.sugarcubes.reflection.XField;
import org.sugarcubes.reflection.XMethod;
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

        Checks.arg().notNull(objectFactory, "objectFactory is null");
        this.objectFactory = objectFactory;

        initialize();

    }

    /**
     * @return the object factory
     */
    public ClonerObjectFactory getObjectFactory() {
        return objectFactory;
    }

    /**
     * Some immutable classes.
     */
    private static final Set<Class<?>> DEFAULT_IMMUTABLE_CLASSES = SetBuilder.unmodifiableHashSet(new Class[] {
        Class.class,

        Boolean.class, Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class,

        String.class,

        BigInteger.class, BigDecimal.class,

        Duration.class, Instant.class, LocalDate.class, LocalDateTime.class, LocalTime.class, MonthDay.class,
        OffsetDateTime.class, OffsetTime.class, Period.class, Year.class, YearMonth.class,
        ZonedDateTime.class, ZoneOffset.class,

        URI.class, URL.class,

        UUID.class,

        Pattern.class,
    });

    protected void initialize() {

        skipModifiers(Modifier.STATIC);

        immutableClasses(isAnyOf(DEFAULT_IMMUTABLE_CLASSES));
        immutableClasses(Class::isEnum);

    }

    /**
     * Fields to skip when cloning.
     */
    private final List<Predicate<Field>> skipFields = new ArrayList<>();

    /**
     * Returns true if the field should be skipped when cloning.
     */
    private boolean isFieldSkipped(Field field) {
        return anyMatch(skipFields, field);
    }

    /**
     * Classes which instances are cleared (set to null) when cloning.
     */
    private final List<Predicate<Class<?>>> clearClasses = new ArrayList<>();

    /**
     * Returns true if the class should be skipped when cloning.
     */
    private boolean isClassCleared(Class<?> clazz) {
        return anyMatch(clearClasses, clazz);
    }

    /**
     * Classes which objects are copied without cloning.
     */
    private final List<Predicate<Class<?>>> immutableClasses = new ArrayList<>();

    /**
     * Returns true if the class is immutable and object should not be cloned.
     */
    private boolean isClassImmutable(Class<?> clazz) {
        return anyMatch(immutableClasses, clazz);
    }

    public ReflectionCloner clearIfClassMatches(Predicate<Class<?>> classFilter) {
        this.clearClasses.add(classFilter);
        return this;
    }

    public ReflectionCloner clearExactInstanceOf(Class<?> clazz, Class<?>... classes) {
        return clearIfClassMatches(isAnyOf(asSet(clazz, classes)));
    }

    public ReflectionCloner clearInstanceOf(Class<?> clazz, Class<?>... classes) {
        return clearIfClassMatches(isSubclassOf(asSet(clazz, classes)));
    }

    public ReflectionCloner skipFields(Predicate<Field> fieldFilter) {
        this.skipFields.add(fieldFilter);
        return this;
    }

    /**
     * Sets the modifier excluded (so the fields with this modifier will not be copied).
     * Example: {@code Cloner cloner = new ReflectionCloner().skipModifiers(Modifier.TRANSIENT); }
     *
     * @param modifier modifier, one of {@link Modifier} constant
     * @param modifiers modifiers, array of {@link Modifier} constants
     *
     * @return same cloner
     *
     * @see Modifier
     */
    public ReflectionCloner skipModifiers(int modifier, int... modifiers) {
        int modifiersToExclude = IntStream.of(modifiers).reduce(modifier, (x, y) -> x | y);
        return skipFields(field -> (field.getModifiers() & modifiersToExclude) != 0);
    }

    /**
     * Adds predicate for immutable classes.
     *
     * @param predicate predicate for immutable classes
     *
     * @return same cloner
     */
    public ReflectionCloner immutableClasses(Predicate<Class<?>> predicate) {
        immutableClasses.add(predicate);
        return this;
    }

    /**
     * Register immutable types which will not be cloned and just copied via reference.
     *
     * @param classes immutable types
     *
     * @return same cloner
     */
    public ReflectionCloner immutableClasses(Class<?> clazz, Class<?>... classes) {
        return immutableClasses(isAnyOf(asSet(clazz, classes)));
    }

    @Override
    protected Object doClone(Object object) {
        return doClone(object, new IdentityHashMap<>());
    }

    private final Map<Class<?>, Boolean> clearedClassesCache = new HashMap<>();
    private final Map<Class<?>, Boolean> immutableClassesCache = new HashMap<>();

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
        if (object == null) {
            return null;
        }
        Class<?> type = object.getClass();
        if (clearedClassesCache.computeIfAbsent(type, this::isClassCleared)) {
            return null;
        }
        if (immutableClassesCache.computeIfAbsent(type, this::isClassImmutable)) {
            return object;
        }
        Object clone = cloned.get(object);
        return clone != null ? clone : doCloneObjectOrArray(object, cloned);
    }

    protected Object doCloneObjectOrArray(Object object, Map<Object, Object> cloned) {
        return object.getClass().isArray() ? doCloneArray(object, cloned) : doCloneObject(object, cloned);
    }

    private static final XMethod<Object> CLONE_METHOD = XReflection.of(Object.class).getMethod("clone");

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
        if (componentType.isPrimitive()) {
            Object clone = CLONE_METHOD.invoke(object);
            cloned.put(object, clone);
            return clone;
        }
        else {
            Object[] sourceArray = (Object[]) object;
            int length = sourceArray.length;
            Object[] targetArray = (Object[]) Array.newInstance(componentType, length);
            cloned.put(sourceArray, targetArray);
            for (int k = 0; k < length; k++) {
                targetArray[k] = doClone(sourceArray[k], cloned);
            }
            return targetArray;
        }
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
        field.set(into, doClone(field.get(from), cloned));
    }

    /**
     * Cache with (class, fields to copy) entries.
     */
    private final Map<Class, Collection<XField<?>>> copyableFieldsCache = new WeakHashMap<>();

    protected Collection<XField<?>> getCopyableFields(Class<?> clazz) {
        return copyableFieldsCache.computeIfAbsent(clazz, this::findCopyableFields);
    }

    protected List<XField<?>> findCopyableFields(Class<?> clazz) {
        List<XField<?>> fields = XReflection.of(clazz).getFields()
            .filter(field -> !isFieldSkipped(field.getReflectionObject()))
            .map(XField::withNoFinal)
            .collect(Collectors.toList());
        return fields;
    }

    private static <X> boolean anyMatch(Collection<Predicate<X>> predicates, X x) {
        return anyMatch(predicates.stream(), x);
    }

    private static <X> boolean anyMatch(Stream<Predicate<X>> predicates, X x) {
        return predicates.anyMatch(p -> p.test(x));
    }

    private static <X> Set<X> asSet(X first, X... others) {
        Checks.arg().notNull(first, "first argument must be not null");
        return SetBuilder.<X>hashSet().add(first).addAll(others).build();
    }

    private static Predicate<Class<?>> isAnyOf(Collection<Class<?>> classes) {
        return classes::contains;
    }

    private static Predicate<Class<?>> isSubclassOf(Collection<Class<?>> classes) {
        return clazz -> anyMatch(classes.stream().map(cl -> (Predicate<Class<?>>) cl::isAssignableFrom), clazz);
    }

}
