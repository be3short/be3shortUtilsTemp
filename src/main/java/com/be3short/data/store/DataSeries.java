
package com.be3short.data.store;

import java.util.ArrayList;

public interface DataSeries extends XYDataSeries<Object, Object> {

	public static final String ADJUSTED_LABEL = "ADJUSTED_LABEL";

	public <W, Z> XYDataSeries<W, Z> getContent();

	@Override
	public DataSeries setLabel(String label);

	@Override
	public DataSeries addDataSet(ArrayList<Object> x, ArrayList<Object> y);

	@Override
	public DataSeries setDataSet(ArrayList<Object> x, ArrayList<Object> y);

	@Override
	public DataSeries addData(Object x, Object y);

	public ArrayList<Integer> setJumpDomain(ArrayList<Integer> times);

	public ArrayList<Integer> getJumpDomain();
}
