package com.github.ih0rd.utils;

import com.github.ih0rd.exceptions.InvokeMethodException;
import com.github.ih0rd.exceptions.ReadPyFileException;
import com.github.ih0rd.exceptions.ValidationException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.github.ih0rd.utils.PolyglotHelper.PYTHON;
import static com.github.ih0rd.utils.PolyglotHelper.getContext;

public class PythonEvaluator {

    public static <T> Object evaluate(String importPyFileName, String pyClass, Class<T> memberTargetType, String methodName, Object... args) {
        try (var context = getContext()) {
            var type = mapValue(importPyFileName, pyClass, memberTargetType, methodName, context);
            return invokeMethod(memberTargetType, type, methodName, args);
        } catch (PolyglotException e) {
            if (e.isExit()) {
                System.exit(e.getExitStatus());
            } else {
                throw new InvokeMethodException("Could not invoke method " + methodName, e);
            }
        }
        return null;
    }

    public static <T> Object evaluate(String importPyFileName, String pyClass, Class<T> memberTargetType, String methodName) {
        try (var context = getContext()) {
            var type = mapValue(importPyFileName, pyClass, memberTargetType, methodName, context);

            return invokeMethod(memberTargetType, type, methodName);
        } catch (PolyglotException e) {
            if (e.isExit()) {
                System.exit(e.getExitStatus());
            } else {
                throw new InvokeMethodException("Could not invoke method " + methodName, e);
            }
        }
        return null;
    }


    //    Maps a polyglot value to a value with a given Java target type.
    private static <T> T mapValue(String importPyFileName, String pyClass, Class<T> memberTargetType, String methodName, Context context) {
        var source = getSource(importPyFileName);
        context.eval(source);

        var member = context.getPolyglotBindings().getMember(pyClass);
        if (!isMethodSupported(member, methodName, memberTargetType)) {
            throw new ValidationException("Method " + methodName + " is not supported to evaluate " + memberTargetType);
        }

        return member.newInstance().as(memberTargetType);
    }

    private static <T, R> R invokeMethod(Class<T> targetType, T targetInstance, String methodName, Object... args) {
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

    private static Source getSource(String importPyFileName) {
        Source source;
        try {
            source = Source
                    .newBuilder(PYTHON, "import " + importPyFileName, "<internal>")
                    .internal(true)
                    .build();
        } catch (IOException e) {
            throw new ReadPyFileException("Could not load Python script file: " + importPyFileName, e);
        }
        return source;
    }

    private static List<String> getInterfaceMethods(Class<?> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new ValidationException("Provided class '" + interfaceClass.getName() + "' must be an interface");
        }

        // Convert array of methods to a list of method names
        return Arrays.stream(interfaceClass.getDeclaredMethods())
                .map(Method::getName)
                .toList();
    }

    private static boolean isMethodSupported(Value pyMember, String methodName, Class<?> interfaceClazz) {
        return getInterfaceMethods(interfaceClazz).contains(methodName) && pyMember.getMember(methodName) != null;
    }

}
