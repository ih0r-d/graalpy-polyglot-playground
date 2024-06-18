package com.github.ih0rd.exceptions;

/**
 * Runtime exception to use with GraalPyRunner polyglot API executions
 */
public class PolyglotApiExecutionException extends RuntimeException {

    public PolyglotApiExecutionException(String message) {
        super(message);
    }

    public PolyglotApiExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
