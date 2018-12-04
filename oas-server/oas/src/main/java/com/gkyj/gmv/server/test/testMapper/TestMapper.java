package com.gkyj.gmv.server.test.testMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Component;
import com.gkyj.gmv.server.test.testModel.TestModel;

@Component
public class TestMapper{
	@Autowired
	private CassandraTemplate cassandraTemplate; 
	public List<TestModel> getAll() {
	return cassandraTemplate.select("SELECT * FROM box_info",TestModel.class);
	}  
}
