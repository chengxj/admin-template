package com.edgar.core.workflow;

import java.util.Iterator;
import java.util.List;

public class OrderWorkflowProcessor extends WorkflowProcessor {

	@Override
	public void doWorkflow(ProcessorContext ctx) throws Exception {

		List<Activity> actlist = getActivities();
		if (actlist != null) {
			Iterator<Activity> i = actlist.iterator();
			while (i.hasNext()) {
				Activity a = (Activity) i.next();
				ctx = a.execute(ctx);
			}
		}

	}

}