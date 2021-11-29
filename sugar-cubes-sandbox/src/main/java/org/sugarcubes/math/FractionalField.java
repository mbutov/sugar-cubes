package org.sugarcubes.math;

/**
 * @author Maxim Butov
 */
public class FractionalField extends NumberField<Fractional> {

    @Override
    public Fractional getZero() {
        return Fractional.ZERO;
    }

    @Override
    public Fractional getOne() {
        return Fractional.ONE;
    }

    @Override
    public Fractional add(Fractional left, Fractional right) {
        return left.add(right);
    }

    @Override
    public Fractional subtract(Fractional left, Fractional right) {
        return left.subtract(right);
    }

    @Override
    public Fractional negate(Fractional fractional) {
        return fractional.negate();
    }

    @Override
    public Fractional multiply(Fractional left, Fractional right) {
        return left.multiply(right);
    }

    @Override
    public Fractional reverse(Fractional fractional) {
        return fractional.reverse();
    }

    @Override
    public Fractional divide(Fractional left, Fractional right) {
        return left.divide(right);
    }

}
