package com.oofgz.fight.exception;

public class MyEvalException extends Exception {

    public MyEvalException(String message) {
        super(message);
    }

    public MyEvalException(String message, Throwable cause) {
        super(message, cause);
    }
}
