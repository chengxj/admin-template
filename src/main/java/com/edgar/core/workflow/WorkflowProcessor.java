package com.edgar.core.workflow;

import java.util.List;

public abstract class WorkflowProcessor {

	private List<Activity> activities;

	public void doWorkflow() throws Exception {
		ProcessorContext ctx = new ProcessorContext();
		doWorkflow(ctx);
	}

	public abstract void doWorkflow(ProcessorContext ctx) throws Exception;

	public void setActivities(List<Activity> activities) {
		this.activities = activities;

	}

	public List<Activity> getActivities() {
		return activities;
	}

}