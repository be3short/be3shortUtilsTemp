
package com.be3short.data.store;

import java.util.Map;

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
		public DataSeries create(String label, Map<Object, Object> data) {

			return new BaseDataSeries(label, data);
		}
	};

	public static DataSeries create() {

		return factory.create();
	}

	public static DataSeries create(String label) {

		return factory.create(label);

	}

	public static DataSeries create(String label, Map<Object, Object> data) {

		return factory.create(label, data);

	}
}
