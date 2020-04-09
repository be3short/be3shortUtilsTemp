
package com.be3short.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsolePrompter {

	private BufferedReader reader;

	public ConsolePrompter() {

		initialize();
	}

	public String getResponse(String prompt) {

		System.out.print(prompt);
		// Reading data using readLine
		String name = null;
		try {
			name = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Printing the read line
		// System.out.println("response : " + name);
		return name;

	}

	public static String getPromptedResponse(String prompt) {

		ConsolePrompter console = new ConsolePrompter();
		return console.getResponse(prompt);
	}

	private void initialize() {

		// Enter data using BufferReader
		reader = new BufferedReader(new InputStreamReader(System.in));

	}
}
