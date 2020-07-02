package com.be3short.obj.access;

import com.be3short.obj.labeling.ChoiceName;
import java.lang.reflect.Field;
import java.util.HashMap;

public class ChoiceAccessor
{

	Object[] choiceEnums;
	Field choiceField;

	public static HashMap<String, Object> getChoiceNames(Object... enums)
	{
		HashMap<String, Object> names = new HashMap<String, Object>();
		HashMap<String, Field> fields = FieldAccessor.getAnnotationFields(enums[0], true, ChoiceName.class);
		Field choiceName = fields.values().toArray(new Field[fields.size()])[0];
		for (Object cenum : enums)
		{
			try
			{
				names.put((String) choiceName.get(cenum), cenum);
			} catch (IllegalArgumentException | IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return names;
	}

	public static HashMap<String, Object> getChoices(Object... enums)
	{
		HashMap<String, Object> names = new HashMap<String, Object>();
		for (Object cenum : enums)
		{
			try
			{
				String enumName = ((Enum) cenum).name();
				String[] splitByUnderscore = enumName.split("_");
				enumName = "";
				for (int i = 0; i < splitByUnderscore.length; i++)
				{
					if (i > 0)
					{
						enumName += " ";
					}
					enumName = splitByUnderscore[i].substring(0, 1) + splitByUnderscore[i].substring(1).toLowerCase();
				}
				names.put(enumName, cenum);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return names;
	}

	// public static HashMap<String, Object> getChoicesUnit(Object... enums)
	// {
	// HashMap<String, Object> names = new HashMap<String, Object>();
	// for (Object cenum : enums)
	// {
	// try
	// {
	// String enumName = UnitData.getUnitData((Unit) cenum).unitAbbreviation;
	// System.out.println(enumName);
	// //String[] splitByUnderscore = enumName.split("_");
	// //enumName = "";
	// // for (int i = 0; i < splitByUnderscore.length; i++)
	// // {
	// // if (i > 0)
	// // {
	// // enumName += " ";
	// // }
	// // enumName = splitByUnderscore[i].substring(0, 1) +
	// splitByUnderscore[i].substring(1).toLowerCase();
	// // }
	// names.put(enumName, cenum);
	// } catch (Exception e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// return names;
	// }

}
