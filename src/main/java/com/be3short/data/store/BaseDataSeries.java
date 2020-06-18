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

public class BaseDataSeries extends DynamicFieldObject implements DataSeries {

	private String label;

	private LinkedHashMap<Object, Object> data;

	public BaseDataSeries() {

		this("Series", new LinkedHashMap<Object, Object>());
	}

	public BaseDataSeries(String label) {

		this(label, new LinkedHashMap<Object, Object>());
	}

	public BaseDataSeries(String label, Map<Object, Object> data) {

		setLabel(label);
		addData(data);
	}

	@Override
	public String getLabel() {

		return label;
	}

	@Override
	public DataSeries setLabel(String label) {

		this.label = label;
		// System.out.println("setting label of " + this.toString() + " to " + label);
		return this;
	}

	@Override
	public Map<Object, Object> getDataMap() {

		return data;
	}

	@Override
	public List<Object> getXValues() {

		return new ArrayList<Object>(data.keySet());
	}

	@Override
	public List<Object> getYValues() {

		return new ArrayList<Object>(data.values());

	}

	@Override
	public DataSeries addData(Map<Object, Object> data) {

		if (this.data == null) {
			this.data = new LinkedHashMap<Object, Object>();
		}
		// this.data.clear();
		for (Object x : data.keySet()) {
			this.data.put(x, data.get(x));
		}
		return this;
	}

	@Override
	public Object getYValue(Object domain_value) {

		if (data.containsKey(domain_value)) {
			return data.get(domain_value);
		}
		return null;

	}

	@Override
	public DataSeries addData(Object x, Object y) {

		data.put(x, y);
		return this;
	}

	@Override
	public Object removeData(Object x) {

		if (data.containsKey(x)) {
			return data.remove(x);
		}
		return null;
	}

	@Override
	public <W, Z> XYDataSeries<W, Z> getContent() {

		XYDataSeries<W, Z> newSeries = (XYDataSeries<W, Z>) this;
		return newSeries;
	}

}
