
package com.be3short.data.chart;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYSeriesCollectionMultiDomain extends XYSeriesCollection {

	private LinkedHashMap<XYSeries, ArrayList<Integer>> jumpDomains;

	/**
	* 
	*/
	private static final long serialVersionUID = 5702609583279533398L;

	/**
	 * Constructs an empty dataset.
	 */
	public XYSeriesCollectionMultiDomain() {

		this(null);
	}

	/**
	 * Constructs a dataset and populates it with a single series.
	 *
	 * @param series
	 *            the series (<code>null</code> ignored).
	 */
	public XYSeriesCollectionMultiDomain(XYSeries series) {

		super(null);

		jumpDomains = new LinkedHashMap<XYSeries, ArrayList<Integer>>();
	}

	/**
	 * Adds a series to the collection and sends a {@link DatasetChangeEvent} to all
	 * registered listeners.
	 *
	 * @param series
	 *            the series (<code>null</code> not permitted).
	 * 
	 * @throws IllegalArgumentException
	 *             if the key for the series is null or not unique within the
	 *             dataset.
	 */
	public void addSeries(XYSeries series, ArrayList<Integer> jump_domain) {

		super.addSeries(series);
		if (jump_domain != null) {
			System.out.println(jump_domain.size());
			if (series.getItemCount() == jump_domain.size()) {
				jumpDomains.put(series, jump_domain);
			}
		}
	}

	public String jumpOccurring(int series_i, int item, double x0, double y0, double x1, double y1) {

		String jump = ChartContentRenderer.NO_ALT_DOMAIN;
		if (this.getSeriesCount() > 0) {
			XYSeries series = this.getSeries(series_i);
			if (jumpDomains.containsKey(series))

			{
				jump = ChartContentRenderer.JUMP_NOT_OCCURRING_ALT_DOMAIN;
				if (item > 0) {
					double t0 = jumpDomains.get(series).get(item - 1);
					double t1 = jumpDomains.get(series).get(item);

					if ((t0 != t1) && ((x0 != x1) || (y0 != y1))) {
						jump = ChartContentRenderer.JUMP_OCCURRING_ALT_DOMAIN;

					}
				}
			}
		}
		return jump;

	}
}
