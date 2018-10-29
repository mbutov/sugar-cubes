package org.sugarcubes.reflection;

/**
 * Wrapper for {@link Package}.
 *
 * @author Maxim Butov
 */
public abstract class XPackage extends XReflectionObjectImpl<Package> {

    XPackage(Package reflectionObject) {
        super(reflectionObject);
    }

    public XPackage(String name) {
        this(Package.getPackage(name));
    }

    public String getName() {
        return getReflectionObject().getName();
    }

}
