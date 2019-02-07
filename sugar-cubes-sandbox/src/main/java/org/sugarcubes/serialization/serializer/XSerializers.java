package org.sugarcubes.serialization.serializer;

import java.util.Map;

import org.sugarcubes.builder.collection.MapBuilder;

/**
 * @author Maxim Butov
 */
public class XSerializers {

    private static final Map<Character, XSerializer<?>> SERIALIZERS = MapBuilder.<Character, XSerializer<?>>linkedHashMap()

        .put('N', new XNullSerializer())
        .put('R', new XReferenceSerializer())
        .put('S', new XStringSerializer())
        
        .build();

}
