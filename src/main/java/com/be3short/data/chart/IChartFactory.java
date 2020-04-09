
package com.be3short.data.chart;

import com.be3short.data.store.DataSet;

public interface IChartFactory {

	public Chart create();

	public Chart create(DataSet data);

}
