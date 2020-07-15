/* ==============================================================
 * data utils - general data storage & visualization utilities
 * ==============================================================
 *
 * MIT License
 * 
 * Copyright (c) 2020 Brendan Short https://be3short.com/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/

package com.be3short.data.store;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.be3short.data.chart.SeriesPointData;

public class BaseDataSeries extends DynamicFieldObject implements DataSeries {

	private String label;

	private List<Object> xValues;

	private List<Object> yValues;

	private ArrayList<Integer> timeDomain;

	public BaseDataSeries() {

		this("Series", new ArrayList<Object>(), new ArrayList<Object>());
	}

	public BaseDataSeries(String label) {

		this(label, new ArrayList<Object>(), new ArrayList<Object>());
	}

	public BaseDataSeries(String label, ArrayList<Object> x, ArrayList<Object> y) {

		setLabel(label);
		xValues = x;
		yValues = y;
	}

	@Override
	public String getLabel() {

		return label;
	}

	@Override
	public DataSeries setLabel(String label) {

		if (label != null) {
			this.label = label;
		} else {
			this.label = "Series";
		}
		// System.out.println("setting label of " + this.toString() + " to " + label);
		return this;
	}

	@Override
	public List<Object> getXValues() {

		return xValues;
	}

	@Override
	public List<Object> getYValues() {

		return yValues;

	}

	@Override
	public DataSeries addDataSet(ArrayList<Object> x, ArrayList<Object> y) {

		// this.data.clear();
		for (Object xv : x) {
			xValues.add(xv);
		}
		for (Object yv : y) {
			xValues.add(yv);
		}
		return this;
	}

	@Override
	public DataSeries setDataSet(ArrayList<Object> x, ArrayList<Object> y) {

		xValues.clear();
		yValues.clear();
		return addDataSet(x, y);
	}

	@Override
	public Object getYValue(Object domain_value) {

		Integer ind = xValues.indexOf(domain_value);
		if (ind > 0) {
			return yValues.get(ind);
		}
		return null;

	}

	@Override
	public DataSeries addData(Object x, Object y) {

		xValues.add(x);
		yValues.add(y);
		return this;
	}

	@Override
	public Object removeData(Object x) {

		Object v = null;
		Integer ind = xValues.indexOf(x);
		if (ind > 0) {
			v = yValues.get(ind);
			yValues.remove(ind);
			xValues.remove(ind);
		}
		return v;
	}

	@Override
	public <W, Z> XYDataSeries<W, Z> getContent() {

		XYDataSeries<W, Z> newSeries = (XYDataSeries<W, Z>) this;
		return newSeries;
	}

	@Override
	public ArrayList<Integer> setJumpDomain(ArrayList<Integer> times) {

		timeDomain = times;
		return timeDomain;
	}

	@Override
	public ArrayList<Integer> getJumpDomain() {

		// TODO Auto-generated method stub
		return timeDomain;
	}

	@Override
	public Map<SeriesPointData<Object>, Object> getDataMap() {

		Map<SeriesPointData<Object>, Object> map = new LinkedHashMap<SeriesPointData<Object>, Object>();
		for (int i = 0; i < xValues.size(); i++) {
			Object y = yValues.get(i);
			Object x = xValues.get(i);
			map.put(new SeriesPointData<Object>(x), y);
		}
		// TODO Auto-generated method stub
		return map;
	}

}
