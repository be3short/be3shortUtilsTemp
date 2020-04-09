
package com.be3short.data.store;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DynamicFieldObject implements DynamicFields {

	private LinkedHashMap<Object, Object> fields;

	public DynamicFieldObject() {

		fields = new LinkedHashMap<Object, Object>();
	}

	@Override
	public Object set(Object key, Object object) {

		fields.put(key, object);
		return object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E get(Object key) {

		E fetch = null;
		if (fields.containsKey(key)) {
			Object obj = fields.get(key);
			try {
				fetch = (E) obj;
			} catch (Exception badFieldClass) {
				badFieldClass.printStackTrace();
			}
		}
		return fetch;
	}

	@Override
	public HashMap<Object, Object> getValueMap(Object... keys) {

		if (keys.length == 0) {
			return fields;
		}
		HashMap<Object, Object> fieldVals = new HashMap<Object, Object>();
		for (Object key : keys) {
			if (fields.containsKey(key)) {
				fieldVals.put(key, get(key));
			}
		}
		return fieldVals;
	}

	@Override
	public Map<Object, Object> set(Map<Object, Object> map) {

		for (Object valKey : map.keySet()) {
			Object val = map.get(valKey);
			fields.put(valKey, val);
		}
		return map;
	}
}
