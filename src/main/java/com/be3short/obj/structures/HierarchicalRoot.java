package com.be3short.obj.structures;

import java.util.ArrayList;
import com.be3short.obj.access.ObjectFinder;
import com.be3short.obj.access.Protected;
import com.be3short.obj.hiearchy.interfaces.GlobalHierarchy;
import com.be3short.obj.hiearchy.interfaces.HierarchicalComponent;

public class HierarchicalRoot implements GlobalHierarchy
{

	public ArrayList<Protected<HierarchicalLayer>> allObjects;

	public HierarchicalRoot()
	{
		allObjects = new ArrayList<Protected<HierarchicalLayer>>();
	}

	public Protected<HierarchicalLayer> addLayer(Protected<Object> obj, Protected<HierarchicalLayer> parent)
	{
		Protected<HierarchicalLayer> layer = HierarchicalLayer.getLayer(obj, this, parent);
		allObjects.add(layer);
		return layer;
	}

	public <T> ArrayList<T> getAllObjects(Class<T> obj_class)
	{
		ArrayList<T> objects = new ArrayList<T>();
		for (Protected<HierarchicalLayer> layer : allObjects)
		{
			if (ObjectFinder.containsSuper(layer.get().getSelf(), (obj_class)))
			{
				System.out.println("woohoo" + layer.get().self);
				objects.add((T) layer.get().getSelf());
			}
		}
		return objects;
	}

	public Protected<HierarchicalLayer> addLayer(Object obj)
	{
		//		if (!isDuplicate(obj))
		//		{
		//			Protected<HierarchicalLayer> layer = HierarchicalLayer.getLayer(obj, this, null);
		//			allObjects.add(layer);
		//			return layer;
		//		} else

		return null;
	}

	public Protected<HierarchicalLayer> updateLayer(Protected<HierarchicalLayer> obj)
	{
		if (!isDuplicate(obj.get()))
		{
			obj.set(HierarchicalLayer.getLayer(obj.get(), this, obj.get().parent).get());
			allObjects.add(obj);
			return obj;
		} else
		{
			return addLayer(HierarchicalLayer.getLayer(obj.get(), this, obj.get().parent));
		}
		///return obj;
	}

	public boolean isDuplicate(Object obj)
	{
		for (Protected<HierarchicalLayer> l : allObjects)
		{
			if (l.get().getSelf().equals(obj))
			{
				return true;
			}
		}
		return false;
	}

	public void wipeConnections()
	{
		for (Protected<HierarchicalLayer> child : allObjects)
		{
			child.set(HierarchicalLayer.getLayer(child.get().getSelf(), child.get().globe, child.get().parent).get());
		}
	}

	public void findAllParents()
	{
		for (Protected<HierarchicalLayer> child : allObjects)
		{

			for (Protected<HierarchicalLayer> parent : allObjects)
			{
				if (parent.get().containsAChild(child.get().getSelf()))
				{
					if (child.get().getParent().get() == null)
					{
						child.get().getParent().set(parent.get());
						parent.get().children.add(child);
					}

				}
			}
		}
	}

	@Override
	public HierarchicalComponent findParent()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<HierarchicalComponent> findChildren()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HierarchicalRoot getRoot()
	{
		// TODO Auto-generated method stub
		return null;
	}

	//	public static void main(String args[])
	//	{
	//		//matchingTest();
	//		largeMatchingTest();
	//	}
	//
	//	public static void largeMatchingTest()
	//	{
	//		HierarchicalRoot hei = new HierarchicalRoot();
	//		for (Integer i = 0; i < 10; i++)
	//		{
	//			Parent np = new Parent();
	//			hei.addLayer(np);
	//			hei.addLayer(np.child);
	//		}
	//
	//		hei.findAllParents();
	//		for (Protected<HierarchicalLayer> l : hei.allObjects)
	//		{
	//			System.out.println(l.get().getSelf().toString() + "\n" + "Parent:" + "\n" + "\n");
	//			for (Protected<HierarchicalLayer> h : l.get().children)
	//			{
	//				System.out.println("Child " + h.get().getSelf().getClass() + "\n");
	//			}
	//		}
	//		//System.out.println(XMLParser.serializeObject(hei));
	//	}
	//
	//	public String printLevels()
	//	{
	//		findAllParents();
	//		String print = "Hierarchy\n";
	//		for (Protected<HierarchicalLayer> l : allObjects)
	//		{
	//			if (l.get().getSelf() != null)
	//			{
	//				print += "Self: " + l.get().getSelf().toString() + "\n";
	//
	//				if (l.get().parent.get() != null)
	//				{
	//					print += "Parent: " + l.get().getParent().get().getSelf() + "\n";
	//				}
	//				for (Protected<HierarchicalLayer> h : l.get().children)
	//				{
	//					print += ("Child " + h.get().getSelf().toString() + "\n");
	//				}
	//			}
	//		}
	//		return print;
	//	}
	//
	//	@Override
	//	public HierarchicalRoot getRoot()
	//	{
	//		// TODO Auto-generated method stub
	//		return this;
	//	}

}
