package com.cascv.oas.core.common;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

// Entity
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Setter @Getter private String searchValue;
    @Setter @Getter private Map<String, Object> params;
}
