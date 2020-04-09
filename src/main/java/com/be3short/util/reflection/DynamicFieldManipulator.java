
package com.be3short.util.reflection;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class DynamicFieldManipulator extends FieldManipulator {

	private static Logger log = Logger.getLogger(DynamicFieldManipulator.class);

	public Object changeParent;

	DynamicFieldManipulator(Field field, Object parent, Object change_parent) {

		super(field, parent);
		this.changeParent = change_parent;

	}

	DynamicFieldManipulator(FieldManipulator manipulator, Object change_parent) {

		super(manipulator.getField(), manipulator.getParent());
		this.changeParent = change_parent;

	}

	public Object getChange() {

		return getObject(changeParent);
	}

	public Object getChangeParent() {

		return changeParent;
	}

	public boolean updateValue(Object value) {

		return this.updateObject(value);

	}

	public boolean updateChangeValue(Object value) {

		return this.updateObject(value, changeParent);
	}

	public Object getValue() {

		return this.getObject();
	}

	public Object getChangeValue() {

		return this.getObject(changeParent);
	}

	public static DynamicFieldManipulator getManipulator(Field field, Object parent, Object change_parent) {

		DynamicFieldManipulator man = null;
		if (parent != null && change_parent != null) {
			log.debug(
					"creating dynamic field manipulator: " + field.getName() + " x=" + parent + " dx=" + change_parent);
			if (DynamicFieldManipulator.validField(field, parent)) {
				man = new DynamicFieldManipulator(field, parent, change_parent);
			}
		}
		return man;
	}

}
