package com.edgar.core.workflow;
import java.util.HashMap;
import java.util.Map;

public class ProcessorContext {

	private Map<String, Object> objects = new HashMap<String, Object>();

	public void addObject(String key, Object o) {
		objects.put(key, o);
	}

	public Object getObject(String key) {
		if (objects.containsKey(key)) {
			return objects.get(key);
		}
		return null;
	}

	public void removeObject(String key) {
		if (objects.containsKey(key)) {
			objects.remove(key);
		}
	}

}