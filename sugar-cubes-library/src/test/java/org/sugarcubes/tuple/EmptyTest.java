package org.sugarcubes.tuple;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class EmptyTest {

    @Test
    public void testSerialization() throws Exception {
        Empty empty = Empty.INSTANCE;
        Object object = SerializationUtils.deserialize(SerializationUtils.serialize(empty));
        Assert.assertSame(empty, object);
    }

}
