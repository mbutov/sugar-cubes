package org.sugarcubes.cloner;

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
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.sugarcubes.builder.collection.SetBuilder;
import org.sugarcubes.rex.Rex;
import static org.sugarcubes.rex.Rex.withMessage;

/**
 * Base abstract class for cloners.
 *
 * @author Maxim Butov
 */
public abstract class AbstractCloner implements Cloner {

    @Override
    public <T> T clone(T object) {
        if (object == null || isImmutable(object.getClass())) {
            return object;
        }

        try {
            return (T) doClone(object);
        }
        catch (Throwable e) {
            throw Rex.of(e)
                .rethrowIfError()
                .rethrowIf(ClonerException.class)
                .rethrow(withMessage("Unexpected error", ClonerException::new));
        }
    }

    /**
     * Some immutable classes.
     */
    private static final Set<Class> IMMUTABLE_CLASSES = SetBuilder.<Class>hashSet()
        .addAll(new Class[] {
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
        .transform(Collections::unmodifiableSet)
        .build();

    /**
     * Checks whether the object of class is immutable (i.e. may be cloned by reference).
     *
     * @param clazz class to check
     * @return true if the object of the class is immutable
     */
    protected boolean isImmutable(Class clazz) {
        return clazz.isPrimitive() || clazz.isEnum() || IMMUTABLE_CLASSES.contains(clazz);
    }

    /**
     * Performs cloning of the object.
     *
     * @param object object to clone, not null
     * @return clone of the object
     */
    protected abstract Object doClone(Object object) throws Throwable;

}
