package org.sugarcubes.laser;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Lambda serialization utils.
 *
 * @author Maxim Butov
 */
public class Laser {

    public static boolean isLambda(Object obj) {
        return obj != null && isLambdaClass(obj.getClass());
    }

    private static final Pattern LAMBDA_CLASS_NAME_PATTERN = Pattern.compile(".*\\$\\$Lambda\\$\\d+/\\d+$");

    public static boolean isLambdaClass(Class cl) {
        if (!LAMBDA_CLASS_NAME_PATTERN.matcher(cl.getName()).matches()) {
            return false;
        }
        if (!Object.class.equals(cl.getSuperclass())) {
            return false;
        }
        if (!cl.isSynthetic() || cl.isLocalClass() || cl.isAnonymousClass()) {
            return false;
        }
        long methodCount = Arrays.stream(cl.getInterfaces()).flatMap(c -> Arrays.stream(c.getMethods()))
            .filter(m -> !m.isDefault()).count();
        if (methodCount != 1) {
            return false;
        }
        // todo: maybe something else?
        return true;
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
