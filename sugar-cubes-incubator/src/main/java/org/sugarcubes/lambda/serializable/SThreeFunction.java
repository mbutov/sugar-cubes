package org.sugarcubes.lambda.serializable;

import java.io.Serializable;

import org.sugarcubes.lambda.ThreeFunction;

/**
 * {@link ThreeFunction} + {@link Serializable}
 *
 * @author Maxim Butov
 */
public interface SThreeFunction<T, U, V, R> extends ThreeFunction<T, U, V, R>, Serializable {

}
