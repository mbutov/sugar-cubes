package org.sugarcubes.proxy;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Maxim Butov
 */
public interface MethodInvocationHandler {

    Object invoke(MethodInvocation invocation) throws Throwable;

    Object processError(MethodInvocation invocation, Throwable throwable) throws Throwable;

}
