
package com.be3short.data.store;

import java.util.Map;

/**
 * the dynamic fields interface allows for an object that contains fields that
 * are not explicitly defined, allowing for the objects to be extended without
 * making modifications to the objects themselves.
 * 
 * @author be3short
 *
 */
public interface DynamicFields {

	/**
	 * set a field
	 * 
	 * @param key
	 *            field access key
	 * @param object
	 *            field value
	 * @return the field value that has been set
	 */
	public Object set(Object key, Object object);

	public Map<Object, Object> set(Map<Object, Object> map);

	public <E> E get(Object key);

	public Map<Object, Object> getValueMap(Object... key);
}
