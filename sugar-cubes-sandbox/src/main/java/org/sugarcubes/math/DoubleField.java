package org.sugarcubes.math;

/**
 * @author Maxim Butov
 */
public class DoubleField extends NumberField<Double> {

    public static final double ZERO = 0.0;
    public static final double ONE = 1.0;

    @Override
    public Double getZero() {
        return ZERO;
    }

    @Override
    public Double getOne() {
        return ONE;
    }

    @Override
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double negate(Double t) {
        return -t;
    }

    @Override
    public Double multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double reverse(Double t) {
        return getOne() / t;
    }

    @Override
    public Double divide(Double left, Double right) {
        return left / right;
    }

}
