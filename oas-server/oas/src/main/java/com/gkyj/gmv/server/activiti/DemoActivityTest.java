package com.gkyj.gmv.server.activiti;

import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoActivityTest {
	@Autowired
	private RepositoryService repositoryService;//管理和控制发布包和流程定义(包含了一个流程每个环节的结构和行为)的操作
	@Autowired
	private IdentityService identityService; //管理（创建，更新，删除，查询...）群组和用户
	@Autowired
	private TaskService taskService;//所有与任务有关的功能,查询分配给用户或组的任务,创建独立运行任务
	@Autowired
	private FormService formService;//一个可选服务,这个服务提供了启动表单和任务表单两个概念
	@Autowired
	private RuntimeService runtimeService;//负责启动一个流程定义的新实例,获取和保存流程变量,查询流程实例和执行
	@Autowired
	private ManagementService managementService;//在使用Activiti的定制环境中基本上不会用到。 它可以查询数据库的表和表的元数据。另外，它提供了查询和管理异步操作的功能。 
	@Autowired
	private HistoryService historyService; //提供了Activiti引擎的所有历史数据
	
	
	/**部署流程定义*//*
	@Test
	public void deploymentProcessDefinition() {
		*//**获得流程引擎对象*//*
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
				.createDeployment()//创建一个部署对象
				.name("helloworld入门程序")//添加部署的名称
				.addClasspathResource("processes/firstProcess.bpmn")//从classpath的资源中加载，一次只能加载一个文件
				//.addClasspathResource("processes/firstProcess.png")
				.deploy();//完成部署
				System.out.println("部署ID："+deployment.getId());//helloworld
				System.out.println("部署名称："+deployment.getName());//helloworldProcess

	
	}*/
	/**启动流程实例*/
	@Test
	public void startProcessInstance(){
		/**获得流程引擎对象*/
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		
		//流程定义的key
		String processDefinitionKey = "firstProcess";
	
		ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
		.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
	
		System.out.println("流程实例ID:"+pi.getId());//流程实例ID
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID
	}
	
	/**查看我的个人任务**//*
	@Test
	public void queryMyTask() {
		//指定任务办理者
		String assignee = "张三";
		//查询任务列表
		List<Task> tasks = processEngine.getTaskService().createTaskQuery()//创建任务查询对象
				.taskAssignee(assignee).list();
		for(Task t:tasks) {
			System.out.println("taskId:"+t.getId()+",taskName:"+t.getName());
		}
		
	}
	
	*//**完成我的个人任务**//*
	public void completeTask() {
		String taskId = "2502";
		processEngine.getTaskService().complete(taskId);
		System.out.println("完成任务");
	}*/

}
