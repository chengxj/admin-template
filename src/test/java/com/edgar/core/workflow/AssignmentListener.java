package com.edgar.core.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 为每个usertask任务分配用户组的监听器
 * <p />
 * 任务与用户角色之间的关联由服务的具体实现管理。 通过调用
 * {@link IIdentityService#findCandidateUsers(String)}查找到每个任务拥有的用户列表。 然后通过
 * {@link DelegateTask#addCandidateUsers(java.util.Collection)}分配用户
 * 
 * @author 张雨舟
 * 
 */
@Service
public class AssignmentListener implements TaskListener {

	@Autowired
	private ProcessEngine processEngine;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AssignmentListener.class);

	@Override
	public void notify(DelegateTask delegateTask) {
		if (StringUtils.startsWith(delegateTask.getTaskDefinitionKey(), "A_")) {
			delegateTask.setAssignee("-1");
			processEngine.getTaskService().complete(delegateTask.getId());
		} else if (StringUtils.startsWith(delegateTask.getTaskDefinitionKey(),
				"M_")) {
			delegateTask.setAssignee("Manager");
		} else if (StringUtils.startsWith(delegateTask.getTaskDefinitionKey(),
				"I_")) {
			delegateTask.setAssignee("Inventory");
		} else if (StringUtils.startsWith(delegateTask.getTaskDefinitionKey(),
				"D_")) {
			delegateTask.setAssignee("Delivery");
		} else if (StringUtils.startsWith(delegateTask.getTaskDefinitionKey(),
				"C_")) {
			delegateTask.setAssignee("Customer");
		}
		// LOGGER.debug("分配用户");
		// LOGGER.debug(delegateTask.getTaskDefinitionKey());
		// List<String> candidateUsers = new ArrayList<String>();
		// candidateUsers.add("Edgar");

	}

}