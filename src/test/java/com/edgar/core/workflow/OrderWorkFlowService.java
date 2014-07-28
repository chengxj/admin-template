package com.edgar.core.workflow;

import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;

public class OrderWorkFlowService {
	/**
	 * 流程定义的服务接口
	 */
	private RepositoryService repositoryService;

	/**
	 * 流程实例的服务接口
	 */
	private RuntimeService runtimeService;

	/**
	 * 待办任务的服务接口
	 */
	private TaskService taskService;

	private HistoryService historyService;

	public void setProcessEngine(ProcessEngine processEngine) {
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
		historyService = processEngine.getHistoryService();
	}

	/**
	 * 部署流程，调用{@link RepositoryService#createDeployment()}、
	 * {@link DeploymentBuilder#addClasspathResource(String)}、
	 * {@link DeploymentBuilder#deploy()}方法部署流程定义文件
	 * 
	 * @param resources
	 *            资源集合
	 * @return 流程部署对象
	 * @see RepositoryService#createDeployment();
	 * @see DeploymentBuilder#addClasspathResource(String)
	 * @see DeploymentBuilder#deploy()
	 */
	public Deployment deploy(List<String> resources) {
		DeploymentBuilder deploymentBuilder = repositoryService
				.createDeployment();
		for (String resource : resources) {
			deploymentBuilder.addClasspathResource(resource);
		}
		return deploymentBuilder.deploy();
	}

	/**
	 * 根据流程定义的键值和业务键值启动流程实例
	 * {@link RuntimeService#startProcessInstanceByKey(String, String)}、
	 * {@link ProcessInstance#getId()}
	 * 
	 * @param processDefinitionKey
	 *            流程定义的键值
	 * @param businessKey
	 *            业务键值
	 * @return 流程实例ID
	 * @see RuntimeService#startProcessInstanceByKey(String, String)
	 * @see ProcessInstance#getId()
	 */
	public String startProcessInstanceByKey(String processDefinitionKey,
			String businessKey) {
		return runtimeService.startProcessInstanceByKey(processDefinitionKey,
				businessKey).getId();
	}

	/**
	 * 根据流程定义ID和业务键值启动流程实例
	 * {@link RuntimeService#startProcessInstanceByKey(String, String, Map)}、
	 * {@link ProcessInstance#getId()}
	 * 
	 * @param processDefinitionKey
	 *            流程定义的键值
	 * @param businessKey
	 *            业务键值
	 * @param variables
	 *            流程变量
	 * @return 流程实例ID
	 * @see RuntimeService#startProcessInstanceByKey(String, String, Map)
	 * @see ProcessInstance#getId()
	 */
	public String startProcessInstanceByKey(String processDefinitionKey,
			String businessKey, Map<String, Object> variables) {
		return runtimeService.startProcessInstanceByKey(processDefinitionKey,
				businessKey, variables).getId();
	}

	public void addComment(String taskId, String processInstanceId,
			String comment) {
		taskService.addComment(taskId, processInstanceId, comment);
	}

	public void complete(String taskId) {
		taskService.complete(taskId);
	}

	public void claim(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}

	public void delegate(String taskId, String delegateUserId) {
		taskService.delegateTask(taskId, delegateUserId);
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}
}
