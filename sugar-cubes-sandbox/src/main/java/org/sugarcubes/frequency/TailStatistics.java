package org.sugarcubes.frequency;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.function.LongSupplier;

import org.sugarcubes.check.Checks;

/**
 * Класс для подсчёта среднего из последних N значений.
 *
 * @author Q-MBU
 */
public class TailStatistics {

    private final AtomicLong count = new AtomicLong(0);
    private final AtomicLong sum = new AtomicLong(0);
    private final AtomicLongArray buffer;

    private LongSupplier avg;

    public TailStatistics(int size) {
        Checks.arg().check(size > 0, "Size must be > 0.");
        buffer = new AtomicLongArray(size);
        avg = () -> {
            long count = count();
            if (count == 0) {
                return 0;
            }
            else if (count < buffer.length()) {
                return sum() / count;
            }
            else {
                avg = () -> sum() / buffer.length();
                return avg();
            }
        };
    }

    public void append(long next) {
        sum.getAndAdd(next);
        int index = (int) (count.getAndIncrement() % buffer.length());
        long prev = buffer.getAndSet(index, next);
        sum.getAndUpdate(sum -> sum - prev);
    }

    public long count() {
        return count.get();
    }

    public long sum() {
        return sum.get();
    }

    public long exactSum() {
        long sum = 0;
        for (int k = 0; k < buffer.length(); k++) {
            sum += buffer.get(k);
        }
        return sum;
    }

    public long avg() {
        return avg.getAsLong();
    }

    @Override
    public String toString() {
        return "TailStatistics{" +
            "count=" + count() +
            ",sum=" + sum() +
            ",exactSum=" + exactSum() +
            ",average=" + avg() +
            '}';
    }
}
