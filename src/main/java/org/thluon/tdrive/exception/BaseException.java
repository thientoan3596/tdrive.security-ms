package org.thluon.tdrive.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseException extends RuntimeException{
    private String fieldName;
    private String modelName;
    private String rejectedValue;
    public BaseException(String message, String fieldName, String modelName, String rejectedValue) {
        super(message);
        this.fieldName = fieldName;
        this.modelName = modelName;
        this.rejectedValue = rejectedValue;
    }
}