
package com.be3short.data.store;

import java.util.ArrayList;

public interface IDataSeriesFactory {

	public DataSeries create();

	public DataSeries create(String label);

	public DataSeries create(String label, ArrayList<Object> x, ArrayList<Object> y);

}
