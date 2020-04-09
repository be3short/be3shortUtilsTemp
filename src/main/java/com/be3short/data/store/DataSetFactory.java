
package com.be3short.data.store;

public class DataSetFactory {

	public static DataSet create(DataSeries... new_series) {

		return factory.create(new_series);
	}

	public static IDataSetFactory factory = new IDataSetFactory() {

		@Override
		public DataSet create(DataSeries... series) {

			return new BaseDataSet(series);
		}

	};

}
