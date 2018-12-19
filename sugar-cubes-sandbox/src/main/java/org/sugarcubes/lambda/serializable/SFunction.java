package org.sugarcubes.lambda.serializable;

import java.io.Serializable;
import java.util.function.Function;

/**
 * {@link Function} + {@link Serializable}
 *
 * @author Maxim Butov
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {

}
