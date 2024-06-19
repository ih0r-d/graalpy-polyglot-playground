package com.github.ih0rd.adapters;

import java.util.List;
import java.util.Map;

/**
 * Interface as contract to python class contains same methods with same signatures
 */
public interface OptimizeService {
    Map<String, Object> run_simplex(List<List<Integer>> aInput, List<Integer> bInput, List<Integer> cInput, String prob,Object ineq,
                       boolean enableMsg, boolean latex);
}
