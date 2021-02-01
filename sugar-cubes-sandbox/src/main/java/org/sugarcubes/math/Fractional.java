package org.sugarcubes.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

/**
 * @author Maxim Butov
 */
public class Fractional extends Number implements Comparable<Fractional> {

    public static final Fractional ZERO = new Fractional(BigInteger.ZERO, BigInteger.ONE);
    public static final Fractional ONE = new Fractional(BigInteger.ONE, BigInteger.ONE);

    public static Fractional of(BigInteger numerator, BigInteger denominator) {
        int denominatorSignum = denominator.signum();
        if (denominatorSignum == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (numerator.signum() == 0) {
            return ZERO;
        }
        if (denominatorSignum < 0) {
            return of(numerator.negate(), denominator.negate());
        }
        BigInteger gcd = denominator.gcd(numerator);
        return BigInteger.ONE.equals(gcd) ?
            new Fractional(numerator, denominator) :
            new Fractional(numerator.divide(gcd), denominator.divide(gcd));
    }

    public static Fractional of(BigInteger integer) {
        return new Fractional(integer, BigInteger.ONE);
    }

    public static Fractional of(long numerator, long denominator) {
        return of(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static Fractional of(long integer) {
        return of(BigInteger.valueOf(integer));
    }

    public static Fractional parse(String value) {
        return parse(value, 10);
    }

    public static Fractional parse(String value, int radix) {
        int index = value.indexOf('/');
        return index < 0 ?
            of(new BigInteger(value, radix)) :
            of(new BigInteger(value.substring(0, index), radix), new BigInteger(value.substring(index + 1), radix));
    }

    private final BigInteger numerator;
    private final BigInteger denominator;

    private Fractional(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public BigInteger getWholePart() {
        return numerator.divide(denominator);
    }

    public int signum() {
        return numerator.signum();
    }

    public Fractional negate() {
        return new Fractional(numerator.negate(), denominator);
    }

    public Fractional abs() {
        return signum() < 0 ? negate() : this;
    }

    public Fractional add(Fractional that) {
        return of(
            numerator.multiply(that.denominator).add(that.numerator.multiply(denominator)),
            denominator.multiply(that.denominator)
        );
    }

    public Fractional subtract(Fractional that) {
        return add(that.negate());
    }

    public Fractional reverse() {
        return of(denominator, numerator);
    }

    public Fractional multiply(Fractional that) {
        return of(numerator.multiply(that.numerator), denominator.multiply(that.denominator));
    }

    public Fractional divide(Fractional that) {
        return multiply(that.reverse());
    }

    public Fractional pow(int exponent) {
        return new Fractional(numerator.pow(exponent), denominator.pow(exponent));
    }

    @Override
    public int compareTo(Fractional that) {
        return subtract(that).signum();
    }

    @Override
    public int intValue() {
        return getWholePart().intValue();
    }

    @Override
    public long longValue() {
        return getWholePart().longValue();
    }

    @Override
    public float floatValue() {
        return toDecimal(MathContext.DECIMAL32).floatValue();
    }

    @Override
    public double doubleValue() {
        return toDecimal(MathContext.DECIMAL64).doubleValue();
    }

    public BigDecimal toDecimal(MathContext mc) {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), mc);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Fractional)) {
            return false;
        }
        Fractional that = (Fractional) obj;
        return numerator.equals(that.numerator) && denominator.equals(that.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    @Override
    public String toString() {
        return String.format("%s/%s", numerator, denominator);
    }

}
