package org.sugarcubes.math;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Maxim Butov
 */
public class DecimalField extends NumberField<BigDecimal> {

    private final MathContext mathContext;

    public DecimalField(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal getZero() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getOne() {
        return BigDecimal.ONE;
    }

    @Override
    public BigDecimal add(BigDecimal left, BigDecimal right) {
        return left.add(right);
    }

    @Override
    public BigDecimal subtract(BigDecimal left, BigDecimal right) {
        return left.subtract(right);
    }

    @Override
    public BigDecimal negate(BigDecimal bigDecimal) {
        return bigDecimal.negate();
    }

    @Override
    public BigDecimal multiply(BigDecimal left, BigDecimal right) {
        return left.multiply(right);
    }

    @Override
    public BigDecimal divide(BigDecimal left, BigDecimal right) {
        return left.divide(right, mathContext);
    }

}
