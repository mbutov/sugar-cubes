package org.sugarcubes.reflection;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XPackage extends XReflectionObject<Package> {

    public XPackage(Package reflectionObject) {
        super(reflectionObject);
    }

    public XPackage(String name) {
        this(Package.getPackage(name));
    }

    public String getName() {
        return getReflectionObject().getName();
    }

}
