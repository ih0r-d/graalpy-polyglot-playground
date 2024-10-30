package com.github.ih0rd.utils;

import com.github.ih0rd.exceptions.GraalPyExecutionException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.github.ih0rd.utils.Constants.*;

/**
 * Common utils contains general methods
 */
public class CommonUtils {

    /**
     * @param targetType     Java target type associated from Python class
     * @param targetInstance mapped a polyglot value to a value with a given Java target type.
     * @param methodName     method name will be evaluated
     * @param args           Varargs of input arguments needed to python method and java association
     * @param <T>            Generic type of Java target type.
     * @return Result of evaluation method. Result will be a map with returnType and result values, if returned type is void, result will be Optional.empty()
     */
    public static <T> Map<String, Object> invokeMethod(Class<T> targetType, T targetInstance, String methodName, Object... args) {
        try {
            var method = getMethodByName(targetType, methodName);

            // Invoke the method on the target instance
            Object invoke = method.invoke(targetInstance, args);

            return Map.of("returnType",  method.getReturnType().getCanonicalName(), "result", invoke != null ? invoke : Optional.empty());

        } catch (Exception e) {
            throw new GraalPyExecutionException("Could not invoke method '" + methodName + "'", e);
        }
    }


    private static <T> Method getMethodByName(Class<T> targetType, String methodName) throws NoSuchMethodException {
        var parameterTypes = getParameterTypesByMethodName(targetType, methodName);
        return targetType.getMethod(methodName, parameterTypes);
    }

    /**
     * @param targetType Java target type associated from Python class
     * @param methodName method name will be evaluated
     * @param <T>        Generic type of Java target type.
     * @return array of parameter types of arguments by methodName
     */
    private static <T> Class<?>[] getParameterTypesByMethodName(Class<T> targetType, String methodName) {
        var methodOptional = Arrays.stream(targetType.getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst();
        if (methodOptional.isEmpty()) {
            throw new GraalPyExecutionException("Method '" + methodName + "' not found");
        }
        return methodOptional.get().getParameterTypes();
    }


    /**
     * @param interfaceClass Java target type associated from Python class
     * @param methodName     method name will be evaluated
     * @return true if method exists
     */
    public static boolean checkIfMethodExists(Class<?> interfaceClass, String methodName) {
        if (!interfaceClass.isInterface()) {
            throw new GraalPyExecutionException("Provided class '" + interfaceClass.getName() + "' must be an interface");
        }

        return Arrays.stream(interfaceClass.getDeclaredMethods())
                .map(Method::getName)
                .anyMatch(name -> name.contains(methodName));
    }

    /**
     * @param memberKeys set of all member keys from bindings
     * @param <T>        Generic type of Java target type.
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
     * @return Optional of Path if file exists
     */
    public static Optional<Path> checkFileExists(String fileName) {
        try (var files = Files.list(Paths.get(PROJ_RESOURCES_PATH))) {
            return files.filter(f -> f.getFileName().toString().contains(fileName.toLowerCase())).findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}

