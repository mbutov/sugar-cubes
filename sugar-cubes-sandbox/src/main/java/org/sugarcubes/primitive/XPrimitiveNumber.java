package org.sugarcubes.primitive;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.sugarcubes.function.TernaryConsumer;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XPrimitiveNumber<W extends Number, A> extends XPrimitive<W, A> {

    private final Function<Number, W> fromNumber;

    public XPrimitiveNumber(Function<A, Integer> arrayLength, Function<Integer, A> arrayFactory, BiFunction<A, Integer, W> arrayGet, TernaryConsumer<A, Integer, W> arraySet, Function<Number, W> fromNumber) {
        super(arrayLength, arrayFactory, arrayGet, arraySet);
        this.fromNumber = fromNumber;
    }

    public W cast(Number value) {
        return fromNumber.apply(value);
    }

}
