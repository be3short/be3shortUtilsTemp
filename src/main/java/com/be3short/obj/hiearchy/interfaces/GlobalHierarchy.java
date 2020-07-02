package com.be3short.obj.hiearchy.interfaces;

import com.be3short.obj.structures.HierarchicalRoot;
import java.util.ArrayList;

public interface GlobalHierarchy
{

	public HierarchicalRoot getRoot();

	public HierarchicalComponent findParent();

	public ArrayList<HierarchicalComponent> findChildren();
}
