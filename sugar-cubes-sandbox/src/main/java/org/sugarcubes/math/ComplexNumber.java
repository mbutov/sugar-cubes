package org.sugarcubes.math;

/**
 * todo: document it
 *
 * @author Q-MBU
 */
public class ComplexNumber<T extends Number> {

    private final T real;
    private final T imaginary;

    public ComplexNumber(T real, T imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public T getReal() {
        return real;
    }

    public T getImaginary() {
        return imaginary;
    }

}
