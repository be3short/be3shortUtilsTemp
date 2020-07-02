package com.be3short.obj.organization;

import java.util.HashMap;

public class IdFactory
{

	public static IdFactory staticFactory = new IdFactory(true);
	private static boolean active;

	public IdFactory(boolean active)
	{
		this.active = active;
	}

	public static void enabled(boolean on)
	{
		active = on;
	}

	private static HashMap<Scope, LocalIdFactory> regionalFactories = initializeDefaultFactory();

	private static HashMap<Scope, LocalIdFactory> initializeDefaultFactory()
	{
		HashMap<Scope, LocalIdFactory> defaultFactories = new HashMap<Scope, LocalIdFactory>();
		defaultFactories.put(Scope.BasicScope.GLOBAL, new LocalIdFactory());
		return defaultFactories;
	}

	public static void regenerateId(Identified old, Identified parent)
	{
		Identified.idAccess(old).set(generateId(old.id().description().description.get(), old.id().type()));
		old.id().parent = (Identified.idAccess(parent));
		Identified.idAccess(parent).get().children.add(Identified.idAccess(old));
	}

	public static void regenerateId(Identified old)
	{
		Identified.idAccess(old).set(generateId(old.id().description().description.get(), old.id().type()));
	}

	public static ObjectId generateId(String name, ObjectType type)
	{
		if (active)
		{
			return regionalFactories.get(Scope.BasicScope.GLOBAL).generateId(name, type);
		} else
		{
			return regionalFactories.get(Scope.BasicScope.GLOBAL).generateId(name, type, -1);
		}

	}

	private static class LocalIdFactory
	{

		HashMap<ObjectType, Integer> globalIndicies; // indicies based on the type of component
		HashMap<Object, HashMap<Object, Integer>> localIndicies; // indicies based on the type of component
		Integer overallIndex;

		public LocalIdFactory()
		{
			globalIndicies = new HashMap<ObjectType, Integer>();
			localIndicies = new HashMap<Object, HashMap<Object, Integer>>();
			overallIndex = 0;
		}

		public ObjectId generateId(String name, ObjectType type)
		{
			ObjectId id = new ObjectId(name, type);
			if (globalIndicies.containsKey(type))
			{
				id.address().globalIndex.set(globalIndicies.get(type));
				globalIndicies.put(type, globalIndicies.get(type) + 1);
				id.description().description.set(id.description().description.get() + id.address().globalIndex.get());
			} else
			{
				id.description().description.set(id.description().description.get());
				id.address().globalIndex.set(0);
				globalIndicies.put(type, 0);
			}
			return id;
		}

		public ObjectId generateId(String name, ObjectType type, Integer address)
		{
			ObjectId id = new ObjectId(name, type);

			id.address().globalIndex.set(address);
			id.description().description.set(id.description().description.get() + address);
			return id;
		}

	}
}
