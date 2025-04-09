package org.thluon.tdrive.exception;

public class UniqueKeyViolationException extends BaseException {
    public UniqueKeyViolationException(String message, String fieldName, String modelName, String rejectedValue) {
        super(message, fieldName, modelName, rejectedValue);
    }
}