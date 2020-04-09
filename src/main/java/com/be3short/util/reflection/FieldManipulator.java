
package com.be3short.util.reflection;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class FieldManipulator {

	private static Logger log = Logger.getLogger(FieldManipulator.class);

	private Field field;

	private Object parent;

	FieldManipulator(Field field, Object parent) {

		this.field = field;
		this.parent = parent;
	}

	public boolean updateObject(Object update) {

		return updateObject(update, parent);
	}

	public boolean updateObject(Object value, Object parent) {

		boolean success = true;

		try {

			field.set(parent, value);
		} catch (Exception e) {
			success = false;

			e.printStackTrace();
		}
		return success;
	}

	public Object getObject() {

		try {
			return field.get(parent);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}

	}

	public Object getObject(Object alternate) {

		try {
			return field.get(alternate);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}

	public Object getParent() {

		return parent;
	}

	public Field getField() {

		return field;
	}

	public static boolean isNumericalValue(FieldManipulator manipulator, Object update) {

		boolean valid = true;
		if (manipulator.field.getType().equals(double.class) || manipulator.field.getType().equals(Double.class)) {
			try {
				Double val = (Double) update;
				valid = valid && (!Double.isNaN(val) && Double.isFinite(val));
				if (!valid) {

					throw new Exception("Invalid number entered for field " + manipulator.field.getName() + " of state "
							+ manipulator.parent.getClass().getSimpleName() + " : " + val);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return valid;
	}

	public static boolean validField(Field field, Object parent) {

		boolean valid = FieldMapping.getGlobal().validField(field, parent);
		if (valid) {
			setAccessable(field);
		}
		return true;
	}

	public static String getFieldName(Field field) {

		String name = null;
		if (field != null) {
			try {
				// log.debug("getting name for field " + field.toString());
				name = field.getName();
			} catch (Exception noAccess) {

				log.error("unable to get name for field " + field.toString(), noAccess);

			}
			field.getName();
		}
		return name;
	}

	public static boolean setAccessable(Field field) {

		boolean success = true;
		try {
			String name = getFieldName(field);
			if (name == null) {
				throw new Exception();
			}
			if (!field.isAccessible()) {
				log.debug("setting field accessable: " + field.getName());
				field.setAccessible(true);
			}
		} catch (Exception noAccess) {
			success = false;
			log.error("unable to make field accessable " + field.getName(), noAccess);

		}
		return success;
	}

	public static FieldManipulator getManipulator(Field field, Object parent) {

		FieldManipulator man = null;
		if (parent != null) {
			if (FieldManipulator.validField(field, parent)) {
				man = new FieldManipulator(field, parent);
			}
		}
		return man;
	}

}
