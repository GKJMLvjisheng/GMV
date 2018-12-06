package com.gkyj.gmv.server.test.testService;

import com.datastax.driver.core.querybuilder.Ordering;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.gkyj.gmv.server.load.config.MediaServer;
import com.gkyj.gmv.server.test.testModel.TestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.cassandra.core.CassandraOperations;
import java.util.List;

@Service
@Slf4j
public class TestService{
    @Autowired
    private CassandraOperations cassandraTemplate;
    @Autowired
    private MediaServer mediaServer;

    public List<TestModel> findByTime(String time){

        Select select = QueryBuilder.select().from("box_info");
        select.where(QueryBuilder.gte("time",time)).allowFiltering();
        List<TestModel> testModelList = this.cassandraTemplate.select(select, TestModel.class);
        for(TestModel testModel:testModelList){
            String pic_path = mediaServer.getImageHost()+testModel.getPicPath();
            testModel.setPicPath(pic_path);
        }
        return  testModelList;
    }
}
