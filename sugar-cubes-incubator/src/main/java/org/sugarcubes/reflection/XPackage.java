package org.sugarcubes.reflection;

/**
 * Wrapper for {@link Package}.
 *
 * @author Maxim Butov
 */
abstract class XPackage extends XReflectionObjectImpl<Package> implements XAnnotated<Package> {

    XPackage(Package reflectionObject) {
        super(reflectionObject);
    }

    public String getName() {
        return getReflectionObject().getName();
    }

}
