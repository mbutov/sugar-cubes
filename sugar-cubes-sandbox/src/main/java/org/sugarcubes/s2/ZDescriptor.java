package org.sugarcubes.s2;

import java.io.IOException;
import java.io.ObjectStreamException;

public interface ZDescriptor {

    Class<?> getType();

    Object newInstance();

    void readDescriptor(ZObjectInputStream in) throws IOException, ClassNotFoundException;

    void readObject(ZObjectInputStream in, Object obj) throws IOException, ClassNotFoundException;

    Object resolve(Object obj) throws ObjectStreamException;

}
