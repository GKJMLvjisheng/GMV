package com.gkyj.gmv.server.test.testModel;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.Data;
@Data
@Table
public class TestModel {
 
    @PrimaryKey
    private Integer box_id;
    private Integer circle_number;
    private String  parameter;
    private String  pic_path;
    private double value;
    private String time;
    @Override
    public String toString() {
        return String.format("TestModel[box_id=%s, circle_number=%s ,parameter=%s ,pic_path=%s ,value=%s ,time=%s]", this.box_id,
        		this.circle_number,this.parameter,this.pic_path,this.value,this.time);
    }
}
