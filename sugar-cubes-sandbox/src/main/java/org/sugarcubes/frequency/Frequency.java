package org.sugarcubes.frequency;

import java.io.Serializable;
import java.util.Arrays;

import org.sugarcubes.check.Checks;

/**
 * Frequency representation.
 *
 * @author Maxim Butov
 */
public class Frequency implements Serializable, Comparable<Frequency> {

    public static final Frequency ZERO = new Frequency(0, 1);

    private final int count;
    private final int seconds;

    private Frequency(int count, int seconds) {
        Checks.arg().check(seconds != 0, "seconds must be != 0");
        if (seconds < 0) {
            count = -count;
            seconds = -seconds;
        }
        int gcd = gcd(Math.abs(count), seconds);
        this.count = count / gcd;
        this.seconds = seconds / gcd;
    }

    public int getCount() {
        return count;
    }

    public int getSeconds() {
        return seconds;
    }

    public static Frequency of(int count, int seconds) {
        return new Frequency(count, seconds);
    }

    public static Frequency perSecond(int count) {
        return of(count, 1);
    }

    public static Frequency perMinute(int count) {
        return of(count, 60);
    }

    public static Frequency perHour(int count) {
        return of(count, 60 * 60);
    }

    public static Frequency perDay(int count) {
        return of(count, 60 * 60 * 24);
    }

    public Frequency negate() {
        return new Frequency(-count, seconds);
    }

    private Frequency addOrSubtract(Frequency value, int sign) {
        int gcd = gcd(seconds, value.seconds);
        return of(count * (value.seconds / gcd) + sign * (value.count * (seconds / gcd)), (seconds / gcd) * value.seconds);
    }

    public Frequency add(Frequency value) {
        return addOrSubtract(value, +1);
    }

    public Frequency subtract(Frequency value) {
        return addOrSubtract(value, -1);
    }

    public Frequency multiply(int value) {
        return of(count * value, seconds);
    }

    public Frequency divide(int value) {
        return of(count, seconds * value);
    }

    public double divide(Frequency value) {
        return count * (double) value.seconds / (value.count * (double) seconds);
    }

    public int getPerSeconds(int numberOfSeconds) {
        return (int) (count * (long) numberOfSeconds / seconds);
    }

    public int getPerDay() {
        return getPerSeconds(60 * 60 * 24);
    }

    public int getPerHour() {
        return getPerSeconds(60 * 60);
    }

    public int getPerMinute() {
        return getPerSeconds(60);
    }

    public int getPerSecond() {
        return getPerSeconds(1);
    }

    @Override
    public int compareTo(Frequency that) {
        if (seconds == that.seconds) {
            return Integer.compare(count, that.count);
        }
        return Long.compare(that.seconds * (long) count, seconds * (long) that.count);
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Frequency)) {
            return false;
        }
        Frequency that = (Frequency) obj;
        return count == that.count && seconds == that.seconds;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] {count, seconds});
    }

    @Override
    public String toString() {
        return String.format("Frequency[%.3f per minute]", 60.0 * count / seconds);
    }

}
