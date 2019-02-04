package org.sugarcubes.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.sugarcubes.reflection.XClass;
import org.sugarcubes.reflection.XFieldAccessor;
import org.sugarcubes.reflection.XReflection;

/**
 * @author Maxim Butov
 */
public class ObjectStatePersister {

    enum N {

        NULL('N'),
        REFERENCE('R'),
        ENUM('E'),
        OBJECT('O'),
        ;

        private final int data;

        N(int data) {
            this.data = data;
        }

        void write(DataOutputStream out, Object object, Map<Object, Integer> saved) throws IOException {
            out.writeByte(data);
            write1(out, object, saved);
        }

        void write1(DataOutputStream out, Object object, Map<Object, Integer> saved) throws IOException {
        }
        
    }

    public void save(OutputStream out, Object object) throws IOException {
        save(new DataOutputStream(out), object, new IdentityHashMap<>());
    }

    private void save(DataOutputStream out, Object object, Map<Object, Integer> saved) throws IOException {

        if (object == null) {
            out.write(N.NULL.data);
        }
        else {
            Integer reference = saved.get(object);
            if (reference != null) {
                out.write(N.REFERENCE.data);
                out.writeInt(reference);
            }
            else {
                saved.put(object, saved.size());
                saveObject(out, object, saved);
            }
        }

    }

    private void saveObject(DataOutputStream out, Object object, Map<Object, Integer> saved) throws IOException {
        if (object instanceof Enum) {
            out.write(N.ENUM.data);
            out.writeUTF(object.getClass().getName());
            out.writeUTF(((Enum) object).name());
        }
        else {
            out.write(N.OBJECT.data);
            XClass<?> xClass = XReflection.of(object.getClass());
            saveFields(out, object, xClass, true, saved);
        }

    }

    private void saveFields(DataOutputStream out, Object object, XClass<?> xClass, boolean root, Map<Object, Integer> saved) throws IOException {
        if (!xClass.isNull()) {
            List<XFieldAccessor> accessors = xClass.getDeclaredFields()
                .map(field -> field.getAccessor(object))
                .filter(acc -> acc.get() != null)
                .collect(Collectors.toList());
            if (!accessors.isEmpty() || root) {
                out.writeUTF(xClass.getName());
            }
            accessors.forEach(acc -> saveField(out, acc, saved));
            saveFields(out, object, xClass.getSuperclass(), false, saved);
        }
    }

    private void saveField(DataOutputStream out, XFieldAccessor acc, Map<Object, Integer> saved) {

    }

}