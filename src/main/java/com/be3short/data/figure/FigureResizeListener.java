
package com.be3short.data.figure;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;

import com.be3short.data.graphics.GraphicFormat;

public class FigureResizeListener implements KeyListener {

	FigurePane figure;

	FigureFormatSelector<GraphicFormat> gs;

	public FigureResizeListener(FigurePane figure) {

		this.figure = figure;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

		// System.out.println("released" + KeyEvent.getKeyText(e.getKeyCode()) + " ctrl
		// " + e.isAltDown());
		if (e.isControlDown()) {
			if (KeyEvent.getKeyText(e.getKeyCode()).equals("R") || KeyEvent.getKeyText(e.getKeyCode()).equals("r")) {

				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							figure.displayFrame.setResizable(!figure.displayFrame.isResizable());
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}

				});
				t.start();

			}
			e.consume();
		}
	}

	public File handleDialog(JFileChooser fc) {

		final ArrayList<File> files = new ArrayList<File>();

		try {

			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {

					fc.setPreferredSize(new Dimension(1020, 680));
					int result = -1;
					if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
						result = fc.showSaveDialog(null);
					} else {
						result = fc.showOpenDialog(null);
					}
					if (result != JFileChooser.CANCEL_OPTION) {
						File file = fc.getSelectedFile();
						GraphicFormat format = gs.getSelected().get();
						figure.exportToFile(file, format);
						files.addAll(Arrays.asList(fc.getSelectedFiles()));
					}

				}
			});
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private Object getField(String name, Object parent)
			throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

		Class aClass = parent.getClass();
		Field field = aClass.getDeclaredField(name);
		field.setAccessible(true);
		return field.get(parent);
	}
}
