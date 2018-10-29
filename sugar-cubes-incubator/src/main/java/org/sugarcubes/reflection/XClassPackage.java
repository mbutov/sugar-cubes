package org.sugarcubes.reflection;

/**
 * Wrapper for {@link Package}.
 *
 * @author Maxim Butov
 */
public class XClassPackage extends XPackage {

    private final XClass xClass;

    XClassPackage(XClass<?> xClass) {
        super(xClass.getReflectionObject().getPackage());
        this.xClass = xClass;
    }

    public XClass getXClass() {
        return xClass;
    }

}
