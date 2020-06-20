
package com.be3short.data.chart;

import com.be3short.data.figure.Figure;
import com.be3short.data.store.DataSeries;
import com.be3short.data.store.DataSeriesFactory;
import com.be3short.data.store.DataSet;
import com.be3short.data.store.DataSetFactory;

public class TestDataSeries {

	public static boolean helpDisplay(Figure fig) {

		fig.display();
		try {
			Thread.sleep(6000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		return true;
	}

	public static void main(String args[]) {

		DataSeries series = DataSeriesFactory.create("test");// ChartSeries series = new BaseChartSeries("test");
		series.addData(0.0, 0.0).addData(1.0, 1.0).addData(2.0, 2.0);
		DataSet dataset = DataSetFactory.create(series);
		Chart chart = ChartFactory.create(dataset);
		chart.display();

	}
}
