
package com.be3short.data.chart;

import java.util.HashMap;

public interface ChartRenderData {

	public static HashMap<String, ChartRenderData> chartTypes = getBaseChartTypes();

	static HashMap<String, ChartRenderData> getBaseChartTypes() {

		HashMap<String, ChartRenderData> types = new HashMap<String, ChartRenderData>();
		types.put(Chart.LINE, new BaseChartRenderType(true, false));
		types.put(Chart.SCATTER, new BaseChartRenderType(false, true));
		types.put(Chart.SCATTER_WITH_LINE, new BaseChartRenderType(true, true));
		return types;
	}

	public static ChartRenderData getRenderType(String type) {

		if (chartTypes.containsKey(type)) {
			return chartTypes.get(type);
		}
		return null;
	}

	public boolean getRenderShapes();

	public boolean getRenderLines();

}
