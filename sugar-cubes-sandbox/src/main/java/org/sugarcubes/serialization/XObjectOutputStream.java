package org.sugarcubes.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.sugarcubes.builder.collection.SetBuilder;
import org.sugarcubes.serialization.serializer.XSerializers;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectOutputStream extends DataOutputStream {

    public XObjectOutputStream(OutputStream out) {
        super(out);
    }

    private final Map<Object, Integer> objects = new IdentityHashMap<>();
    private final Map<Object, Integer> immutables = new HashMap<>();

    private final Set<Class<?>> immutableClasses = SetBuilder.<Class<?>>hashSet()
        .addAll(new Class[] {
            Class.class,

            Long.class, Double.class,

            String.class,

            BigInteger.class, BigDecimal.class,

            Duration.class, Instant.class, LocalDate.class, LocalDateTime.class, LocalTime.class, MonthDay.class,
            OffsetDateTime.class, OffsetTime.class, Period.class, Year.class, YearMonth.class,
            ZonedDateTime.class, ZoneOffset.class,

            URI.class, URL.class,

            UUID.class,

            Pattern.class,
        })
        .build();

    private boolean isImmutable(Object object) {
        return immutableClasses.contains(object.getClass());
    }

    private Integer findReference(Object object) {
        Integer ref = objects.get(object);
        if (ref == null) {
            ref = immutables.get(object);
        }
        return ref;
    }

    private int addReference(Object object) {
        int ref = objects.size();
        objects.put(object, ref);
        if (isImmutable(object)) {
            immutables.put(object, ref);
        }
        return ref;
    }

    private void writeTag(char tag) throws IOException {
        write(String.valueOf(tag).getBytes(StandardCharsets.UTF_8));
    }

    public void writeObject(Object object) throws IOException {
        if (object == null) {
            writeTag(XSerializers.NULL);
            return;
        }
        Integer ref = findReference(object);
        if (ref != null) {
            writeTag(XSerializers.REFERENCE);
            writeInt(ref);
            return;
        }
        addReference(object);
        for (Map.Entry<Character, XSerializer> entry : XSerializers.SERIALIZERS.entrySet()) {
            XSerializer serializer = entry.getValue();
            if (serializer.matches(this, object)) {
                writeTag(entry.getKey());
                serializer.writeValue(this, object);
                return;
            }
        }
        throw new IllegalStateException();
    }

}
