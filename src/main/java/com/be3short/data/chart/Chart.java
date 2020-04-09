
package com.be3short.data.chart;

import java.awt.Paint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.be3short.data.graphics.Graphic;
import com.be3short.data.store.DataSeries;
import com.be3short.data.store.DataSet;
import com.be3short.util.io.ObjectEvaluator;

public interface Chart extends Graphic {

	public static String DISPLAY_FIGURE = "DISPLAY_FIGURE";

	public static String SERIES_COLOR = "SERIES_COLOR";

	public static String TITLE = "TITLE";

	public static String X_LABEL = "X_LABEL";

	public static String Y_LABEL = "Y_LABEL";

	public static String SERIES_STROKE = "SERIES_STROKE";

	public static String HEIGHT = "HEIGHT";

	public static String WIDTH = "WIDTH";

	public static String DISPLAY_LEGEND = "DISPLAY_LEGEND";

	public static String TYPE = "TYPE";

	public static String DEFAULT_FLOW_STROKE = "DEFAULT_FLOW_STROKE";

	public static String DEFAULT_JUMP_STROKE = "DEFAULT_JUMP_STROKE";

	public static String DEFAULT_SERIES_COLOR = "DEFAULT_SERIES_COLOR";

	public static String[] CHART_DISPLAY_PROPERTIES =
		{ TITLE, X_LABEL, Y_LABEL, TYPE, DISPLAY_LEGEND };

	public JFreeChart getChart();

	/**
	 * 
	 * 
	 * Line chart where data points generate a trajectory.
	 */
	public static String LINE = "LINE";

	/**
	 * Scatter chart where data points are plotted as shapes.
	 */
	public static String SCATTER = "SCATTER";

	/**
	 * Line chart where data points generate a trajectory and are also plotted as
	 * shapes.
	 */
	public static String SCATTER_WITH_LINE = "SCATTER_WITH_LINE";

	public DataSet getData();

	/**
	 * set the data set that will be displayed in the chart
	 * 
	 * @param data
	 *            dataset to be displayed
	 * @return dataset displayed by the chart
	 */
	public DataSet setData(DataSet data);

	/**
	 * Get the library default set of series colors that are used
	 * 
	 * @return the library default set of colors
	 */
	public static ArrayList<Paint> getDefaultLibrarySeriesColors() {

		return new ArrayList<Paint>(Arrays.asList(ChartColor.createDefaultPaintArray()));
	}

	/**
	 * Creates an XY chart contained in a JFreeChart container.
	 * 
	 * @param chart_data
	 *            the chart data to generate the JFreeChart
	 * @return the generated chart
	 */
	public static JFreeChart createXYChart(Chart chart_data) {

		if (chart_data == null) {
			return ChartFactory.createXYLineChart(null, null, null, new XYSeriesCollection(), PlotOrientation.VERTICAL,
					true, true, true);
		}
		JFreeChart chart = chart_data.getChart();

		ChartContentRenderer rend = new ChartContentRenderer(chart_data);
		XYDataset dataset = buildXYSeriesCollection(chart_data.getData());
		List<Double> domain = chart_data.getData().getDomain();
		if (chart == null) {
			chart = ChartFactory.createXYLineChart(null, null, null, dataset, PlotOrientation.VERTICAL, true, true,
					true);
		}
		chart.getXYPlot().setDataset(dataset);
		try {

			if (dataset.getSeriesCount() > 0) {

				chart.getXYPlot().getDomainAxis().setRange(Collections.min(domain), Collections.max(domain));
			}
		} catch (Exception noData) {
			noData.printStackTrace();
		}
		chart.getXYPlot().setRenderer(rend);
		chart.getXYPlot().setBackgroundPaint(null);
		chart.getPlot().setBackgroundPaint(null);
		chart.setBackgroundPaint(null);

		return chart;
	}

	public JFreeChart setChart(JFreeChart ch);

	/**
	 * Creates a chart panel to house a JFreeChart. This panel is needed to disable
	 * buffering so the image vectoring is produced correctly.
	 * 
	 * @param plot
	 *            the plot to be loaded into the panel
	 * @return chart panel containing specified plot
	 */
	public static ChartPanel createPanel(JFreeChart plot) {

		ChartPanel panel = new ChartPanel(plot, false);
		panel.setMinimumDrawHeight(150);
		panel.setMinimumDrawWidth(150);
		panel.setMaximumDrawHeight(999999);
		panel.setMaximumDrawWidth(999999);
		panel.setBackground(null);
		panel.setOpaque(false);
		return panel;
	}

	public static XYSeriesCollection buildXYSeriesCollection(DataSet data) {

		XYSeriesCollection seriesCollection = new XYSeriesCollection();
		ArrayList<String> sortedSeriesNames = new ArrayList<String>(data.getSeriesMap().keySet());
		Collections.sort(sortedSeriesNames);
		for (String seriesName : sortedSeriesNames) {
			DataSeries chartSeries = data.getSeries(seriesName);
			XYSeries newSeries = new XYSeries(seriesName, false, true);
			newSeries.setDescription(seriesName);
			for (Object xValueObj : chartSeries.getXValues()) {
				if (ObjectEvaluator.isNumericalValue(xValueObj)) {
					Double xValue = ObjectEvaluator.getDoubleValue(xValueObj);
					Object yValueObj = chartSeries.getYValue(xValueObj);
					if (ObjectEvaluator.isNumericalValue(yValueObj)) {
						Double yValue = ObjectEvaluator.getDoubleValue(yValueObj);

						newSeries.add(xValue, yValue);
					}
				}
			}
			if (newSeries.getItemCount() > 0) {
				seriesCollection.addSeries(newSeries);
			}

		}

		return seriesCollection;
	}
}
