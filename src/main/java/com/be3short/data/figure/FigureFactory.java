
package com.be3short.data.figure;

public class FigureFactory {

	public static IFigureFactory factory = new IFigureFactory() {

		@Override
		public Figure create() {

			return new FigurePane();

		}
	};

	public static Figure create() {

		return factory.create();
	}
}
