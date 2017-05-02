package org.sugarcubes.laser;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Lambda serialization utils.
 *
 * @author Maxim Butov
 */
public class Laser {

    /**
     * Checks whether an object is lambda or isn't.
     * @param obj object to check
     * @return true if obj is lambda
     */
    public static boolean isLambda(Object obj) {
        return obj != null && isLambdaClass(obj.getClass());
    }

    /**
     * Pattern of a lambda class name.
     */
    private static final Pattern LAMBDA_CLASS_NAME_PATTERN = Pattern.compile(".*\\$\\$Lambda\\$\\d+/\\d+$");

    public static boolean isLambdaClass(Class cl) {
        if (!LAMBDA_CLASS_NAME_PATTERN.matcher(cl.getName()).matches()) {
            return false;
        }
        if (!cl.isSynthetic() || cl.isLocalClass() || cl.isAnonymousClass()) {
            return false;
        }
        if (getFunctionalInterfaceMethod(cl) == null) {
            return false;
        }
        // todo: maybe something else?
        return true;
    }

    public static Method getFunctionalInterfaceMethod(Class lambdaClass) {
        if (Object.class.equals(lambdaClass.getSuperclass())) {
            List<Method> methods = Arrays.stream(lambdaClass.getInterfaces()).flatMap(c -> Arrays.stream(c.getMethods()))
                .filter(m -> !m.isDefault()).collect(Collectors.toList());
            if (methods.size() == 1) {
                return methods.iterator().next();
            }
        }
        return null;
    }

    public static boolean isNonSerializableLambda(Object obj) {
        return isLambda(obj) && !(obj instanceof Serializable);
    }

    public static <T> T serializable(T lambda) {
        if (lambda == null || lambda instanceof Serializable) {
            return lambda;
        }
        Class<?> lambdaClass = lambda.getClass();
        if (!isLambdaClass(lambdaClass)) {
            throw new IllegalArgumentException(lambdaClass + " is not lambda class");
        }
        Object proxy = Proxy.newProxyInstance(lambdaClass.getClassLoader(), lambdaClass.getInterfaces(),
            new SerializableLambdaInvocationHandler(lambda));
        return (T) proxy;
    }

}
