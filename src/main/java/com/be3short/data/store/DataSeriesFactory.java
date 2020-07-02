
package com.be3short.data.store;

import java.util.ArrayList;

public class DataSeriesFactory {

	public static IDataSeriesFactory factory = new IDataSeriesFactory() {

		@Override
		public DataSeries create() {

			return new BaseDataSeries();
		}

		@Override
		public DataSeries create(String label) {

			return new BaseDataSeries(label);
		}

		@Override
		public DataSeries create(String label, ArrayList<Object> x, ArrayList<Object> y) {

			return new BaseDataSeries(label, x, y);
		}
	};

	public static DataSeries create() {

		return factory.create();
	}

	public static DataSeries create(String label) {

		return factory.create(label);

	}

	public static DataSeries create(String label, ArrayList<Object> x, ArrayList<Object> y) {

		return factory.create(label, x, y);

	}
}
