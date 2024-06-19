package com.github.ih0rd;

import com.github.ih0rd.adapters.Hello;
import com.github.ih0rd.adapters.OptimizeService;
import com.github.ih0rd.helpers.PythonExecutor;

import java.util.List;
import java.util.Map;


public class GraalPyRunner {


    public static void main(String[] args) {

//
//        Object helloEval = PythonExecutor.evaluate(Hello.class, "hello", "GenericOne");
//        System.out.println("helloEval = " + helloEval);
//        Object valueEval = PythonExecutor.evaluate(Hello.class, "num");
//        System.out.println("valueEval = " + valueEval);
//        Object sumEval = PythonExecutor.evaluate(Hello.class, "sum", 3,7);
//        System.out.println("sumEval = " + sumEval);


        //if __name__ == '__main__':
//    A = [[2, 1], [1, 2]]
//    b = [4, 3]
//    c = [1, 1]
//
//    #     print('A: Matrix that represents coefficients of constraints.')
//    #             print('b: Ax <= b')
//    #             print('c: Coefficients of objective function.')
//    #             print('p: Indicates max or min objective function.')
//
//    simplex_solver = SimplexSolver()
//    min_res = simplex_solver.run_simplex(A, b, c, prob='min', enableMsg=False, latex=True)
//    print(min_res)
//        var coefficients = List.of(-1, -2, -3, -4, -5);
//        var constraints = Map.of(
//                "a", List.of(
//                        List.of(2, 1, 1, 0, 0),
//                        List.of(1, 3, 0, 1, 0),
//                        List.of(0, 2, 0, 0, 1)
//                ),
//                "b", List.of(20, 30, 15),
//                "a_eq", List.of(
//                        List.of(1, 1, 1, 1, 1)
//                ),
//                "b_eq", List.of(40)
//        );


        var aInput = List.of(List.of(1, 2), List.of(4, 0), List.of(0, 4));
        var bInput = List.of(8, 16, 12);
        var cInput = List.of(3, 2);
        var prob = "max";
        var enableMsg = true;
        var latex = true;

        var optimizeEvalResult = PythonExecutor.evaluate(
                OptimizeService.class,
                "run_simplex",
                aInput, bInput, cInput, prob, null, enableMsg, latex
        );
        System.out.println("optimizeEvalResult = " + optimizeEvalResult);
//        {'x_1': 4.0, 'x_2': 2.0, 's_1': 0, 's_2': 0, 's_3': 4.0, 'z': 16.0}
    }
}