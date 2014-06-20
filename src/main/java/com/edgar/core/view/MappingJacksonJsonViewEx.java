package com.edgar.core.view;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

/**
 * This class will make sure that if there is a single object to
 * transform to JSON, it won't be rendered inside a map.
 * @author 张雨舟
 * @version 1.0
 */
@Deprecated
public class MappingJacksonJsonViewEx extends MappingJacksonJsonView {
	@SuppressWarnings("rawtypes")
	@Override
	protected Object filterModel(Map<String, Object> model) {
		Object result = super.filterModel(model);
		if (!(result instanceof Map)) {
			return result;
		}

		Map map = (Map) result;
		if (map.size() == 1) {
			return map.values().toArray()[0];
		}
		return map;
	}
}