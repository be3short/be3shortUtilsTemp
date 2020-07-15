
package com.be3short.data.figure;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.be3short.data.graphics.GraphicFormat;

public class FigureSaveListener implements KeyListener {

	FigurePane figure;

	FigureFormatSelector<GraphicFormat> gs;

	public FigureSaveListener(FigurePane figure) {

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

		if (e.isControlDown()) {
			if (KeyEvent.getKeyText(e.getKeyCode()).equals("S") || KeyEvent.getKeyText(e.getKeyCode()).equals("S")) {

				performFileSave(null, null);

			}
			e.consume();
		}
	}

	public void performFileSave(File output, GraphicFormat graphic_format) {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				if (output != null && graphic_format != null) {
					figure.exportToFile(output, graphic_format);
				} else {
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException ex) {
						ex.printStackTrace();
					}
					HashMap<String, GraphicFormat> formats = GraphicFormat.getFormatMap();

					gs = new FigureFormatSelector<GraphicFormat>(formats);
					JFileChooser fc = new JFileChooser();
					JPanel cb = gs.getChoicePanel();
					// cb.setPreferredSize(new Dimension(500, 200));

					try {
						JComboBox filterComboBox = (JComboBox) getField("filterComboBox", fc.getUI());
						Container parent = filterComboBox.getParent();
						parent.add(cb);
					} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
						ex.printStackTrace();
					}

					JComboBox filterComboBox;
					try {
						filterComboBox = (JComboBox) getField("filterComboBox", fc.getUI());
						filterComboBox.getParent().remove(0);
						JPanel bot = new JPanel(new BorderLayout());
						bot.add(new JLabel("Select a graphic format:"), BorderLayout.CENTER);
						bot.add(cb, BorderLayout.SOUTH);

						filterComboBox.getParent().add(bot);
						System.out.println(filterComboBox.getParent().getComponents().length);
						filterComboBox.setVisible(false);

					} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fc.setDialogType(JFileChooser.SAVE_DIALOG);
					File file = handleDialog(fc);
					if (file != null) {
						GraphicFormat format = gs.getSelected().get();
						figure.exportToFile(file, format);
					}
				}
			}

		});
		t.start();
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
