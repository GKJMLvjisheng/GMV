package com.gkyj.gmv.server.test.service;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.gkyj.gmv.server.load.model.LoadModel;
import com.gkyj.gmv.server.test.model.TestModel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TestService {
    @Autowired
    private CassandraOperations cassandraTemplate;
    public List<TestModel> selectLoadMsg(Integer number){
        Integer maxCircleNumber = 2;
        Select maxCircleNumberData = QueryBuilder.select().from("timefrombox").where(QueryBuilder.eq("circle_number",maxCircleNumber)).limit(number).allowFiltering();
        List<TestModel> testModelList = this.cassandraTemplate.select(maxCircleNumberData,TestModel.class);
        //List<LoadModel> loadModelList = this.cassandraTemplate.select(String.format("select * from load_box where circle_number = %d order by time asc limit %d",maxCircleNumber,number),LoadModel.class);
        return testModelList;
    }
    public List<TestModel> selectLoad(){
        Integer maxCircleNumber = 2;
        Select maxCircleNumberData = QueryBuilder.select().from("timefrombox").where(QueryBuilder.eq("circle_number",maxCircleNumber)).allowFiltering();
        List<TestModel> testModelList = this.cassandraTemplate.select(maxCircleNumberData,TestModel.class);
        //List<LoadModel> loadModelList = this.cassandraTemplate.select(String.format("select * from load_box where circle_number = %d order by time asc limit %d",maxCircleNumber,number),LoadModel.class);
        return testModelList;
    }
    public List<TestModel> selectLoadMsgByPeriod(String startTime,String endTime){
        Select LoadMsgByPeriod = QueryBuilder.select().from("timefrombox").where(QueryBuilder.eq("circle_number",2)).and(QueryBuilder.gte("time",startTime)).and(QueryBuilder.lte("time",endTime)).allowFiltering();
        List<TestModel> testModelList = this.cassandraTemplate.select(LoadMsgByPeriod,TestModel.class);
        return testModelList;
    }


}
