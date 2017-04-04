package org.sugarcubes.valueholder.context;

import java.io.Serializable;
import java.util.Map;

//CHECKSTYLE:OFF

/**
 * @author Maxim Butov
 */
public interface ValueContext extends Serializable {

    Map getStore();

    void remove();

    default Object getMutex() {
        return this;
    }

}

//CHECKSTYLE:ON
