package org.sugarcubes.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.sugarcubes.builder.collection.SetBuilder;

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

    public int findReference(Object object) {
        Integer ref = objects.get(object);
        if (ref == null) {
            ref = immutables.get(object);
        }
        return ref != null ? ref : -1;
    }

    public int addReference(Object object) {
        int ref = objects.size();
        objects.put(object, ref);
        if (isImmutable(object)) {
            immutables.put(object, ref);
        }
        return ref;
    }

    public void writeObject(Object object) throws IOException {
        for (XSerializer serializer : XSerializers.values()) {
            if (serializer.write(this, object)) {
                return;
            }
        }
        throw new IllegalStateException();
    }

}
