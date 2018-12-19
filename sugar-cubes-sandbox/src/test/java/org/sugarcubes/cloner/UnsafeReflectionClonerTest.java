package org.sugarcubes.cloner;

/**
 * @author Maxim Butov
 */
public class UnsafeReflectionClonerTest extends AbstractClonerTests {

    @Override
    protected Cloner getCloner() {
        return new UnsafeReflectionCloner();
    }

}
