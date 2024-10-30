package com.github.ih0rd.exceptions;

/**
 * Runtime exception to use with GraalPyRunner polyglot API executions
 */
public class GraalPyExecutionException extends RuntimeException {

    public GraalPyExecutionException(String message) {
        super(message);
    }

    public GraalPyExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
