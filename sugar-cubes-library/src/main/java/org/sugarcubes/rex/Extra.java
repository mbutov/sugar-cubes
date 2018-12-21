package org.sugarcubes.rex;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
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
 *              .map(SQLException.class, DataAccessException::new)
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
public class Extra<K extends Throwable, V extends Throwable> implements Function<K, V> {

    public static final Function<Throwable, Throwable> NOT_SUPPORTED = ex -> {
        throw new IllegalArgumentException("Cannot translate " + ex);
    };

    private final Map<Class<K>, Function<K, V>> translators = new LinkedHashMap<>();
    private final Function<K, V> defaultTranslator;

    public Extra(Function<K, V> defaultTranslator) {
        this.defaultTranslator = defaultTranslator;
    }

    public Extra() {
        this((Function) NOT_SUPPORTED);
    }

    public <T extends K> Extra<K, V> map(Class<T> type, Function<T, V> function) {
        Optional<Class<K>> existing = translators.keySet().stream().filter(t -> t.isAssignableFrom(type)).findAny();
        if (existing.isPresent()) {
            throw new IllegalArgumentException(String.format("Translator already contains %s, cannot add %s", existing.get(), type));
        }
        translators.put((Class) type, (Function) function);
        return this;
    }

    @Override
    public V apply(K k) {
        Function<K, V> translator =
            translators.entrySet().stream()
                .filter(e -> e.getKey().isInstance(k))
                .findAny()
                .map(Map.Entry::getValue)
                .orElse(defaultTranslator);
        return translator.apply(k);
    }

}
