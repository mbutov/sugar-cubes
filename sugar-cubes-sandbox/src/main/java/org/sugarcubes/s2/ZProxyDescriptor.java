package org.sugarcubes.s2;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.naming.OperationNotSupportedException;

public class ZProxyDescriptor implements ZDescriptor {

    private static final InvocationHandler DEFAULT_INVOCATION_HANDLER =
        (proxy, method, args) -> {
            throw new OperationNotSupportedException();
        };

    private final Class<?>[] interfaces;
    private ZClassDescriptor superClassDescriptor;

    public ZProxyDescriptor(String[] interfaces) throws ClassNotFoundException {
        this.interfaces = new Class<?>[interfaces.length];
        for (int k = 0; k < interfaces.length; k++) {
            this.interfaces[k] = Class.forName(interfaces[k]);
        }
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }

    @Override
    public Class<?> getType() {
        return newInstance().getClass();
    }

    @Override
    public Object newInstance() {
        return Proxy.newProxyInstance(null, interfaces, DEFAULT_INVOCATION_HANDLER);
    }

    @Override
    public void readDescriptor(ZObjectInputStream in) throws IOException, ClassNotFoundException {
        // reading class annotations
        in.blockDataInputStream().skip(Integer.MAX_VALUE);

        superClassDescriptor = in.readNext(ZClassDescriptor.class, ZTag.NOT_NULL_CLASSDESC_TAGS);
    }

    @Override
    public void readObject(ZObjectInputStream in, Object obj) throws IOException, ClassNotFoundException {
        superClassDescriptor.readObject(in, obj);
    }

    @Override
    public Object resolve(Object obj) {
        return obj;
    }
}
