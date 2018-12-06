package com.gkyj.gmv.server.test.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class TimeInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Setter @Getter private String startTime;
    @Setter @Getter private String endTime;
}
