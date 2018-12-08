package com.gkyj.gmv.server.test.wrapper;

import com.gkyj.gmv.server.test.model.TestModel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public class LoadInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter @Setter private String startTime;
    @Getter @Setter private String endTime;
    @Getter @Setter private Integer number;
    @Getter @Setter private List<TestModel> testModelList;
}
