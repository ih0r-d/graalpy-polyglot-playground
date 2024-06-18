package com.github.ih0rd.utils;

import com.github.ih0rd.exceptions.PolyglotApiExecutionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import static com.github.ih0rd.utils.Constants.*;

/**
 * Common utils contains general methods
 */
public class CommonUtils {

    /**
     * @param targetType Java target type associated from Python class
     * @param targetInstance maped a polyglot value to a value with a given Java target type.
     * @param methodName method name will be evaluated
     * @param args Varargs of input arguments needed to python method and java association
     * @param <T> Generic type of Java target type.
     * @param <R> Result type of evaluation method
     * @return Result of evaluation method. If returned type is void, result will be null
     */
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
            throw new PolyglotApiExecutionException("Could not invoke method '" + methodName + "'", e);
        }
    }


    /**
     * @param interfaceClass Java target type associated from Python class
     * @param methodName method name will be evaluated
     * @return true if method exists
     */
    public static boolean checkIfMethodExists(Class<?> interfaceClass, String methodName) {
        if (!interfaceClass.isInterface()) {
            throw new PolyglotApiExecutionException("Provided class '" + interfaceClass.getName() + "' must be an interface");
        }

        return Arrays.stream(interfaceClass.getDeclaredMethods())
                .map(Method::getName)
                .anyMatch(name -> name.contains(methodName));
    }

    /**
     * @param memberKeys set of all member keys from bindings
     * @param <T> Generic type of Java target type.
     * @return member key if memberKeys is not null and not empty
     */
    public static <T> T getFirstElement(Set<T> memberKeys) {
        if (memberKeys == null || memberKeys.isEmpty()) {
            return null;
        }
        Iterator<T> iterator = memberKeys.iterator();
        return iterator.next();
    }

    /**
     * @param fileName simpleName of java interface
     * @return true if file exists
     */
    public static boolean checkFileExists(String fileName) {
        try (var files = Files.list(Paths.get(PROJ_RESOURCES_PATH))) {
            return files.anyMatch(f -> f.getFileName().toString().contains(fileName.toLowerCase()));
        } catch (IOException e) {
            return false;
        }
    }
}

