package com.gkyj.gmv.server.test.testModel;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.Data;
@Data
@Table
public class TestModel {
 
    @PrimaryKey
    private Integer box_id;
    
    @Column(value = "circle_number")
    private Integer circleNumber;
    
    @Column(value = "parameter")
    private String  parameter;
    
    @Column(value = "pic_path")
    private String  picPath;
    
    @Column(value = "value")
    private double value;
    
    @Column(value = "time")
    private String time;
//    
//    @Override
//    public String toString() {
//        return String.format("TestModel[boxId=%s, circleNumber=%s ,parameter=%s ,picPath=%s ,value=%s ,time=%s]", this.box_id,
//        		this.circleNumber,this.parameter,this.picPath,this.value,this.time);
//    }
}
