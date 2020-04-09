
package com.be3short.data.chart;

/**
 * A specification that indicates what type of plot is to be rendered. This
 * specification includes the rendering characteristics of each type.
 * 
 * Intended Operator: User
 */
public class BaseChartRenderType implements ChartRenderData {

	/**
	 * Flag indicating if lines should be rendered for the given type.
	 */
	boolean renderLines;

	/**
	 * Flag indicating if lines should be rendered for the given type.
	 */
	boolean renderShapes;

	/**
	 * Constructs the chart type and assigns the rendering characteristics.
	 * 
	 * @param lines
	 *            flag indicating line visibility
	 * @param shapes
	 *            flag indicating shape visibility
	 */
	public BaseChartRenderType(boolean lines, boolean shapes) {

		renderLines = lines;
		renderShapes = shapes;
	}

	public static ChartRenderData getFromString(String string) {

		ChartRenderData type = null;
		if (ChartRenderData.chartTypes.containsKey(string)) {
			type = ChartRenderData.chartTypes.get(string);
		}

		return type;
	}

	@Override
	public boolean getRenderShapes() {

		// TODO Auto-generated method stub
		return renderShapes;
	}

	@Override
	public boolean getRenderLines() {

		// TODO Auto-generated method stub
		return renderLines;
	}

}
