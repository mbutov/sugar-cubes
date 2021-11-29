package org.sugarcubes.math;

/**
 * todo: document it
 *
 * @author Q-MBU
 */
public class ComplexNumberField<T extends Number> extends NumberField<ComplexNumber<T>> {

    private final NumberField<T> numberField;

    public ComplexNumberField(NumberField<T> numberField) {
        this.numberField = numberField;
    }

    public ComplexNumber<T> newComplexNumber(T real, T imaginary) {
        return new ComplexNumber<>(real, imaginary);
    }

    @Override
    public ComplexNumber<T> getZero() {
        return newComplexNumber(numberField.getZero(), numberField.getZero());
    }

    @Override
    public ComplexNumber<T> getOne() {
        return newComplexNumber(numberField.getOne(), numberField.getZero());
    }

    @Override
    public ComplexNumber<T> add(ComplexNumber<T> left, ComplexNumber<T> right) {
        return newComplexNumber(numberField.add(left.getReal(), right.getReal()),
            numberField.add(left.getImaginary(), right.getImaginary()));
    }

    @Override
    public ComplexNumber<T> subtract(ComplexNumber<T> left, ComplexNumber<T> right) {
        return newComplexNumber(numberField.subtract(left.getReal(), right.getReal()),
            numberField.subtract(left.getImaginary(), right.getImaginary()));
    }

    @Override
    public ComplexNumber<T> negate(ComplexNumber<T> tComplexNumber) {
        return newComplexNumber(numberField.negate(tComplexNumber.getReal()), numberField.negate(tComplexNumber.getImaginary()));
    }

    @Override
    public ComplexNumber<T> multiply(ComplexNumber<T> left, ComplexNumber<T> right) {
        return null;
    }

    @Override
    public ComplexNumber<T> reverse(ComplexNumber<T> tComplexNumber) {
        return super.reverse(tComplexNumber);
    }

    @Override
    public ComplexNumber<T> divide(ComplexNumber<T> left, ComplexNumber<T> right) {
        return super.divide(left, right);
    }

}
