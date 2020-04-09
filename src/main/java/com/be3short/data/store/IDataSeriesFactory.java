
package com.be3short.data.store;

import java.util.Map;

public interface IDataSeriesFactory {

	public DataSeries create();

	public DataSeries create(String label);

	public DataSeries create(String label, Map<Object, Object> data);

}
