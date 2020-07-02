
package com.be3short.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PropertyScanner {

	public static CleanProperties getObjectProperties(Object obj, boolean include_class) {

		CleanProperties prop = new CleanProperties();
		scanFields(obj, prop, include_class);
		scanGeneralProperties(obj, prop);
		return prop;

	}

	public static void scanGeneralProperties(Object obj, Properties prop) {

		for (SupportProperty gp : SupportProperty.values()) {
			switch (gp) {
			case CLASS:
				Class<?> objClass = obj.getClass();
				prop.setProperty(gp.key, objClass.getName());
				break;
			}

		}
	}

	public static void scanFields(Object obj, Properties prop, boolean include_class) {

		for (Field field : obj.getClass().getFields()) {
			String fieldName = field.getName();
			if (include_class) {
				fieldName = obj.getClass().getName() + "." + fieldName;
			}
			Object fieldObj = null;
			try {
				fieldObj = field.get(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (fieldObj != null) {
				String fieldObjStr = fieldObj.toString();
				if (!PropertyScanner.isWrapperType(field.getType())) {
					fieldObjStr = XMLParser.serializeObject(fieldObj);
				}
				prop.setProperty(fieldName, fieldObjStr);
			}
		}

	}

	public static <T> T importFromFile(File file) {

		Properties prop = new Properties();
		InputStream input = null;
		T obj = null;
		try {

			input = new FileInputStream(file);

			// load a properties file from class path, inside static method
			prop.load(input);

			if (!prop.containsKey(SupportProperty.CLASS.key)) {
				return (T) prop;
			}
			importFromProperties(prop);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	public static enum SupportProperty {

		CLASS("classType");

		public final String key;

		SupportProperty(String key) {

			this.key = key;
		}
	}

	public static Properties importPropertiesFromFile(File file) {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(file);

			// load a properties file from class path, inside static method
			prop.load(input);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public static Properties importPropertiesFromString(String file) {

		Properties prop = new Properties();
		StringReader input = null;

		input = new StringReader(file);

		// load a properties file from class path, inside static method
		try {
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return prop;
	}

	public static <T> T importFromProperties(Properties prop) {

		T imported = null;
		if (prop.containsKey(SupportProperty.CLASS.key)) {
			String className = prop.get(SupportProperty.CLASS.key).toString();
			try {
				imported = (T) Class.forName(className).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PropertyScanner.importFields(imported, prop);
		} else {
			imported = (T) prop;
		}
		return imported;
	}

	public static void importFields(Object obj, Properties props) {

		for (Field field : obj.getClass().getFields()) {
			String fieldName = field.getName();
			Object fieldObj = null;
			try {
				fieldObj = props.get(field.getName());
				Object ob = PropertyScanner.parseFieldString(fieldObj, field);
				field.set(obj, ob);

			} catch (Exception e) {
				// e.printStackTrace();
			}

		}

	}

	public static HashMap<String, ArrayList<Object>> readDirectoryObjects(File dir) {

		HashMap<String, ArrayList<Object>> objects = new HashMap<String, ArrayList<Object>>();
		for (File fil : dir.listFiles()) {
			Object obj = importFromFile(fil);
			String objClass = obj.getClass().getName();
			if (!objects.containsKey(objClass)) {
				objects.put(objClass, new ArrayList<Object>());
			}
			objects.get(objClass).add(obj);
		}
		return objects;
	}

	public static HashMap<String, Object> mapClassNameToObject(Object... obj) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		for (Object ob : obj) {
			map.put(ob.getClass().getName(), ob);
		}
		return map;
	}

	public static Object parseFieldString(Object value, Field field) {

		String fieldObjStr = value.toString();
		if (!PropertyScanner.isWrapperType(field.getType())) {
			return XMLParser.getObjectFromString(fieldObjStr);
		}
		if ((field.getType().equals(boolean.class)) || (field.getType().equals(Boolean.class))) {
			return Boolean.parseBoolean(value.toString());
		} else if ((field.getType().equals(Double.class)) || (field.getType().equals(double.class))) {
			if (value.toString().contains(":")) {
				Double min = Double.parseDouble(value.toString().split(":")[0]);
				Double max = Double.parseDouble(value.toString().split(":")[1]);
				double random = min + Math.random() * (max - min);
				return random;
			} else {
				return Double.parseDouble(value.toString());
			}
		} else if ((field.getType().equals(Integer.class)) || (field.getType().equals(int.class))) {
			return Integer.parseInt(value.toString());
		} else if ((field.getType().equals(String.class)) || field.getType().equals(String.class)) {
			return value;
		}
		return value;
	}

	static Set<Class<?>> WRAPPER_TYPES = PropertyScanner.getWrapperTypes();

	public static boolean isWrapperType(Class<?> clazz) {

		return WRAPPER_TYPES.contains(clazz);
	}

	static Set<Class<?>> getWrapperTypes() {

		Set<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(String.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		ret.add(boolean.class);
		ret.add(char.class);
		ret.add(byte.class);
		ret.add(short.class);
		ret.add(int.class);
		ret.add(long.class);
		ret.add(float.class);
		ret.add(double.class);
		ret.add(void.class);
		return ret;
	}

	public static void exportObjectPropertiesToFile(File file_path, Object obj) {

		OutputStream output = null;

		try {

			output = new FileOutputStream(file_path);
			CleanProperties props = getObjectProperties(obj, false);
			props.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static void exportPropertiesToFile(File file_path, Properties props) {

		OutputStream output = null;

		try {

			output = new FileOutputStream(file_path);
			props.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
