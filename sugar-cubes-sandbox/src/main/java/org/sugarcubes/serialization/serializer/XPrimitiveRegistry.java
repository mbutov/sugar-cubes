package org.sugarcubes.serialization.serializer;

import java.util.Collections;
import java.util.Set;

import org.sugarcubes.builder.collection.SetBuilder;
import org.sugarcubes.reflection.XReflection;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XPrimitiveRegistry {

    

    public static final Set<Class<?>> WRAPPERS = SetBuilder.<Class<?>>hashSet().addAll(
        Boolean.class,
        Byte.class,
        Short.class,
        Character.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class
    ).transform(Collections::unmodifiableSet).build();

    public static final Set<Class<?>> PRIMITIVES = SetBuilder.<Class<?>>hashSet().addAll(
        WRAPPERS.stream()
            .map(wrapperClass -> XReflection.of(wrapperClass).<Class<?>>getDeclaredField("TYPE").get(null))
    ).transform(Collections::unmodifiableSet).build();


}
