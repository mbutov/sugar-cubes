package org.sugarcubes.lambda.serializable;

import java.io.Serializable;

import org.sugarcubes.lambda.ThreeConsumer;

/**
 * {@link ThreeConsumer} + {@link Serializable}
 *
 * @author Maxim Butov
 */
public interface SThreeConsumer<T, U, V> extends ThreeConsumer<T, U, V>, Serializable {

}
