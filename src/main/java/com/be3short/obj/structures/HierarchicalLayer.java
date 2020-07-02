package com.be3short.obj.structures;

import java.lang.reflect.Field;
import java.util.ArrayList;
import com.be3short.obj.access.Protected;
import com.be3short.obj.hiearchy.interfaces.GlobalHierarchy;
import com.be3short.obj.hiearchy.interfaces.HierarchicalComponent;

public class HierarchicalLayer implements HierarchicalComponent
{

	public GlobalHierarchy globe;
	public Protected<HierarchicalLayer> parent;
	public Protected<Object> self;
	public ArrayList<Protected<HierarchicalLayer>> children;

	private HierarchicalLayer(Object self, GlobalHierarchy globe)
	{
		this.globe = globe;
		this.self = new Protected<Object>(self, true);
		this.parent = new Protected<HierarchicalLayer>(null, true);// new Layer("Unknown", globe);
		children = new ArrayList<Protected<HierarchicalLayer>>();
	}

	public static Protected<HierarchicalLayer> getLayer(

	Object self, GlobalHierarchy globe, Protected<HierarchicalLayer> parent)
	{
		HierarchicalLayer newLayer = new HierarchicalLayer(self, globe);
		//newLayer.parent.set(parent);
		return new Protected<HierarchicalLayer>(newLayer, true);
	}

	@Override
	public Protected<HierarchicalLayer> getParent()
	{
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public ArrayList<Protected<HierarchicalLayer>> getChildren()
	{
		// TODO Auto-generated method stub
		return children;
	}

	@Override
	public <T> ArrayList<T> getChildren(Class<T> type)
	{
		ArrayList<T> childs = new ArrayList<T>();
		for (Protected<HierarchicalLayer> child : children)
		{
			if (child.get().getSelf().getClass().equals(type))
			{
				childs.add(type.cast(child));
				childs.addAll(getChildren(type));
			}
		}
		return childs;
	}

	@Override
	public Object getSelf()
	{
		// TODO Auto-generated method stub
		return self.get();
	}

	public boolean containsAChild(Object test)
	{
		for (Field field : getSelf().getClass().getDeclaredFields())
		{
			try
			{
				System.out.println(field.getName() + test);
				field.setAccessible(true);
				try
				{
					if (field.get(self.get()).equals(test))
					{

						return true;
					}
				} catch (Exception p)
				{
					p.printStackTrace();
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}