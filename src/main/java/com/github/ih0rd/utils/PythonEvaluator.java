package com.github.ih0rd.utils;

import com.github.ih0rd.exceptions.InvokeMethodException;
import com.github.ih0rd.exceptions.ReadPyFileException;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.ih0rd.utils.PolyglotHelper.PYTHON;
import static com.github.ih0rd.utils.PolyglotHelper.getContext;

public class PythonEvaluator {

    public static <T> Object eval(String importPyFileName, String pyClass, Class<T> memberTargetType, String methodName, Object... args) {
        try (var context = getContext()) {
            var source = getSource(importPyFileName);
            context.eval(source);
            var bindings = context.getPolyglotBindings();

            var member = bindings.getMember(pyClass);
            var memberInstance = member.newInstance();

            var type = memberInstance.as(memberTargetType);

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

    public static <T> Object eval(String importPyFileName, String pyClass, Class<T> memberTargetType, String methodName) {
        try (var context = getContext()) {
            var source = getSource(importPyFileName);
            context.eval(source);
            var bindings = context.getPolyglotBindings();

            var member = bindings.getMember(pyClass);
            var memberInstance = member.newInstance();

            var type = memberInstance.as(memberTargetType);

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

}
