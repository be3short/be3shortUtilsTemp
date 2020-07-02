package com.be3short.obj.organization;

import com.be3short.obj.access.Protected;
import java.util.ArrayList;

public class ObjectId
{

	public ArrayList<Protected<ObjectId>> getChildren()
	{
		return children;
	}

	Protected<ObjectId> parent; // id of parent 
	ArrayList<Protected<ObjectId>> children; // id of children
	private ObjectType type; // type of object this id represents
	private Address address; // address of the object represented by this id
	private Description name; // name of the object represented by this id, which can contain additional data that can be displayed or hidden depending on the usage

	public ObjectId(String name, ObjectType type)
	{

		this.type = type;
		this.name = new Description(name);
		this.address = new Address();
		children = new ArrayList<Protected<ObjectId>>();
		parent = new Protected<ObjectId>(null, true);
	}

	public ObjectType type()
	{
		return type;
	}

	public Address address()
	{
		return address;
	}

	public Description description()
	{
		return name;
	}

	public String getName()
	{
		return name.description.get();
	}

	public String getLineage()
	{
		String root = name.description.get();
		Protected<ObjectId> parentId = parent;
		while (parentId.get() != null)
		{
			root = parentId.get().description().description.get() + " - " + root;
			parentId = parentId.get().parent;
		}
		return root;
	}

	public String getName(boolean simple)
	{
		if (simple)
		{
			return null;//description;
		} else
		{
			return null;//description;//+ count;
		}
	}

	public String getNamePrefix()
	{
		return null;//prefix;
	}

	public Integer getIndex()
	{
		return null;//localIndex;
	}

	public String printTree()
	{
		Integer indentIndex = 0;
		String root = description().description.get() + "\n";
		for (Protected<ObjectId> child : getChildren())
		{
			root += child.get().printTree() + "\n";
		}
		return root;
	}

	private static class IDFactory
	{

		//		public static ArrayList<ID> assignedIds = new ArrayList<ID>();// names already assigned based on type
		//
		//		public static ID getID(String name, ComponentType type)
		//		{
		//
		//		}
		//
		//		private static ID adjustID(ID old)
		//		{
		//			for (ID check : assignedIds)
		//			{
		//				if (check.type.equals(old.type)&&check.name.equals(old.name))
		//				{
		//				}
		//			}
		//		}
		//		public static String getAdjustedName(String name, ComponentType type)
		//		{
		//			Integer append = 1;
		//			String newName = name;
		//			while (typeNames.get(type).)
		//		}

	}
}
