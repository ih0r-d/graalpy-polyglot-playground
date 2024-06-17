package com.github.ih0rd.utils;

import com.github.ih0rd.exceptions.InvokeMethodException;
import com.github.ih0rd.exceptions.ValidationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommonUtils {
    public static <T, R> R invokeMethod(Class<T> targetType, T targetInstance, String methodName, Object... args) {
        try {
            // Find the method with the given name and parameter types
            Class<?>[] argTypes = new Class[args.length];

            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }

            Method method = targetType.getMethod(methodName, argTypes);

            // Invoke the method on the target instance
            Object result = method.invoke(targetInstance, args);

            // If the method return type is void, return null
            if (method.getReturnType().equals(Void.TYPE)) {
                return null;
            } else {
                @SuppressWarnings("unchecked")
                R castResult = (R) result;
                return castResult;
            }

        } catch (ClassCastException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new InvokeMethodException("Could not invoke method '" + methodName + "'", e);
        }
    }

    public static List<String> getInterfaceMethods(Class<?> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new ValidationException("Provided class '" + interfaceClass.getName() + "' must be an interface");
        }

        // Convert array of methods to a list of method names
        return Arrays.stream(interfaceClass.getDeclaredMethods())
                .map(Method::getName)
                .toList();
    }

    public static <T> T getFirstElement(Set<T> set) {
        if (set == null || set.isEmpty()) {
            return null;
        }
        Iterator<T> iterator = set.iterator();
        return iterator.next();
    }
}
