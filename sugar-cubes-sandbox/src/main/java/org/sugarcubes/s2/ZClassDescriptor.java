package org.sugarcubes.s2;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.Set;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.StdInstantiatorStrategy;

public class ZClassDescriptor implements ZDescriptor {

    private final Class<?> type;
    private final String name;
    private final long serialVersionUid;
    private final Set<ZTypeFlag> flags;
    private final ZField[] fields;

    private ObjectInstantiator<?> instantiator;
    private ZClassDescriptor superClassDescriptor;

    public ZClassDescriptor(String name, long serialVersionUid, Set<ZTypeFlag> flags, int numFields) throws ClassNotFoundException {
        this.type = Class.forName(name);
        this.name = name;
        this.serialVersionUid = serialVersionUid;
        this.flags = flags;
        this.fields = new ZField[numFields];
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public Object newInstance() {
        if (instantiator == null) {
            instantiator = new StdInstantiatorStrategy().newInstantiatorOf(type);
        }
        return instantiator.newInstance();
    }

    public String getName() {
        return name;
    }

    public long getSerialVersionUid() {
        return serialVersionUid;
    }

    public Set<ZTypeFlag> getFlags() {
        return flags;
    }

    public ZField[] getFields() {
        return fields;
    }

    @Override
    public void readDescriptor(ZObjectInputStream in) throws IOException, ClassNotFoundException {
        for (int n = 0; n < fields.length; n++) {
            ZFieldType fieldType = ZFieldType.get(in.readByte());
            String fieldName = in.readUTF();
            String className = fieldType.isPrimitive() ? null : in.readNext(String.class, ZTag.NOT_NULL_STRING_TAGS);
            fields[n] = new ZField(this, fieldType, fieldName, className);
        }

        // reading class annotations
        in.blockDataInputStream().skip(Integer.MAX_VALUE);

        superClassDescriptor = in.readNext(ZClassDescriptor.class, ZTag.CLASSDESC_TAGS);
    }

    @Override
    public void readObject(ZObjectInputStream in, Object obj) throws IOException, ClassNotFoundException {
        for (ZField field : getFields()) {
            field.readField(in, obj);
        }
        if (superClassDescriptor != null) {
            superClassDescriptor.readObject(in, obj);
        }
    }

    @Override
    public Object resolve(Object obj) throws ObjectStreamException {
        ZSerialInfo classInfo = ZSerialInfo.of(type);
        return classInfo.hasReadResolve() ? classInfo.readResolve(obj) : obj;
    }
}
