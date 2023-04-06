package org.sugarcubes.function;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import static org.sugarcubes.function.Predicates.and;
import static org.sugarcubes.function.Predicates.not;
import static org.sugarcubes.function.Predicates.or;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class PredicatesTest {

    @Test
    public void testPredicates() {

        Predicate<String> stringIsNotNullAndNotEmpty = and(Objects::nonNull, not(String::isEmpty));
        Predicate<List> listIsNullOrEmpty = or(Objects::isNull, Collection::isEmpty);

    }

}
