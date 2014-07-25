package com.edgar.core.workflow;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WorkflowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowProcessor.class);
	
	private List<Activity> activities;

	public void doWorkflow() throws Exception {
		LOGGER.info("activities: {}", activities);
		ProcessorContext ctx = new ProcessorContext();
		
		List<Activity> actlist = getActivities();
		if (actlist != null) {
			Iterator<Activity> i = actlist.iterator();
			while (i.hasNext()) {
				Activity a = (Activity) i.next();
//				ctx = a.execute(ctx);
				a.getHandler();
			}
		}
		
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