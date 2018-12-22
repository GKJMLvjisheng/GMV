package com.gkyj.gmv.server.activiti.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.UuidUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gkyj.gmv.server.activiti.model.FirstProject;
import com.gkyj.gmv.server.activiti.model.FirstProjectTask;
import com.gkyj.gmv.server.activiti.model.ProcessDefine;
import com.gkyj.gmv.server.activiti.model.UserGroup;
import com.gkyj.gmv.server.activiti.util.ActivitiUtil;
import com.gkyj.gmv.server.common.UuidPrefix;
import com.gkyj.gmv.server.user.model.UserModel;
import com.gkyj.gmv.server.user.service.UserService;
import com.gkyj.gmv.server.utils.ShiroUtils;

@RestController
public class ActivitiController {
	@Autowired
	private RepositoryService repositoryService;// 管理和控制发布包和流程定义(包含了一个流程每个环节的结构和行为)的操作
	@Autowired
	private IdentityService identityService; // 管理（创建，更新，删除，查询...）群组和用户
	@Autowired
	private TaskService taskService;// 所有与任务有关的功能,查询分配给用户或组的任务,创建独立运行任务
	@Autowired
	private FormService formService;// 一个可选服务,这个服务提供了启动表单和任务表单两个概念
	@Autowired
	private RuntimeService runtimeService;// 负责启动一个流程定义的新实例,获取和保存流程变量,查询流程实例和执行
	@Autowired
	private ManagementService managementService;// 在使用Activiti的定制环境中基本上不会用到。 它可以查询数据库的表和表的元数据。另外，它提供了查询和管理异步操作的功能。
	@Autowired
	private HistoryService historyService; // 提供了Activiti引擎的所有历史数据

	@Autowired
	private UserService userSrevice;
	@Autowired
    ObjectMapper objectMapper;
	private static final Logger log = LogManager.getLogger(ActivitiController.class);


	private static final String PROCESS_FIRST_KEY = "firstProcess";

	/*************************user start****************************/
	/**
	 * 增加用户
	 * 
	 * @param user
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@ResponseBody
	public ResponseEntity<?> addUser(@RequestBody UserModel user) {

		String uuid = UuidUtils.getPrefixUUID(UuidPrefix.USER_MODEL);
		userSrevice.addUser(uuid, user);

		User actUser = identityService.newUser(user.getName());
		actUser.setPassword(user.getPassword());

		identityService.saveUser(actUser);
		identityService.createMembership(user.getName(), user.getGroupId()); // 添加用户和组的关系
		
		return new ResponseEntity.Builder<Integer>().setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	}

	/**
	 * 增加组
	 * 
	 * @param userGroup
	 */
	@RequestMapping(value = "/addOrUpdateGroup", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@ResponseBody
	public ResponseEntity<?> addOrUpdateGroup(@RequestBody UserGroup userGroup) {
		Group g = new GroupEntity(); 
		if(userGroup.getType() != null) {
			Group oldG = identityService.createGroupQuery().groupType(userGroup.getType()).singleResult();
			identityService.deleteGroup(oldG.getId());
		}
		g = identityService.newGroup(userGroup.getId());
		g.setType(generateDtring(32));
		g.setName(userGroup.getName());
		identityService.saveGroup(g);
		return new ResponseEntity.Builder<Integer>().setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	}
	
	private String generateDtring(int length) {
		String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	}

	/**
	 * 获取所有组
	 * 
	 * @return
	 */
	@GetMapping("/getAllGroup")
	@ResponseBody
	public List<UserGroup> getAllGroup() {
		List<Group> groupList = identityService.createGroupQuery().list();
		List<UserGroup> userGroupList = new ArrayList<>();
		for (Group group : groupList) {
			UserGroup userGroup = new UserGroup();
			userGroup.setId(group.getId());
			userGroup.setName(group.getName());
			userGroup.setType(group.getType());
			userGroupList.add(userGroup);
		}
		return userGroupList;
	}

	/**
	 * 获取所有用户
	 * 
	 * @return
	 */
	@GetMapping("/getAllUser")
	@ResponseBody
	public Object getAllUser() {
		List<User> userList = identityService.createUserQuery().list();
		return toMyUser(userList);
	}

	private List<UserModel> toMyUser(List<User> userList) {
		List<UserModel> myUserList = new ArrayList<>();
		for (User user : userList) {
			UserModel myUser = new UserModel();
			myUser.setName(user.getId());
			myUser.setPassword(user.getPassword());
			Group group = identityService.createGroupQuery().groupMember(user.getId()).singleResult();
			myUser.setGroupId(group.getId());
			myUserList.add(myUser);
		}
		return myUserList;
	}

	/*************************user end & process start****************************/
	
	/**
         * 新建一个空模型
     */
    @RequestMapping(value="/create", method = {RequestMethod.POST,RequestMethod.GET})
    public void newModel(HttpServletResponse response) throws IOException {
        //RepositoryService repositoryService = processEngine.getRepositoryService();
        //初始化一个空模型
        Model model = repositoryService.newModel();
 
        //设置一些默认信息
        String name = "new-process";
        String description = "";
        int revision = 1;
        String key = "process";
 
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);
 
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());
 
        repositoryService.saveModel(model);
        String id = model.getId();
 
        //完善ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace","http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        repositoryService.addModelEditorSource(id,editorNode.toString().getBytes("utf-8"));
        response.sendRedirect("/modeler.html?modelId="+id); 
    }
    
   
    
    /**
         * 获取所有模型
     */
    @RequestMapping(value="/modelList", method = RequestMethod.GET)
    @ResponseBody
    public Object modelList(){
    	return repositoryService.createModelQuery().list();
    }
    
	/**
	   * 删除模型
	 * @param idsMap
	 * @return
	 */
    @RequestMapping(value="/deleteModel", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteModel(@RequestBody Map<String,List<String>> idsMap){
    	List<String> modelIds = new ArrayList<>();
    	if(idsMap!=null) {
    		modelIds = idsMap.get("ids");
    	}
    	if(modelIds!=null && modelIds.size()>0) {
    		for(String modelId : modelIds) {
    			repositoryService.deleteModel(modelId);
    		}
    	}
    	return new ResponseEntity.Builder<Integer>().setData(0).setErrorCode(ErrorCode.SUCCESS).build();
    }
    
 
    /**
         * 发布模型为流程定义
     */
    @RequestMapping(value="/deploy", method = RequestMethod.POST)
    @ResponseBody
    public Object deploy(@RequestBody String modelId) throws Exception {
    	JSONObject jb = JSON.parseObject(modelId);
    	modelId = (String)jb.get("modelId");
        //获取模型
        //RepositoryService repositoryService = processEngine.getRepositoryService();
        Model modelData = repositoryService.getModel(modelId);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
 
        if (bytes == null) {
            return "模型数据为空，请先设计流程并成功保存，再进行发布。";
        }
 
        JsonNode modelNode = new ObjectMapper().readTree(bytes);
 
        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        if(model.getProcesses().size()==0){
            return "数据模型不符要求，请至少设计一条主线流程。";
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
 
        //发布流程
        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, new String(bpmnBytes, "UTF-8"))
                .deploy();
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);
 
        return new ResponseEntity.Builder<Integer>().setData(0).setErrorCode(ErrorCode.SUCCESS).build();
    }

	
	/**
	 * 开启流程
	 * 
	 * @param project
	 * @return
	 */
	@RequestMapping(value = "/startProcess", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?>  startProcess(@RequestBody FirstProject project) {
		UserModel user = ShiroUtils.getUser();
		identityService.setAuthenticatedUserId(user.getName());
		// 开始流程
		ProcessInstance projectInstance = runtimeService.startProcessInstanceByKey(project.getKeyName());
		// 查询当前任务
		Task currentTask = taskService.createTaskQuery().processInstanceId(projectInstance.getId()).singleResult();
		// 申明任务
		taskService.claim(currentTask.getId(), user.getName());//认领任务，让用户成为任务的执行者

		//用map设置
		Map<String, Object> vars = new HashMap<>();
		vars.put("applyUser", user.getName());
		vars.put("reason", project.getReason());
		vars.put("toWho",project.getAuditor());
		//用set设置
		//taskService.setVariable(taskId, variableName, value);

		//taskService.addCandidateUser(currentTask.getId(), "zzz");//设置任务候选人
		//加入流程变量
		taskService.complete(currentTask.getId(), vars);
		
		return new ResponseEntity.Builder<Integer>().setData(0).setErrorCode(ErrorCode.SUCCESS).build();

	}

	/**
	 * 获取我的流程
	 * 
	 * @return
	 */
	@GetMapping("/getMyProcess")
	@ResponseBody
	public List<FirstProject> getMyProcess() {
		UserModel user = ShiroUtils.getUser();
		List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().involvedUser(user.getName())//.startedBy(user.getName())
				.list();
		List<FirstProject> pList = new ArrayList<>();
		for (ProcessInstance instance : instanceList) {
			String starter = runtimeService.getVariable(instance.getId(), "applyUser", String.class);
			if(starter == null || !starter.equals(user.getName())) {
				continue;
			}
			FirstProject p = getProcess(instance);
			p.setProcessId(instance.getId());
			pList.add(p);
			
		}
		
		return pList;
	}

	private FirstProject getProcess(ProcessInstance instance) {
		String reason = runtimeService.getVariable(instance.getId(), "reason", String.class);
		String auditor = runtimeService.getVariable(instance.getId(), "auditor", String.class);
		Date auditTime = runtimeService.getVariable(instance.getId(), "auditTime", Date.class);
		String result = runtimeService.getVariable(instance.getId(), "result", String.class);
		FirstProject fProject = new FirstProject();
		//fProject.setApplyUser(instance.getStartUserId());
		fProject.setReason(reason);
		//Date startTime = instance.getStartTime();
		//fProject.setApplyTime(startTime);
		fProject.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
		fProject.setAuditor(auditor);
		fProject.setAuditTime(auditTime);
		fProject.setResult(result);
		return fProject;
	}

	/**
	 * 获取我审批的列表
	 * 
	 * @return
	 */
	@GetMapping("/myAudit")
	@ResponseBody
	public List<FirstProjectTask> myAudit() {
		UserModel user = ShiroUtils.getUser();
		List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(user.getName()).orderByTaskCreateTime().desc().list();
		List<Task> taskListAssignee = taskService.createTaskQuery().taskAssignee(user.getName()).orderByTaskCreateTime().desc().list();
		/*List<Task> taskList = taskService.createNativeTaskQuery().sql("select * from "+managementService.getTableName(Task.class)
								+ " t where t.assignee_=#{userId} order by t.create_time_ desc").parameter("userId", user.getName()).list();*/
		if(taskListAssignee!=null && taskListAssignee.size()>0) {
			taskList.addAll(taskListAssignee);
		}
		List<FirstProjectTask> vacTaskList = new ArrayList<>();
		for (Task task : taskList) {
			FirstProjectTask pTask = new FirstProjectTask();
			pTask.setId(task.getId());
			pTask.setName(task.getName());
			pTask.setCreateTime(task.getCreateTime());
			String instanceId = task.getProcessInstanceId();
			ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId)
					.singleResult();
			FirstProject p = getProcess(instance);
			p.setProcessId(instance.getId());
			pTask.setProject(p);
			vacTaskList.add(pTask);
		}
		return vacTaskList;
	}

	/**
	 * 操作审批
	 * 
	 * @param pTask
	 * @return
	 */
	@RequestMapping(value = "/passAudit", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?> passAudit(@RequestBody FirstProjectTask pTask) {
		UserModel user = ShiroUtils.getUser();
		String taskId = pTask.getId();
		String result = pTask.getProject().getResult();
		Map<String, Object> vars = new HashMap<>();
		vars.put("result", result);
		vars.put("auditor", user.getName());
		vars.put("auditTime", new Date());
		taskService.claim(taskId, user.getName());//认领任务，让用户成为任务的执行者
		taskService.complete(taskId, vars);
		
		return new ResponseEntity.Builder<Integer>().setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	}
	
	/**
	 * 获取我申请的历史流程
	 * 
	 * @param userName
	 * @return
	 */
	@GetMapping("/myProjectRecord")
	@ResponseBody
	public List<FirstProject> myProjectRecord() {
		List<FirstProject> vacList = new ArrayList<>();
		List<ProcessDefinition> processList = repositoryService.createProcessDefinitionQuery().list();
		if(processList!=null && processList.size()>0) {
			for(ProcessDefinition p:processList) {
				List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
						.processDefinitionId(p.getId()).startedBy(ShiroUtils.getLoginName()).finished()
						.orderByProcessInstanceEndTime().desc().list();

				
				for (HistoricProcessInstance hisInstance : hisProInstance) {
					FirstProject fp = new FirstProject();
					fp.setApplyUser(hisInstance.getStartUserId());
					fp.setApplyTime(hisInstance.getStartTime());
					fp.setApplyStatus("申请结束");
					fp.setProcessId(hisInstance.getId());
					List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
							.processInstanceId(hisInstance.getId()).list();
					ActivitiUtil.setVars(fp, varInstanceList);
					vacList.add(fp);
				}
			}
		}
		
		return vacList;
	}

	/**
	 * 我审批的历史流程
	 * 
	 * @return
	 */
	@GetMapping("/myAuditRecord")
	@ResponseBody
	public List<FirstProject> myAuditRecord() {
		List<FirstProject> vacList = new ArrayList<>();
		List<ProcessDefinition> processList = repositoryService.createProcessDefinitionQuery().list();
		if(processList!=null && processList.size()>0) {
			for(ProcessDefinition p:processList) {
				List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
						.processDefinitionId(p.getId()).involvedUser(ShiroUtils.getLoginName())//.finished()
						.orderByProcessInstanceEndTime().desc().list();

				for (HistoricProcessInstance hisInstance : hisProInstance) {
					if(hisInstance.getStartUserId()!=null && hisInstance.getStartUserId().equals(ShiroUtils.getLoginName())) {
						break;
					}
					List<HistoricTaskInstance> hisTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
							.processInstanceId(hisInstance.getId()).taskAssignee(ShiroUtils.getLoginName())//.processFinished()
							// .taskNameIn(auditTaskNameList)
							.orderByHistoricTaskInstanceEndTime().desc().list();
					boolean isMyAudit = false;
					for (HistoricTaskInstance taskInstance : hisTaskInstanceList) {
						if (taskInstance.getAssignee().equals(ShiroUtils.getLoginName())) {
							isMyAudit = true;
						}
					}
					if (!isMyAudit) {
						continue;
					}
					FirstProject fp = new FirstProject();
					fp.setApplyUser(hisInstance.getStartUserId());
					fp.setApplyStatus("申请结束");
					fp.setApplyTime(hisInstance.getStartTime());
					fp.setProcessId(hisInstance.getId());
					List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
							.processInstanceId(hisInstance.getId()).list();
					ActivitiUtil.setVars(fp, varInstanceList);
					vacList.add(fp);
				}
			}
		}
		return vacList;
	}

/*	@RequestMapping(value = "/queryProImg", method = RequestMethod.POST, consumes = "application/json")
	public void queryProImg(String processInstanceId) throws Exception {
		// 获取历史流程实例
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(PROCESS_FIRST_KEY).singleResult();

		// 根据流程定义获取输入流
		InputStream is = repositoryService.getProcessDiagram(processInstance.getProcessDefinitionId());
		BufferedImage bi = ImageIO.read(is);
		File file = new File("demo2.png");
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		ImageIO.write(bi, "png", fos);
		fos.close();
		is.close();
		System.out.println("图片生成成功");

		List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("userId").list();
		for (Task t : tasks) {
			System.out.println(t.getName());
		}
	}*/

	/**
	 * @Note 读取流程资源
	 * @param processDefinitionId 流程定义ID
	 */
	@RequestMapping(value = "/getProcessImg", method = RequestMethod.POST)
	public void getProcessImg(@RequestBody String pProcessInstanceId, HttpServletResponse response) throws Exception {
/*		JSONObject jb = JSON.parseObject(jsonReq);
    	String pProcessInstanceId = (String)jb.get("pProcessInstanceId");*/
		String [] str = pProcessInstanceId.split(",");
		pProcessInstanceId = (str!=null && str.length>0 ? str[0]:"");
		String processKey = (str!=null && str.length>1 ? str[1]:PROCESS_FIRST_KEY);
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		List<ProcessDefinition> pd = pdq.processDefinitionKey(processKey).list();

		if (StringUtils.isEmpty(pProcessInstanceId) == false) {
			getActivitiProccessImage(pProcessInstanceId, response);
			// ProcessDiagramGenerator.generateDiagram(pde, "png",
			// getRuntimeService().getActiveActivityIds(processInstanceId));
		} else {
			// 通过接口读取
			InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.get(0).getDeploymentId(),processKey);

			// 输出资源内容到相应对象
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		}
	}

	/**
	 * 获取流程图像，已执行节点和流程线高亮显示
	 */
	public void getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response) {
		// logger.info("[开始]-获取流程图图像");
		try {
			// 获取历史流程实例
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(pProcessInstanceId).singleResult();

			if (historicProcessInstance == null) {
				// throw new BusinessException("获取流程实例ID[" + pProcessInstanceId +
				// "]对应的历史流程实例失败！");
			} else {
				// 获取流程定义
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

				// 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
				List<HistoricActivityInstance> historicActivityInstanceList = historyService
						.createHistoricActivityInstanceQuery().processInstanceId(pProcessInstanceId)
						.orderByHistoricActivityInstanceId().asc().list();

				// 已执行的节点ID集合
				List<String> executedActivityIdList = new ArrayList<String>();
				// int index = 1;
				// logger.info("获取已经执行的节点ID");
				for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
					executedActivityIdList.add(activityInstance.getActivityId());

					// logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + "
					// : " +activityInstance.getActivityName());
					// index++;
				}

				BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

				// 已执行的线集合
				List<String> flowIds = new ArrayList<String>();
				// 获取流程走过的线 (getHighLightedFlows是下面的方法)
				flowIds = getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);

				// 获取流程图图像字符流
				// ProcessDiagramGenerator pec2 =
				// ProcessEngines.getProcessEngine(PROCESS_FIRST_KEY).getProcessEngineConfiguration().getProcessDiagramGenerator();
				ProcessDiagramGenerator pec = ProcessEngines.getDefaultProcessEngine().getProcessEngineConfiguration()
						.getProcessDiagramGenerator();

				// 配置字体
				InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds, "宋体",
						"微软雅黑", "黑体", null, 2.0);

				response.setContentType("image/png");
				OutputStream os = response.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				imageStream.close();
			}
			// logger.info("[完成]-获取流程图图像");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// logger.error("【异常】-获取流程图失败！" + e.getMessage());
			// throw new BusinessException("获取流程图失败！" + e.getMessage());
		}
	}

	public List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
			// 对历史流程节点进行遍历
			// 得到节点定义的详细信息
			FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess()
					.getFlowElement(historicActivityInstances.get(i).getActivityId());

			List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
			FlowNode sameActivityImpl1 = null;

			HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
			HistoricActivityInstance activityImp2_;

			for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
				activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

				/*if (activityImpl_.getActivityType().equals("userTask")
						&& activityImp2_.getActivityType().equals("userTask")
						&& df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) // 都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
				{

				} else {
					sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess()
							.getFlowElement(historicActivityInstances.get(k).getActivityId());// 找到紧跟在后面的一个节点
					break;
				}*/
				if(df.format(activityImpl_.getEndTime()).equals(df.format(activityImp2_.getStartTime()))){
					sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess()
							.getFlowElement(historicActivityInstances.get(k).getActivityId());// 找到紧跟在后面的一个节点
					break;
				}

			}
			sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
		/*	for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

				if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
					FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess()
							.getFlowElement(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {// 有不相同跳出循环
					break;
				}
			}*/
			List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // 取出节点的所有出去的线

			for (SequenceFlow pvmTransition : pvmTransitions) {// 对所有的线进行遍历
				FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess()
						.getFlowElement(pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}

		}
		return highFlows;

	}
	
	/**
	 * 获取流程定义,所有流程
	 * 
	 * @return
	 */
	@GetMapping("/findProcessDefinition")
	@ResponseBody
	public List<ProcessDefine> findProcessDefinition() {
		List<ProcessDefine> result = new ArrayList<>();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		if(list != null && list.size()>0) {
			for(ProcessDefinition p:list) {
				ProcessDefine pd = new ProcessDefine();
				pd.setDefineId(p.getId()); //流程定义id
				pd.setName(p.getName());
				pd.setDeployId(p.getDeploymentId());
				pd.setKey(p.getKey());
				result.add(pd);
			}
		}
		return result;
	}
	
	/**
	 * 删除流程定义(非级联)
	 * @param ids 部署对象id，加true则可以删掉 
	 * @return
	 */
	@RequestMapping(value = "/deleteProcessDefinition", method = RequestMethod.POST, consumes = "application/json")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@ResponseBody
	public ResponseEntity<?> deleteProcessDefinition(@RequestBody Map<String,List<String>> idsMap) {
		ErrorCode result  = ErrorCode.SUCCESS;
		List<String> ids = idsMap.get("ids");
		try {
			if(ids!=null && ids.size()>0) {
				for(String id:ids) {
					repositoryService.deleteDeployment(id,true);
				}
			}
		}catch(Exception e) {
			result = ErrorCode.GENERAL_ERROR;
			e.printStackTrace();
			throw e;
		}
		return new ResponseEntity.Builder<Integer>().setData(0).setErrorCode(result).build();
		
	}

}
