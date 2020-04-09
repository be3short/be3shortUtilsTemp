
package com.be3short.data.graphics;

import java.awt.Component;
import java.io.File;

import com.be3short.data.figure.Figure;
import com.be3short.data.store.DynamicFields;

public interface Graphic extends DynamicFields {

	public Component getContent();

	public void render();

	public boolean export(File destination, GraphicFormat format);

	public Figure display();
}
