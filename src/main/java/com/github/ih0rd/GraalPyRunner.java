package com.github.ih0rd;

import com.github.ih0rd.adapters.Hello;
import com.github.ih0rd.adapters.OptimizeService;
import com.github.ih0rd.adapters.RequestHandler;
import com.github.ih0rd.helpers.PythonExecutor;

import java.util.List;
import java.util.Map;


public class GraalPyRunner {


    public static void main(String[] args) {
        var pythonExecutor = new PythonExecutor();

//        var helloResult = pythonExecutor.evaluate("hello", Hello.class, "GraalDemo");
//        System.out.println("helloResult = " + helloResult);
//
//        var numResult = pythonExecutor.evaluate("num", Hello.class);
//        System.out.println("numResult = " + numResult);
//
//        var sumResult = pythonExecutor.evaluate("sum", Hello.class, 3,7);
//        System.out.println("sumResult = " + sumResult);
//
        var aInput = List.of(List.of(1, 2), List.of(4, 0), List.of(0, 4));
        var bInput = List.of(8, 16, 12);
        var cInput = List.of(3, 2);
        var prob = "max";
        var enableMsg = true;
        var latex = true;
        var simplexArgs = new Object[]{aInput, bInput, cInput, prob, null, enableMsg, latex};

        var runSimplexResult = pythonExecutor.evaluate("run_simplex", OptimizeService.class, simplexArgs);
        System.out.println("runSimplexResult = " + runSimplexResult);

//        Map<String, Object> result = pythonExecutor.evaluate("get", RequestHandler.class);
//        System.out.println("result = " + result);
        pythonExecutor.closeContext();

    }
}