
package com.be3short.data.chart;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.be3short.data.figure.Figure;
import com.be3short.data.figure.FigureFactory;
import com.be3short.data.graphics.GraphicFormat;
import com.be3short.data.store.DataSet;
import com.be3short.data.store.DataSetFactory;
import com.be3short.data.store.DynamicFieldObject;

public class BaseChart extends DynamicFieldObject implements Chart {

	private ChartPanel chartPanel;

	private DataSet data;

	public BaseChart() {

		this(DataSetFactory.create());
	}

	public BaseChart(DataSet data) {

		this.data = data;
		initialize();
	}

	private void initialize() {

		JFreeChart chart = Chart.createXYChart(null);

		chartPanel = Chart.createPanel(chart);
		set(Chart.TYPE, Chart.LINE);
		set(Chart.WIDTH, Figure.defaultWidth);
		set(Chart.HEIGHT, Figure.defaultHeight);
		set(Chart.DEFAULT_SERIES_COLOR, Chart.getDefaultLibrarySeriesColors());

		float dash1[] =
			{ 2.0f };

		set(Chart.DEFAULT_JUMP_STROKE,
				new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash1, 2.0f));
		set(Chart.DEFAULT_FLOW_STROKE, new BasicStroke(1.5f));
	}

	@Override
	public Figure display() {

		if (get(Chart.DISPLAY_FIGURE) != null) {
			Figure f = get(Chart.DISPLAY_FIGURE);
			f.getFrame().dispatchEvent(new WindowEvent(f.getFrame(), WindowEvent.WINDOW_CLOSING));
		}
		Figure fig = FigureFactory.create();
		fig.addComponent(0, 0, getContent());
		fig.display();
		set(Chart.DISPLAY_FIGURE, fig);
		return fig;

	}

	/**
	 * Exports the figure to an output file in a specified image format
	 * 
	 * @param location
	 *            location where the image file is to be created
	 * @param format
	 *            image format for file to be exported to
	 * 
	 * @return true if export was a success, false otherwise
	 * 
	 */
	@Override
	public boolean export(File location, GraphicFormat format) {

		return display().exportToFile(location, format);
	}

	@Override
	public Component getContent() {

		render();
		return chartPanel;
	}

	@Override
	public void render() {

		if (chartPanel == null) {
			JFreeChart chart = Chart.createXYChart(null);

			chartPanel = Chart.createPanel(chart);
		} else {
			update();
			JFreeChart chart = Chart.createXYChart(this);
			chartPanel.setChart(chart);
		}
	}

	public void update() {

		JFreeChart c = getChart();
		HashMap<Object, Object> fieldVals = getValueMap(Chart.TYPE, Chart.X_LABEL, Chart.Y_LABEL, Chart.TITLE,
				Chart.DISPLAY_LEGEND);
		Object val = fieldVals.get(Chart.X_LABEL);
		if (val != null) {
			c.getXYPlot().getDomainAxis().setLabel(val.toString());
		}
		val = fieldVals.get(Chart.Y_LABEL);
		if (val != null) {
			c.getXYPlot().getRangeAxis().setLabel(val.toString());
		}
		val = fieldVals.get(Chart.TITLE);
		if (val != null) {
			c.setTitle(val.toString());
		}
		val = fieldVals.get(Chart.DISPLAY_LEGEND);
		if (val != null) {
			c.getLegend().setVisible(Boolean.parseBoolean(val.toString()));
		}
	}

	@Override
	public DataSet getData() {

		return data;
	}

	@Override
	public DataSet setData(DataSet data) {

		this.data = data;
		return data;
	}

	@Override
	public JFreeChart setChart(JFreeChart ch) {

		if (chartPanel == null) {
			chartPanel = Chart.createPanel(ch);
		}
		chartPanel.setChart(ch);
		return getChart();
	}

	@Override
	public JFreeChart getChart() {

		return chartPanel.getChart();
	}

}
