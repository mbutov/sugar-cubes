package org.sugarcubes.frequency;

import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Q-MBU
 */
public class TailStatisticsTest {

    @Test
    public void testStats() {
        TailStatistics stats = new TailStatistics(3);

        assertEquals(0, stats.count());
        assertEquals(0, stats.sum());
        assertEquals(0, stats.avg());

        stats.append(3);

        assertEquals(1, stats.count());
        assertEquals(3, stats.sum());
        assertEquals(3, stats.avg());

        stats.append(5);

        assertEquals(2, stats.count());
        assertEquals(8, stats.sum());
        assertEquals(4, stats.avg());

        stats.append(7);

        assertEquals(3, stats.count());
        assertEquals(15, stats.sum());
        assertEquals(5, stats.avg());

        stats.append(9);

        assertEquals(4, stats.count());
        assertEquals(21, stats.sum());
        assertEquals(7, stats.avg());
    }

    @Test
    public void testXxx() {
        int processors = Runtime.getRuntime().availableProcessors();
        int queueSize = 3;
        int upperBound = 100;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(processors);
        executor.initialize();

        TailStatistics statistics = new TailStatistics(queueSize);

        IntStream.range(0, processors - 1)
            .mapToObj(i -> (Runnable) () -> {
                for (int k = 0; k < 100_000_000; k++) {
                    statistics.append(RandomUtils.nextLong(0, upperBound));
                }
            })
            .forEach(executor::submit);

        int checks = 0;
        try {
            while (executor.getActiveCount() > 0) {
                Assertions.assertTrue(statistics.sum() >= 0);
                checks++;
            }
        }
        finally {
            executor.shutdown();
            System.out.println("checks = " + checks);
            System.out.println("statistics = " + statistics);
        }
    }

}
