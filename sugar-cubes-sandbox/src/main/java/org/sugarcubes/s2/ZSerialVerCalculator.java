package org.sugarcubes.s2;

import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Proxy;

public class ZSerialVerCalculator {

    public static long getSerialVersionUID(Class<?> cl) {
        if (!Serializable.class.isAssignableFrom(cl) || Proxy.isProxyClass(cl)) {
            return 0;
        }

        return ObjectStreamClass.lookup(cl).getSerialVersionUID();
    }

}
