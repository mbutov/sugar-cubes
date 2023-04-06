package org.sugarcubes.stream;

import java.util.stream.Stream;

/**
 * @author Maxim Butov
 */
public class StreamUtils {

    @SafeVarargs
    public static <T> Stream<T> concat(Stream<T>... streams) {
        return concat(streams, 0, streams.length);
    }

    private static <T> Stream<T> concat(Stream<T>[] streams, int start, int end) {
        switch (end - start) {
            case 0:
                return Stream.empty();
            case 1:
                return streams[start];
            default:
                int mid = (start + end + 1) / 2;
                return Stream.concat(concat(streams, start, mid), concat(streams, mid, end));
        }
    }

}
