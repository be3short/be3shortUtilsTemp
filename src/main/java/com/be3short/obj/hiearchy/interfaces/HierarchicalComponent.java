package com.be3short.obj.hiearchy.interfaces;

import java.util.ArrayList;
import com.be3short.obj.access.Protected;
import com.be3short.obj.structures.HierarchicalLayer;

public interface HierarchicalComponent
{

	public Protected<HierarchicalLayer> getParent();

	public ArrayList<Protected<HierarchicalLayer>> getChildren();

	public <T> ArrayList<T> getChildren(Class<T> type);

	public Object getSelf();

}
