package com.github.ih0rd;

import com.github.ih0rd.adapters.Hello;
import com.github.ih0rd.helpers.PythonExecutor;


public class GraalPyRunner {


    public static void main(String[] args) {


        Object helloEval = PythonExecutor.evaluate(Hello.class, "hello", "GenericOne");
        System.out.println("helloEval = " + helloEval);
        Object valueEval = PythonExecutor.evaluate(Hello.class, "num");
        System.out.println("valueEval = " + valueEval);
        Object sumEval = PythonExecutor.evaluate(Hello.class, "sum", 3,7);
        System.out.println("sumEval = " + sumEval);
    }
}