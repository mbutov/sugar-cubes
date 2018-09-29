package org.sugarcubes.reflection;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XClassPackage extends XPackage {

    private final XClass xClass;

    public XClassPackage(XClass<?> xClass) {
        super(xClass.getReflectionObject().getPackage());
        this.xClass = xClass;
    }

    public XClass getXClass() {
        return xClass;
    }

}
