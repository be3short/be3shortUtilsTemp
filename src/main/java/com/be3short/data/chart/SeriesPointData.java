
package com.be3short.data.chart;

public class SeriesPointData<T> {

	private T data;

	public SeriesPointData(T dat) {

		setData(dat);
	}

	public Object getData() {

		return data;
	}

	public void setData(T data) {

		this.data = data;
	}
}
