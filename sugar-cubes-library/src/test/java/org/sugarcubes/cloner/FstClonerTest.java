package org.sugarcubes.cloner;

/**
 * @author Maxim Butov
 */
public class FstClonerTest extends AbstractClonerTests {

    @Override
    protected Cloner getCloner() {
        return new FstCloner();
    }

}
