package org.sugarcubes.primitive;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.sugarcubes.builder.collection.MapBuilder;
import org.sugarcubes.builder.collection.SetBuilder;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class PrimitiveTypes {

    public static final PrimitiveTypeDescriptor<Boolean> BOOLEAN = new PrimitiveTypeDescriptor<>(Boolean.class);
    public static final PrimitiveTypeDescriptor<Byte> BYTE = new PrimitiveTypeDescriptor<>(Byte.class);
    public static final PrimitiveTypeDescriptor<Character> CHARACTER = new PrimitiveTypeDescriptor<>(Character.class);
    public static final PrimitiveTypeDescriptor<Short> SHORT = new PrimitiveTypeDescriptor<>(Short.class);
    public static final PrimitiveTypeDescriptor<Integer> INTEGER = new PrimitiveTypeDescriptor<>(Integer.class);
    public static final PrimitiveTypeDescriptor<Long> LONG = new PrimitiveTypeDescriptor<>(Long.class);
    public static final PrimitiveTypeDescriptor<Float> FLOAT = new PrimitiveTypeDescriptor<>(Float.class);
    public static final PrimitiveTypeDescriptor<Double> DOUBLE = new PrimitiveTypeDescriptor<>(Double.class);

    public static final Set<PrimitiveTypeDescriptor<?>> TYPES = SetBuilder.<PrimitiveTypeDescriptor<?>>hashSet()
        .addAll(BOOLEAN, BYTE, CHARACTER, SHORT, INTEGER, LONG, FLOAT, DOUBLE)
        .transform(Collections::unmodifiableSet)
        .build();

    public static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPERS = MapBuilder.<Class<?>, Class<?>>hashMap()
        .putAll(TYPES.stream()
            .collect(Collectors.toMap(PrimitiveTypeDescriptor::getPrimitiveType, PrimitiveTypeDescriptor::getWrapperType)))
        .transform(Collections::unmodifiableMap)
        .build();

    public static final Map<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVE = MapBuilder.<Class<?>, Class<?>>hashMap()
        .putAll(TYPES.stream()
            .collect(Collectors.toMap(PrimitiveTypeDescriptor::getWrapperType, PrimitiveTypeDescriptor::getPrimitiveType)))
        .transform(Collections::unmodifiableMap)
        .build();

    public static Class<?> getPrimitiveType(Class<?> wrapperType) {

    }

}
