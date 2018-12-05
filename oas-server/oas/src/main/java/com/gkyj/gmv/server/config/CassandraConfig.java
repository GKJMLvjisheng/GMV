//package com.gkyj.gmv.server.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.Session;
//@Configuration
//public class CassandraConfig {
//	/*
//	   * Use the standard Cassandra driver API to create a com.datastax.driver.core.Session instance.
//	   */
//	    @Bean
//	    public Session session() {
//		System.out.print("start Cassandra connect");
//	    Cluster cluster = Cluster.builder()
//	    		                 .addContactPoints("10.0.0.88")
//	    		                 .withCredentials("gkyj", "123456")
//	    		                 .build();
//	    return cluster.connect("gmv_db");
//	  }
//
//}
