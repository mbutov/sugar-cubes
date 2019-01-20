package org.sugarcubes.cloner;

/**
 * @author Maxim Butov
 */
public class KryoClonerTest extends AbstractClonerTests {

    @Override
    protected Cloner getCloner() {
        return new KryoCloner();
    }

}
