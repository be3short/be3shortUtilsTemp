
package com.be3short.util.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

public class CleanProperties extends Properties {

	private static class StripFirstLineStream extends FilterOutputStream {

		private boolean firstlineseen = false;

		public StripFirstLineStream(final OutputStream out) {

			super(out);
		}

		@Override
		public void write(final int b) throws IOException {

			if (firstlineseen) {
				super.write(b);
			} else if (b == '\n') {
				// Used to go to next line if did use this line
				// you will get the continues output from the give file
				super.write('\n');

				firstlineseen = true;
			}
		}

	}

	private static final long serialVersionUID = 7567765340218227372L;

	@Override
	public synchronized Enumeration<java.lang.Object> keys() {

		return Collections.enumeration(new TreeSet<>(super.keySet()));
	}

	@Override
	public void store(final OutputStream out, final String comments) throws IOException {

		super.store(new StripFirstLineStream(out), null);
	}
}
