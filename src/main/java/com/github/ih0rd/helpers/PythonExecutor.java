package com.github.ih0rd.helpers;

import com.github.ih0rd.exceptions.InvokeMethodException;
import com.github.ih0rd.exceptions.ReadPyFileException;
import com.github.ih0rd.exceptions.ValidationException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;

import java.io.IOException;

import static com.github.ih0rd.helpers.PolyglotHelper.*;
import static com.github.ih0rd.utils.CommonUtils.*;

public class PythonExecutor {

    public static <T> Object evaluate(String importPyFileName, Class<T> memberTargetType, String methodName, Object... args) {

        try (var context = getContext()) {
            var type = mapValue(importPyFileName, memberTargetType, methodName, context);
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

    private static <T> void validate(String pyClassName, Class<T> memberTargetType) {
        if (pyClassName == null || pyClassName.isEmpty()) {
            throw new ValidationException("Cannot invoke Python script without valid class name : " + pyClassName);
        }

        String interfaceName = memberTargetType.getSimpleName();
        if (!interfaceName.equals(pyClassName)) {
            throw new ValidationException("Interface name '" + interfaceName + "' must me equals python class name '" + pyClassName + "'");
        }
    }

    public static <T> Object evaluate(String importPyFileName, Class<T> memberTargetType, String methodName) {

        try (var context = getContext()) {
            var type = mapValue(importPyFileName, memberTargetType, methodName, context);
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
    private static <T> T mapValue(String importPyFileName, Class<T> memberTargetType, String methodName, Context context) {
        var source = getSource(importPyFileName);
        context.eval(source);

        var polyglotBindings = context.getPolyglotBindings();
        var pyClass = getFirstElement(polyglotBindings.getMemberKeys());
        validate(pyClass, memberTargetType);

        var member = polyglotBindings.getMember(pyClass);

        if (!getInterfaceMethods(memberTargetType).contains(methodName) || member.getMember(methodName) == null) {
            throw new ValidationException("Method " + methodName + " is not supported to evaluate " + memberTargetType);
        }

        return member.newInstance().as(memberTargetType);
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
