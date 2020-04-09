/*
 * =========================================================== HSE : The Hybrid Systems Environment
 * ===========================================================
 *
 * MIT License
 * 
 * Copyright (c) 2018 HybridSystemsEnvironment
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * 
 * ------------------------------------------------ FileBrowser.java ------------------------------------------------
 *
 * Original Author: Brendan Short Contributor(s):
 *
 * Changes: -------- 01-June-2018 : Version 1 (BS);
 *
 */

package com.be3short.util.io;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.be3short.data.figure.FigureFormatSelector;
import com.be3short.data.graphics.GraphicFormat;
import com.be3short.io.general.FileSystemInteractor;

/**
 * A file browser window to load or save files.
 *
 * Intended Operator: User
 */
public class FileBrowser {

	private static Logger log = Logger.getLogger(FileBrowser.class);

	/**
	 * Opens a file browser that allows the selection of a file to be loaded
	 * 
	 * @return true if file is found and accepted by the file chooser
	 */
	public static File load() {

		return openFileWindow(false);

	}

	/**
	 * Opens a file browser that allows the selection of a file to be loaded
	 * 
	 * @return true if file is found and accepted by the file chooser
	 */
	public static File load(String prompt) {

		return openFileWindow(false, prompt);

	}

	/**
	 * Open a file browser window
	 * 
	 * @param save
	 *            flag to open save dialog
	 * @return file the file selected by the window, or null if nothing was selected
	 */
	static File openFileWindow(boolean save) {

		if (save) {
			return openFileWindow(save, "Save File");
		} else {
			return openFileWindow(save, "Open");
		}

	}

	/**
	 * Open a file browser window
	 * 
	 * @param save
	 *            flag to open save dialog
	 * @return file the file selected by the window, or null if nothing was selected
	 */
	static File openFileWindow(boolean save, String prompt) {

		int mode = FileDialog.LOAD;
		File file = null;
		if (save) {
			mode = FileDialog.SAVE;
		}
		try {
			final FileDialog fc = new java.awt.FileDialog((java.awt.Frame) null, prompt, mode);
			fc.setVisible(true);
			file = new File(fc.getDirectory(), fc.getFile());
			FileSystemInteractor.checkDirectory(file.getParent(), true);
		} catch (Exception noFile) {
			String operation = "load selection";
			if (save) {
				operation = "save definition";
			}
			log.info(operation + " cancelled");
		}
		return file;

	}

	/**
	 * Open a file browser window
	 * 
	 * @param save
	 *            flag to open save dialog
	 * @return file the file selected by the window, or null if nothing was selected
	 */
	static File openDirectoryWindow(boolean save) {

		int mode = FileDialog.LOAD;
		File file = null;
		if (save) {
			mode = FileDialog.SAVE;
		}
		try {
			final FileDialog fc = new java.awt.FileDialog((java.awt.Frame) null, "Save File", mode);
			fc.setVisible(true);
			file = new File(fc.getDirectory(), fc.getFile());
			FileSystemInteractor.checkDirectory(file.getParent(), true);
		} catch (Exception noFile) {
			String operation = "load selection";
			if (save) {
				operation = "save definition";
			}
			log.info(operation + " cancelled");
		}
		return file;

	}

	/**
	 * Opens a file browser that allows the definition of a file where something
	 * will be saved to.
	 * 
	 * @return True if file creation is a success, null otherwise
	 */
	public static File save() {

		return openFileWindow(true);
	}

	public static String getRelativeFilePath(File file) {

		String absolutePath = file.getAbsolutePath();
		String workingDir = System.getProperty("user.dir");
		String relPath = absolutePath.substring(workingDir.length() + 1);
		return relPath;

	}

	public static File directory(String title) {

		JFileChooser chooser;
		File file = null;
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setControlButtonsAreShown(true);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		return openFileDialog(chooser);
	}

	public static File openFileDialog(JFileChooser fc) {

		System.out.println("Here");
		final ArrayList<File> files = openFilesDialog(fc);
		File file = null;
		try {
			System.out.println("Here2");
			file = files.get(0);
		} catch (Exception problem) {
			problem.printStackTrace();
		}
		System.out.println("returning null");

		return file;

	}

	public static ArrayList<File> openFilesDialog(JFileChooser fc) {

		final ArrayList<File> files = new ArrayList<File>();
		handleDialog(fc, files);
		log.debug(getFileReport(files));
		return files;

	}

	public static String getFileReport(ArrayList<File> files) {

		String report = "files selected by dialog = " + files.size();
		for (File file : files) {

			report += "\n> " + file.getAbsolutePath();
		}
		return report;
	}

	public static ArrayList<File> openFileDialog() {

		String folder = System.getProperty("user.dir");
		JFileChooser fc = new JFileChooser(folder);
		return openFilesDialog(fc);
	}

	public static ArrayList<File> openFilesDialog() {

		String folder = System.getProperty("user.dir");
		JFileChooser fc = new JFileChooser(folder);
		return openFilesDialog(fc);
	}

	public static void handleDialog(JFileChooser fc, ArrayList<File> files) {

		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {

					System.out.println("runit");
					int result = -1;
					if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
						result = fc.showSaveDialog(null);
						if (fc.getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY) {
							files.add(fc.getSelectedFile());
						} else {
							files.add(fc.getSelectedFile());
							files.addAll(Arrays.asList(fc.getSelectedFiles()));
						}
					} else {
						result = fc.showOpenDialog(null);
					}
					if (result == JFileChooser.APPROVE_OPTION) {
						files.addAll(Arrays.asList(fc.getSelectedFiles()));
					}
					System.out.println("done");
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static File save(String title) {

		JFileChooser chooser;
		File file = null;
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(title);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setControlButtonsAreShown(true);
		// disable the "All files" option.
		//
		chooser.setAcceptAllFileFilterUsed(false);
		return openFileDialog(chooser);

	}

	public static JPanel

			generatePanel() {

		JPanel panel = new JPanel();
		panel.setSize(500, 750);
		return panel;
	}

	public static class Test {

		private static int result;

		static File file = null;

		public static void main(String[] args) throws Exception {

			final ArrayList<File> files = new ArrayList<File>();
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {

					String folder = System.getProperty("user.dir");
					JFileChooser fc = new JFileChooser(folder);
					HashMap<String, GraphicFormat> formats = GraphicFormat.getFormatMap();

					FigureFormatSelector<GraphicFormat> gs = new FigureFormatSelector<GraphicFormat>(formats);
					// fc.addChoosableFileFilter(new
					// GraphicFormatFilter<GraphicFormat>(GraphicFormat.PDF));
					JPanel panel = new JPanel(new GridLayout(2, 1));
					JPanel label = new JPanel();
					panel.add(gs.getChoicePanel(), 0);
					panel.add(label, 1);
					panel.setVisible(true);
					result = fc.showSaveDialog(label);

					file = fc.getSelectedFile();
				}
			});
			System.out.println(result + " file " + file.toString());
		}
	}

	public static class DemoJFileChooser extends JPanel implements ActionListener {

		JButton go;

		JFileChooser chooser;

		String choosertitle;

		File file;

		public DemoJFileChooser() {

			go = new JButton("Do it");
			go.addActionListener(this);
			add(go);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle(choosertitle);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			//
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
				file = chooser.getCurrentDirectory();
			} else {
				System.out.println("No Selection ");
			}
		}

		@Override
		public Dimension getPreferredSize() {

			return new Dimension(200, 200);
		}

		public File getDir() {

			JFrame frame = new JFrame("");
			DemoJFileChooser panel = new DemoJFileChooser();
			frame.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {

					file = chooser.getCurrentDirectory();
				}
			});
			frame.getContentPane().add(panel, "Center");
			frame.setSize(panel.getPreferredSize());
			frame.setVisible(true);
			return panel.file;
		}
	}
}
