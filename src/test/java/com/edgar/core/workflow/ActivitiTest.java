package com.edgar.core.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
public class ActivitiTest {

	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private ProcessEngine processEngine;
	
	@Test
	public void testIdentity() {
		User activitiUser = new UserEntity("Manager");
		activitiUser.setFirstName("Edgar");
		activitiUser.setLastName("Zhang");
		activitiUser.setPassword("123456");
		processEngine.getIdentityService().saveUser(activitiUser);
	}

	@Test
	public void testDeploy() {
		List<String> resources = new ArrayList<String>();
		resources.add("diagrams/order.bpmn20.xml");
		workFlowService.deploy(resources);

		System.out.println("Number of process definitions: "
				+ workFlowService.getRepositoryService()
						.createProcessDefinitionQuery().count());
	}

	@Test
	public void test() {
		
		processEngine.getIdentityService().setAuthenticatedUserId("ddd");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", 1);
		map.put("orderNo", 10);
		map.put("orderCustomer", "Adonis");
		workFlowService.startProcessInstanceByKey("Order", "d", map);

		// 分配任务
		long count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Manager").count();
		Assert.assertEquals(1, count);
		List<Task> tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Manager").list();
		Task task = tasks.get(0);
		workFlowService.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), "comment");
		workFlowService.getTaskService().complete(task.getId());

		// 出库
		processEngine.getIdentityService().setAuthenticatedUserId("zzzz");
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Inventory").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Inventory").list();
		task = tasks.get(0);
		workFlowService.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), "comment");
		workFlowService.getTaskService().complete(task.getId());

		// 押运员签收
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 出库押运
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());
		
		// 抵达现场
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 客户签收
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Customer").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Customer").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 使用完毕
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Customer").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Customer").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 移交
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Customer").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Customer").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 押运员签收
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 入库押运
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Delivery").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 入库签收
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Inventory").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Inventory").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 入库
		count = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Inventory").count();
		Assert.assertEquals(1, count);
		tasks = workFlowService.getTaskService().createTaskQuery()
				.taskAssignee("Inventory").list();
		task = tasks.get(0);
		workFlowService.getTaskService().complete(task.getId());

		// 完成
		count = workFlowService.getTaskService().createTaskQuery().count();
		Assert.assertEquals(0, count);
	}

	// @Test
	// public void testStart() {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("orderId", 1);
	// map.put("orderNo", 10);
	// map.put("orderCustomer", "Adonis");
	// workFlowService.startProcessInstanceByKey("Order", "d", map);
	// }
	//
	// @Test
	// public void testProecessQuery() {
	// long count = workFlowService.getRuntimeService()
	// .createProcessInstanceQuery().processDefinitionKey("Order")
	// .count();
	// System.out.println(count);
	// count = workFlowService.getRuntimeService().createExecutionQuery()
	// .processDefinitionId("Order").count();
	// System.out.println(count);
	// }
	//
	// @Test
	// public void testTaskQuery() {
	// long count = workFlowService.getTaskService().createTaskQuery().count();
	// System.out.println(count);
	// count = workFlowService.getTaskService().createTaskQuery()
	// .taskAssignee("Edgar").count();
	// System.out.println(count);
	// count = workFlowService.getTaskService().createTaskQuery()
	// .taskAssignee("Edgar").processDefinitionKey("Order")
	// .processInstanceBusinessKey("d")
	// .processVariableValueEquals("orderCustomer", "Adonis").count();
	// System.out.println(count);
	// count = workFlowService.getTaskService().createTaskQuery()
	// .taskCandidateUser("Edgar").count();
	// System.out.println(count);
	// count = workFlowService.getTaskService().createTaskQuery()
	// .taskCandidateUser("Adonis").count();
	// System.out.println(count);
	// }
	//
	// @Test
	// public void testHistoryQuery() {
	// long count = workFlowService.getHistoryService()
	// .createHistoricTaskInstanceQuery().taskAssignee("Edgar")
	// .count();
	// System.out.println(count);
	// count = workFlowService.getHistoryService()
	// .createHistoricTaskInstanceQuery().taskAssignee("Edgar")
	// .finished().count();
	// System.out.println(count);
	// HistoricProcessInstance instance = workFlowService.getHistoryService()
	// .createHistoricProcessInstanceQuery()
	// .processInstanceBusinessKey("d").list().get(0);
	// List<HistoricTaskInstance> historicTaskInstances = workFlowService
	// .getHistoryService().createHistoricTaskInstanceQuery().list();
	// for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
	// System.out.println(historicTaskInstance.getName());
	// }
	// List<HistoricActivityInstance> historicActivityInstances =
	// workFlowService
	// .getHistoryService().createHistoricActivityInstanceQuery()
	// .list();
	// for (HistoricActivityInstance historicActivityInstance :
	// historicActivityInstances) {
	// System.out.println(historicActivityInstance.getActivityName());
	// }
	// }
	//
	// @Test
	// public void testComplete() {
	// List<Task> tasks = workFlowService.getTaskService().createTaskQuery()
	// .taskAssignee("Edgar").list();
	// Task task = tasks.get(0);
	// workFlowService.getTaskService().complete(task.getId());
	// }
	//
	// @Test
	// public void testChaim() {
	// List<Task> tasks = workFlowService.getTaskService().createTaskQuery()
	// .taskCandidateUser("Edgar").list();
	// Task task = tasks.get(0);
	// workFlowService.getTaskService().claim(task.getId(), "Edgar");
	// long count = workFlowService.getTaskService().createTaskQuery()
	// .taskAssignee("Edgar").count();
	// System.out.println(count);
	// }
	//
	// @Test
	// public void testService() {
	// List<Task> tasks = workFlowService.getTaskService().createTaskQuery()
	// .taskCandidateGroup("admin").list();
	// Task task = tasks.get(0);
	// workFlowService.getTaskService().claim(task.getId(), "Edgar");
	// long count = workFlowService.getTaskService().createTaskQuery()
	// .taskAssignee("Edgar").count();
	// System.out.println(count);
	// workFlowService.complete(task.getId());
	// }
	//
	// @Test
	// public void testComment() {
	// List<Task> tasks = workFlowService.getTaskService().createTaskQuery()
	// .taskAssignee("Adonis").list();
	// Task task = tasks.get(0);
	// // tasks = workFlowService.getTaskService().createTaskQuery()
	// // .taskId(task.getId()).taskAssignee("Edgar").list();
	// // task = tasks.get(0);
	// workFlowService.addComment(task.getId(), task.getProcessInstanceId(),
	// "comment Test");
	// List<Comment> comments = workFlowService.getTaskService()
	// .getTaskComments(task.getId());
	// System.out.println(comments.get(0).getFullMessage());
	// }
	//
	// @Test
	// public void testDelegate() {
	// List<Task> tasks = workFlowService.getTaskService().createTaskQuery()
	// .taskAssignee("Edgar").list();
	// Task task = tasks.get(0);
	// workFlowService.getTaskService().delegateTask(task.getId(), "Adonis");
	// // workFlowService.getTaskService().addComment(task.getId(),
	// // task.getProcessInstanceId(), "comment Test");
	// // long count = workFlowService.getHistoryService()
	// // .createHistoricTaskInstanceQuery().taskAssignee("Edgar")
	// // .count();
	// // System.out.println(count);
	// }
}
