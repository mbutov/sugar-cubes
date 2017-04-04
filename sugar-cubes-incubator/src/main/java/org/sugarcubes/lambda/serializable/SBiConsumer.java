package org.sugarcubes.lambda.serializable;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * {@link BiConsumer} + {@link Serializable}
 *
 * @author Maxim Butov
 */
public interface SBiConsumer<T, U> extends BiConsumer<T, U>, Serializable {

}
