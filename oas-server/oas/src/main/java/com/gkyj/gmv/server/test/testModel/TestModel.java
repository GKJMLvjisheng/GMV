package com.gkyj.gmv.server.test.testModel;

import com.cascv.oas.core.common.BaseEntity;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.Data;
@Data
@Table("box_info")
public class TestModel extends BaseEntity {
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

}
