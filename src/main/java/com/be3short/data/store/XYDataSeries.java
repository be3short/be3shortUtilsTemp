
package com.be3short.data.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.be3short.data.chart.SeriesPointData;

/**
 * a generalized data set
 * 
 * @author Brendan Short
 *
 * @param <X>
 *            x type
 * @param <Y>
 *            y type
 */
public interface XYDataSeries<X, Y> extends DynamicFields {

	/**
	 * Get the label of the data series
	 * 
	 * @return
	 */
	public String getLabel();

	/**
	 * Set the label of the data series
	 * 
	 * @param label
	 * @return
	 */
	public XYDataSeries<X, Y> setLabel(String label);

	/**
	 * get the set data as a map
	 * 
	 * @return data map
	 */
	public Map<SeriesPointData<X>, Y> getDataMap();

	/**
	 * get the domain values of the data set
	 * 
	 * @return list of domain values
	 */
	public List<X> getXValues();

	/**
	 * get the range values of the data set
	 * 
	 * @return list of range values
	 */
	public List<Y> getYValues();

	/**
	 * Set the data
	 * 
	 * @param data
	 *            map with new data
	 * @return the data set object
	 */
	public XYDataSeries<X, Y> addDataSet(ArrayList<Object> x, ArrayList<Object> y);

	public XYDataSeries<X, Y> setDataSet(ArrayList<Object> x, ArrayList<Object> y);

	/**
	 * get the y value corresponding to the specified x value
	 * 
	 * @param x_value
	 *            the x value corresponding to y value
	 * @return the corresponding y value
	 */
	public Y getYValue(X x_value);

	/**
	 * add a data point to the series
	 * 
	 * @param x
	 *            the x value
	 * @param y
	 *            the y value
	 * @return the (this object) series that data was added to
	 */
	public XYDataSeries<X, Y> addData(X x, Y y);

	/**
	 * remove a data point from the series
	 * 
	 * @param x
	 *            x value corresponding to the y value to remove
	 * @return the y value removed
	 */
	public Y removeData(X x);

}
