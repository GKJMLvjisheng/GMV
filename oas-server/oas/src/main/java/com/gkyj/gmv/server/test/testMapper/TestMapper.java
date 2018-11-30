package com.gkyj.gmv.server.test.testMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;

import com.gkyj.gmv.server.test.testModel.TestModel;

@Repository
public class TestMapper{
	@Autowired
	private CassandraTemplate cassandraTemplate; 
	
	public List<TestModel> getAll() {
		return cassandraTemplate.select("SELECT box_id,circle_number,parameter,pic_path,value,time FROM box_info", TestModel.class);
	}  
}
