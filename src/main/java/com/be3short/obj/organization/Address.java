package com.be3short.obj.organization;

import com.be3short.obj.access.Protected;

public class Address
{

	public final Protected<Integer> objectNumber; // index number of the object in relation to all other labeled objects within the global scope
	public final Protected<Integer> localIndex; // local index of the object, allows for duplicate objects to be identified locally
	public final Protected<Integer> globalIndex; // global index of the object, allows for duplicate objects to be identified globally
	public final Protected<Integer> address; // address of the object if the object independently interacts with the environment, typically assigned to agents within the environment, but can be used in cases where an agent contains multiple interacting systems as well

	public Address()
	{
		address = new Protected<Integer>(-1, true);
		localIndex = new Protected<Integer>(-1, true);
		globalIndex = new Protected<Integer>(-1, true);
		objectNumber = new Protected<Integer>(-1, true);
	}

	public static enum AddressComponent implements Information
	{
		ADDRESS,
		LOCAL,
		GLOBAL,
		NUMBER;

		@Override
		public InformationCategory getCategory()
		{
			// TODO Auto-generated method stub
			return InformationCategory.DESCRIPTION;
		}
	}

}
