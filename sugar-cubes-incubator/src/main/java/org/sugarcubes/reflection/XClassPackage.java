package org.sugarcubes.reflection;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XClassPackage extends XPackage {

    private final Class javaClass;

    public XClassPackage(Class javaClass) {
        super(javaClass.getPackage().getName());
        this.javaClass = javaClass;
    }

}
