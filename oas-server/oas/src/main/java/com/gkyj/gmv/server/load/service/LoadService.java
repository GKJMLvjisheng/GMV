package com.gkyj.gmv.server.load.service;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.gkyj.gmv.server.load.model.LoadModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LoadService {
    @Autowired
    private CassandraOperations cassandraTemplate;

    public List<LoadModel> selectLoadMsg(Integer number){
        Integer maxCircleNumber = 2;
        Select maxCircleNumberData = QueryBuilder.select().from("box").where(QueryBuilder.eq("circle_number",maxCircleNumber)).limit(number).allowFiltering();
        List<LoadModel> loadModelList = this.cassandraTemplate.select(maxCircleNumberData,LoadModel.class);
        return loadModelList;
    }

    public List<LoadModel> selectLoadMsgByPeriod(String startTime,String endTime){
        Select LoadMsgByPeriod = QueryBuilder.select().from("box").where(QueryBuilder.gte("time",startTime)).and(QueryBuilder.lte("time",endTime)).allowFiltering();
        List<LoadModel> loadModelList = this.cassandraTemplate.select(LoadMsgByPeriod,LoadModel.class);
        return loadModelList;
    }

}
