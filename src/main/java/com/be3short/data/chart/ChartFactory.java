
package com.be3short.data.chart;

import com.be3short.data.store.DataSet;

public abstract class ChartFactory {

	public static IChartFactory factory = new IChartFactory() {

		@Override
		public Chart create() {

			return new BaseChart();
		}

		@Override
		public Chart create(DataSet data) {

			return new BaseChart(data);
		}

	};

	public static Chart create() {

		return factory.create();
	}

	public static Chart create(DataSet data) {

		return factory.create(data);
	}

}
