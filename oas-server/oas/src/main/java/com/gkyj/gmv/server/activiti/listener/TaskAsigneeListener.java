package com.gkyj.gmv.server.activiti.listener;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskAsigneeListener implements TaskListener{
	@Autowired
	private HistoryService historyService;
	@Autowired
	private TaskService taskService;
	private static TaskAsigneeListener taskAsigneeListener;

	@PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {  
		taskAsigneeListener = this;  
		taskAsigneeListener.historyService = this.historyService; 
		taskAsigneeListener.taskService = this.taskService;
        // 初使化时将已静态化的testService实例化
    }
	
	@Override
	public void notify(DelegateTask delegateTask) {
		try {
		  setTaskAsignee(delegateTask);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置任务的完成人
	 * */
	public void setTaskAsignee(DelegateTask currTask) {
		
		//获取实例ID
		String processInstanceId= currTask.getProcessInstanceId();
		//ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		//HistoryService historyService = (HistoryService) context.getBean("HistoryService");
		
		//获取上面的多个任务
		List<HistoricTaskInstance> taskList = taskAsigneeListener.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
		//获取最后面一个任务，即本任务的上一个任务
		HistoricTaskInstance lastTask=taskList.get(0);
		
		//获取上一个任务的执行人
		String lastTaskAsignee=lastTask.getAssignee();
		Map<String,Object> map = taskAsigneeListener.taskService.getVariables(lastTask.getId());
		String name = "";
		if(map!=null) {
			name =(String) map.get("toWho");
		}
		//当前任务
		if(lastTaskAsignee!=null) {
			currTask.setAssignee(name);
		}
	}

	/*@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		historyService = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(HistoryService.class);
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}*/

}
