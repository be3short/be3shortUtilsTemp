package com.be3short.obj.organization;

import com.be3short.obj.access.Protected;
import java.util.HashMap;

public class Description
{

	public final Protected<String> title; // description of the object -ie "Storage Device" or "UAV"
	public final Protected<String> description; // name of the object - ie "Generic HD Video Camera" "Randomized Temperature Sensor"
	public final Protected<String> prefix; // optional prefix to the objects name - ie a specific part number
	public final Protected<String> suffix; // optional suffix to the objects name - ie a relavant specification
	public final Protected<String> information; // optional information about the object

	private HashMap<NameComponent, Protected<String>> componentMap; // map to all name components, which allows name to be extracted in any format

	public Description(String title)
	{
		this.title = new Protected<String>(title, true);
		this.description = new Protected<String>(title, true);
		this.prefix = new Protected<String>("", true);
		this.suffix = new Protected<String>("", true);
		this.information = new Protected<String>("", true);
		componentMap = initializeComponentMap();
	}

	public Description(String title, String description)
	{
		this.title = new Protected<String>(title, true);
		this.description = new Protected<String>(description, true);
		this.prefix = new Protected<String>("", true);
		this.suffix = new Protected<String>("", true);
		this.information = new Protected<String>("", true);
		componentMap = initializeComponentMap();
	}

	public Description(String title, String description, String prefix)
	{
		this.title = new Protected<String>(title, true);
		this.description = new Protected<String>(description, true);
		this.prefix = new Protected<String>(prefix, true);
		this.suffix = new Protected<String>("", true);
		this.information = new Protected<String>("", true);
		componentMap = initializeComponentMap();
	}

	public Description(String title, String description, String prefix, String suffix)
	{
		this.title = new Protected<String>(title, true);
		this.description = new Protected<String>(description, true);
		this.prefix = new Protected<String>(prefix, true);
		this.suffix = new Protected<String>(suffix, true);
		this.information = new Protected<String>("", true);
		componentMap = initializeComponentMap();
	}

	private HashMap<NameComponent, Protected<String>> initializeComponentMap()
	{
		HashMap<NameComponent, Protected<String>> map = new HashMap<NameComponent, Protected<String>>();
		map.put(NameComponent.TITLE, title);
		map.put(NameComponent.DESCRIPTION, description);
		map.put(NameComponent.PREFIX, prefix);
		map.put(NameComponent.SUFFIX, suffix);

		return map;
	}

	public String getName(Information... format)
	{
		if (format.length == 0)
		{
			return description.get();
		}
		String nameStructure = "";
		for (Information component : format)
		{
			if (nameStructure.length() > 0)
			{
				nameStructure += " ";
			}
			nameStructure += componentMap.get(component).get();
		}

		return nameStructure;
	}

	public static enum NameComponent implements Information
	{
		TITLE,
		DESCRIPTION,
		PREFIX,
		SUFFIX;

		@Override
		public InformationCategory getCategory()
		{
			// TODO Auto-generated method stub
			return InformationCategory.DESCRIPTION;
		}
	}
}
