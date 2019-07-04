package org.sugarcubes.primitive;

/**
 * Extension of {@link XPrimitive} where primitive type is number.
 *
 * @author Maxim Butov
 */
public abstract class XPrimitiveNumber<W extends Number, A> extends XPrimitive<W, A> {

    XPrimitiveNumber() {
    }

    public abstract W cast(Number value);

}
