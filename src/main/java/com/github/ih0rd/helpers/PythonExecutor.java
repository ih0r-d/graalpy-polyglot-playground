package com.github.ih0rd.helpers;

import com.github.ih0rd.exceptions.PolyglotApiExecutionException;
import com.github.ih0rd.utils.StringCaseConverter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static com.github.ih0rd.helpers.PolyglotHelper.*;
import static com.github.ih0rd.utils.CommonUtils.*;
import static com.github.ih0rd.utils.Constants.PYTHON;

public class PythonExecutor {

    /**
     * @param memberTargetType Java target type associated from Python class
     * @param methodName method name will be evaluated
     * @param args Varargs of input arguments needed to python method and java association
     * @param <T> Generic type of Java target type.
     * @return Result of evaluation method. If method type VOID, as result will be null
     */
    public static <T> Object evaluate(Class<T> memberTargetType, String methodName, Object... args) {
        Object result;

        try (var context = getContext()) {
            var type = mapValue(memberTargetType, methodName, context);
            result = invokeMethod(memberTargetType, type, methodName, args);
            System.out.println("object = " + result);
            return result;
        }
    }


    /**
     * @param memberTargetType Java target type associated from Python class
     * @param methodName method name will be evaluated
     * @param <T> Generic type of Java target type.
     * @return Result of evaluation method. If returned type is void, result will be null
     */
    public static <T> Object evaluate(Class<T> memberTargetType, String methodName) {
        Object result;
        try (var context = getContext()) {
            var type = mapValue(memberTargetType, methodName, context);
            result = invokeMethod(memberTargetType, type, methodName);
            System.out.println("object = " + result);
            return result;
        }
    }

    /**
     * @param pyClassName Name of python file will be use to evaluate logic
     * @param memberTargetType Java target type associated from Python class
     * @param <T> Generic type of Java target type.
     */
    private static <T> void validate(String pyClassName, Class<T> memberTargetType) {
        if (pyClassName == null || pyClassName.isEmpty()) {
            throw new PolyglotApiExecutionException("Cannot invoke Python script without valid class name : " + pyClassName);
        }

        String interfaceName = memberTargetType.getSimpleName();
        if (!interfaceName.equals(pyClassName)) {
            throw new PolyglotApiExecutionException("Interface name '" + interfaceName + "' must me equals python class name '" + pyClassName + "'");
        }
    }


    /**
     * @param memberTargetType Java target type associated from Python class
     * @param methodName method name will be evaluated
     * @param context A polyglot context for Graal guest languages that allows to evaluate code.
     * @param <T> Generic type of Java target type.
     * @return returned maps a polyglot value to a value with a given Java target type.
     */
    private static <T> T mapValue(Class<T> memberTargetType, String methodName, Context context) {
        var source = getFileSource(memberTargetType);
        context.eval(source);

        var polyglotBindings = context.getPolyglotBindings();
        var pyClass = getFirstElement(polyglotBindings.getMemberKeys());
        validate(pyClass, memberTargetType);

        var member = polyglotBindings.getMember(pyClass);

        if (!checkIfMethodExists(memberTargetType, methodName) || member.getMember(methodName) == null) {
            throw new PolyglotApiExecutionException("Method " + methodName + " is not supported to evaluate " + memberTargetType);
        }

        return member.newInstance().as(memberTargetType);
    }

    /**
     * @param memberTargetType Java target type associated from Python class
     * @param <T> Generic type of Java target type.
     * @return Representation of a source code unit and its contents that can be evaluated in an execution context.
     */
    private static <T> Source getImportSource(Class<T> memberTargetType) {
        Source source;

        var interfaceName = memberTargetType.getSimpleName();
        var pyFileName = StringCaseConverter.camelToSnake(interfaceName);
        if (!checkFileExists(pyFileName)) {
            throw new PolyglotApiExecutionException("Cannot find Python script file: " + pyFileName);
        }
        try {
            source = Source
                    .newBuilder(PYTHON, "import " + pyFileName, "<internal>")
                    .build();
        } catch (IOException e) {
            throw new PolyglotApiExecutionException("Could not load Python script file: " + pyFileName, e);
        }
        return source;
    }

    /**
     * @param memberTargetType Java target type associated from Python class
     * @param <T> Generic type of Java target type.
     * @return Representation of a source code unit and its contents that can be evaluated in an execution context.
     */
    private static <T> Source getFileSource(Class<T> memberTargetType) {
        Source source;

        var interfaceName = memberTargetType.getSimpleName();
        var pyFileName = StringCaseConverter.camelToSnake(interfaceName);
        Optional<Path> optionalPath = checkFileExists2(pyFileName);
        if (optionalPath.isEmpty()) {
            throw new PolyglotApiExecutionException("Cannot find Python script file: " + pyFileName);
        }
        try {
            source = Source
                    .newBuilder(PYTHON, optionalPath.get().toFile())
                    .build();
        } catch (IOException e) {
            throw new PolyglotApiExecutionException("Could not load Python script file: " + pyFileName, e);
        }
        return source;
    }

}
