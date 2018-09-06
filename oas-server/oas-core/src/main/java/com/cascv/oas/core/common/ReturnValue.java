package com.cascv.oas.core.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

// Entity
public class ReturnValue<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Setter @Getter private ErrorCode errorCode;
    @Setter @Getter private T data;
}
