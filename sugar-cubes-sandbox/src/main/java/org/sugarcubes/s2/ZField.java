package org.sugarcubes.s2;

import java.io.IOException;
import java.lang.reflect.Field;

public class ZField {

    private final ZClassDescriptor cd;
    private final ZFieldType type;
    private final String name;
    private final String className;

    public ZField(ZClassDescriptor cd, ZFieldType type, String name, String className) {
        this.cd = cd;
        this.type = type;
        this.name = name;
        this.className = className;
    }

    public ZFieldType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    protected void readField(ZObjectInputStream in, Object obj) throws IOException, ClassNotFoundException {
        Field declaredField;
        try {
            declaredField = cd.getType().getDeclaredField(name);
        }
        catch (NoSuchFieldException e) {
            throw new IOException(e);
        }
        declaredField.setAccessible(true);
        try {
            ZFieldType type = getType();
            if (type.isPrimitive()) {
                type.read(obj, declaredField, in);
            }
            else {
                declaredField.set(obj, in.readNext(Object.class, ZTag.ANY_OBJECT_TAGS));
            }
        }
        catch (IllegalAccessException e) {
            throw new IOException(e);
        }
    }

}
