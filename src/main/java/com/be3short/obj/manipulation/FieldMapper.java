
package com.be3short.obj.manipulation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

import com.be3short.io.general.FileSystemInteractor;
import com.be3short.util.io.XMLParser;

public class FieldMapper {

	private ArrayList<Field> skipFields;

	public static Reflections reflections;

	private HashMap<String, ArrayList<Field>> elements;

	private ArrayList<Class<?>> scannedClasses;

	private boolean noFile;

	private ArrayList<Field> getSkipFields(Class<?>... skip_classes) {

		ArrayList<Field> fieldz = new ArrayList<Field>();
		for (Class<?> skip_class : skip_classes) {
			fieldz.addAll(Arrays.asList(skip_class.getDeclaredFields()));
		}
		return fieldz;
	}

	private void getReflections() {

		if (reflections == null) {

			Reflections.log = null;
			reflections = new Reflections();

		}
	}

	public boolean isState(Class<?> potential_state) {

		String clas = potential_state.getName();
		return elements.containsKey(potential_state);
	}

	public ArrayList<Field> getClassFields(Class<?> class_to_find) {

		String clas = class_to_find.getName();
		ArrayList<Field> fields = null;
		try {
			if (elements.containsKey(clas)) {
				fields = elements.get(clas);
			} else {
				getReflections();
				HashMap<String, ArrayList<Field>> newStates = getClassFieldsMappingOfType(class_to_find, false);
				elements = newStates;
				// System.out.println(XMLParser.serializeObject(elements));
				fields = elements.get(clas);
			}
		} catch (Exception notSearched) {
			notSearched.printStackTrace();

		}
		return fields;
	}

	public <T extends Annotation> Set<Class<?>> getPackageClassesAnnotated(Class<T> clas) {

		return reflections.getTypesAnnotatedWith(clas);
	}

	/*
	 * Creates a set containing all classes included within the project packages and
	 * dependencies that are a sub type of the specified input. This is used to find
	 * specific types of classes so that their properties, ie fields and methods,
	 * can be determined for later use
	 */
	public <T> Set<Class<? extends T>> getPackageClassesOfType(Class<T> clas) {

		return reflections.getSubTypesOf(clas);
	}

	/*
	 * Creates a mapping containing all declared fields for every state class
	 * included in the project and dependencies. This mapping allows for values to
	 * be updated without changing pointers.
	 */
	public void makeAllFieldsAccessable(HashMap<String, ArrayList<Field>> elements) {

		for (String clas : elements.keySet()) {
			for (Field field : elements.get(clas)) {
				field.setAccessible(true);
			}
		}
		// System.out.println(XMLParser.serializeObject(elements));
	}

	public <T> HashMap<String, ArrayList<Field>> initializeClassFieldMap(ArrayList<Class<?>> search_classes) {

		HashMap<String, ArrayList<Field>> elements = new HashMap<String, ArrayList<Field>>();
		for (Class<?> clas : search_classes) {

			elements.put(clas.getName(), new ArrayList<Field>());
		}
		return elements;
	}

	/*
	 * Creates a mapping containing all declared fields for every state class
	 * included in the project and dependencies. This mapping allows for values to
	 * be updated without changing pointers.
	 */
	public void getClassFieldMapping(HashMap<String, ArrayList<Field>> elements, ArrayList<Class<?>> search_classes,
			Class<?> search_class) {

		for (Class<?> clas : search_classes) {
			for (Field fields : clas.getDeclaredFields()) {
				if (!fields.getName().contains("$SWITCH_TABLE$") && !fields.getType().equals(search_class)
						&& !skipFields.contains(fields))// &&
				// !skipFields.contains(fields))
				{
					if (!elements.get(clas.getName()).contains(fields)) {
						elements.get(clas.getName()).add(fields);
					}
				}
			}
			if (search_classes.contains(clas.getSuperclass())) {
				Class<?> superClass = clas.getSuperclass();
				ArrayList<Field> extendedFields = new ArrayList<Field>(Arrays.asList(superClass.getDeclaredFields()));
				while (search_classes.contains(superClass)) {
					for (Field fi : extendedFields) {
						if (!fi.getName().contains("$SWITCH_TABLE$") && !fi.getType().equals(search_class)
								&& !skipFields.contains(fi))// &&
						{
							if (!elements.get(clas.getName()).contains(fi)) {
								elements.get(clas.getName()).add(fi);
							}
						}
					}
					clas = superClass;
					superClass = superClass.getSuperclass();
					extendedFields.addAll(Arrays.asList(superClass.getDeclaredFields()));

				}
				// elements.put(clas, extendedFields.toArray(new Field[extendedFields.size()]));
			}

		}
		makeAllFieldsAccessable(elements);

	}

	public <T> HashMap<String, ArrayList<Field>> getClassFieldsMappingOfType(Class<T> class_search) {

		return getClassFieldsMappingOfType(class_search, true);
	}

	public <T> HashMap<String, ArrayList<Field>> getClassFieldsMappingOfType(Class<T> class_search, boolean try_file) {

		try

		{
			if (!try_file) {
				throw new Exception();
			}
			Object elefie = XMLParser.getObject(new File(".fieldmapping.xml"));
			if (elefie != null) {
				if (elefie.getClass().equals(ArrayList.class)) {
					ArrayList<Class<?>> map = (ArrayList<Class<?>>) elefie;
					HashMap<String, ArrayList<Field>> mapping = getClassFieldsMapping(map);
					return mapping;
				}
			}
			throw new Exception();

		} catch (Exception e) {

			scannedClasses = getClassesOfType(class_search);//
			// getPackageClassesAnnotated(State.class);
			HashMap<String, ArrayList<Field>> mapping = getClassFieldsMapping(scannedClasses);
			if (!noFile) {
				File outputFile = new File(".fieldmapping.xml");
				FileSystemInteractor.createOutputFile(outputFile, XMLParser.serializeObject(scannedClasses));
			} // System.out.println(XMLParser.serializeObject(elems));
			return mapping;
		}

	}

	public HashMap<String, ArrayList<Field>> getClassFieldsMapping(ArrayList<Class<?>> class_search) {

		HashMap<String, ArrayList<Field>> eles = new HashMap<String, ArrayList<Field>>();
		HashMap<String, ArrayList<Field>> elems = initializeClassFieldMap(class_search);
		getClassFieldMapping(elems, class_search, class_search.get(0));
		while (fields(eles) < fields(elems)) {
			getClassFieldMapping(elems, class_search, class_search.get(0));
			eles = elems;
		}
		if (!noFile) {
			File outputFile = new File(".fieldmapping.xml");
			FileSystemInteractor.createOutputFile(outputFile, XMLParser.serializeObject(elems));
		}
		// System.out.println(XMLParser.serializeObject(elems));
		return elems;
	}

	public <T> ArrayList<Class<?>> getClassesOfType(Class<T> class_search) {

		getReflections();
		Set<Class<? extends T>> classes = getPackageClassesOfType(class_search);//
		// getPackageClassesAnnotated(State.class);
		ArrayList<Class<?>> classSet = new ArrayList<Class<?>>();

		classSet.add(class_search);
		classSet.addAll(classes);
		return classSet;
	}

	private static Integer fields(HashMap<String, ArrayList<Field>> test) {

		Integer count = 0;
		for (ArrayList<Field> clas : test.values()) {
			count += clas.size();
		}
		return count;
	}

	public FieldMapper(Class<?> base_class) {

		noFile = false;
		skipFields = getSkipFields(base_class);
		elements = getClassFieldsMappingOfType(base_class);
	}

	public FieldMapper(Class<?> base_class, boolean try_file, boolean create_file) {

		noFile = create_file;
		skipFields = getSkipFields(base_class);
		elements = getClassFieldsMappingOfType(base_class, try_file);
	}

	public FieldMapper(Class<?> base_class, boolean try_file) {

		noFile = false;
		skipFields = getSkipFields(base_class);
		elements = getClassFieldsMappingOfType(base_class, try_file);
	}
}
