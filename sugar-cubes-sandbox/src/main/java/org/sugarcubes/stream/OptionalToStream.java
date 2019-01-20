package org.sugarcubes.stream;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Maxim Butov
 */
public class OptionalToStream {

    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.map(Stream::of).orElseGet(Stream::empty);
    }

}
