package org.sugarcubes.cloner;

/**
 * @author Maxim Butov
 */
public class SerializationClonerTest extends AbstractClonerTests {

    @Override
    protected Cloner getCloner() {
        return Cloners.serialization();
    }

}
