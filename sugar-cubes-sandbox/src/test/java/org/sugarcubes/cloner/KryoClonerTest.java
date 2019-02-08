package org.sugarcubes.cloner;

import org.junit.Ignore;

/**
 * @author Maxim Butov
 */
@Ignore
public class KryoClonerTest extends AbstractClonerTests {

    @Override
    protected Cloner getCloner() {
        return new KryoCloner();
    }

}
