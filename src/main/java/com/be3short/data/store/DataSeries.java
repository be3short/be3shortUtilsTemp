
package com.be3short.data.store;

import java.util.Map;

public interface DataSeries extends XYDataSeries<Object, Object> {

	public static final String ADJUSTED_LABEL = "ADJUSTED_LABEL";

	public <W, Z> XYDataSeries<W, Z> getContent();

	@Override
	public DataSeries setLabel(String label);

	@Override
	public DataSeries addData(Map<Object, Object> data);

	@Override
	public DataSeries addData(Object x, Object y);
}
