package org.sugarcubes.cloner;

/**
 * Available cloning actions for fields/classes.
 *
 * @author Maxim Butov
 */
public enum ReflectionClonerAction {

    /**
     * Skip the field, don't modify it in target object.
     */
    SKIP,

    /**
     * Set null into target object.
     */
    CLEAR,

    /**
     * Copy value without cloning.
     */
    COPY,

    /**
     * Default cloner behavior (deep clone).
     */
    DEFAULT,

}
