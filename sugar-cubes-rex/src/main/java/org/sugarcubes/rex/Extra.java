package org.sugarcubes.rex;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Extra means "exception translator".
 *
 * A helper to translate checked exceptions into unchecked.
 *
 * Can be used as following:
 *
 * <pre>
 *      Function<Throwable, RuntimeException> translator =
 *          new Extra()
 *              .map(SQLException.class, DataAccessException.class)
 *              .map(Throwable.class, DefaultRuntimeException::new);
 *
 *      try {
 *          doSomething();
 *      } catch (Exception ex) {
 *          throw Rex.of(ex, translator).throwUnchecked();
 *      }
 * </pre>
 *
 * @author Maxim Butov
 */
public class Extra implements Function<Throwable, RuntimeException> {

    /**
     * Default translator of checked exceptions into unchecked.
     */
    public static final Function<Throwable, RuntimeException> DEFAULT = RuntimeException::new;

    /**
     * Does not translate, throws an exception.
     */
    public static final Function<Throwable, RuntimeException> ERROR = ex -> {
        throw new IllegalStateException("Cannot translate " + ex);
    };

    /**
     * Returns a function which creates new instance of {@code translated} class using constructor
     * with Throwable parameter.
     *
     * @param translated class of runtime exception
     * @return translator function
     */
    public static Function<Throwable, RuntimeException> newInstance(Class<? extends RuntimeException> translated) {
        Constructor<? extends RuntimeException> constructor = Rex.call(() -> translated.getConstructor(Throwable.class));
        return ex -> Rex.call(() -> constructor.newInstance(ex));
    }

    /**
     * Default translation scenario.
     */
    private final Function<Throwable, RuntimeException> defaultScenario;

    /**
     * Ordered pairs of (exception class, translation scenario).
     */
    private final Map<Class<? extends Throwable>, Function<Throwable, RuntimeException>> translators =
        new LinkedHashMap<>();

    /**
     * Creates translator with default scenario.
     */
    public Extra() {
        this(true);
    }

    /**
     * Creates translator.
     * @param useDefault if true, then use default scenario, if false, then throw an exception when scenario not found
     */
    public Extra(boolean useDefault) {
        this(useDefault ? DEFAULT : null);
    }

    /**
     * Creates translator with default scenario.
     *
     * @param defaultScenario default scenario to apply to exception
     */
    public Extra(Function<Throwable, RuntimeException> defaultScenario) {
        this.defaultScenario = defaultScenario;
    }

    public <E extends Throwable> Extra map(Class<E> original, Function<E, RuntimeException> translator) {
        for (Map.Entry<Class<? extends Throwable>, Function<Throwable, RuntimeException>> entry : translators.entrySet()) {
            if (entry.getKey().isAssignableFrom(original)) {
                throw new IllegalArgumentException("There is already mapping for " + entry.getKey());
            }
        }
        translators.put(original, (Function) translator);
        return this;
    }

    public Extra map(Class<? extends Throwable> original, Class<? extends RuntimeException> translated) {
        return map(original, (Function) newInstance(translated));
    }

    @Override
    public RuntimeException apply(Throwable throwable) {
        for (Map.Entry<Class<? extends Throwable>, Function<Throwable, RuntimeException>> entry : translators.entrySet()) {
            if (entry.getKey().isInstance(throwable)) {
                return entry.getValue().apply(throwable);
            }
        }
        if (defaultScenario == null) {
            throw new IllegalStateException("Cannot translate " + throwable);
        }
        else {
            return defaultScenario.apply(throwable);
        }
    }

}
