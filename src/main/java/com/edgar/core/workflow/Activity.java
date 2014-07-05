package com.edgar.core.workflow;
public interface Activity {

	public ProcessorContext execute(ProcessorContext context) throws Exception;

}