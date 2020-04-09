
package com.be3short.util.var;

public class Shared<X> {

	private X object;

	public Shared(X obj) {

		setObject(obj);
	}

	/**
	 * @return the object
	 */
	public X getObject() {

		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(X object) {

		this.object = object;
	}
}
