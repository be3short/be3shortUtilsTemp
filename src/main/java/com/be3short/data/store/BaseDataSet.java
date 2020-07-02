
package com.be3short.data.store;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.be3short.util.io.FileParser;
import com.be3short.util.io.ObjectEvaluator;
import com.be3short.util.var.Null;

public class BaseDataSet extends DynamicFieldObject implements DataSet {

	private ArrayList<DataSeries> seriesList;

	private Map<DataSeries, ArrayList<Double>> timeDomain;

	private String domainLabel;

	public BaseDataSet(DataSeries... new_series) {

		domainLabel = DataSet.defaultDomainName;
		seriesList = new ArrayList<DataSeries>();
		timeDomain = new LinkedHashMap<DataSeries, ArrayList<Double>>();
		addSeries(new_series);
	}

	@Override
	public Map<String, DataSeries> getSeriesMap() {

		LinkedHashMap<String, DataSeries> seriesMap = new LinkedHashMap<String, DataSeries>();
		ArrayList<String> names = new ArrayList<String>();
		for (DataSeries series : seriesList) {
			String seriesName = series.getLabel();
			int i = 1;
			while (names.contains(seriesName)) {
				seriesName = series.getLabel() + i++;
			}
			if (!seriesName.equals(series.getLabel())) {
				series.set(DataSeries.ADJUSTED_LABEL, seriesName);
			}
			names.add(seriesName);
			seriesMap.put(seriesName, series);
		}
		return seriesMap;
	}

	@Override
	public List<DataSeries> getSeriesList() {

		return seriesList;
	}

	@Override
	public DataSeries getSeries(String label) {

		for (DataSeries series : seriesList) {
			if (series.get(DataSeries.ADJUSTED_LABEL) != null) {
				if (series.get(DataSeries.ADJUSTED_LABEL).equals(label)) {
					return series;
				}
			} else {
				if (series.getLabel().equals(label)) {
					return series;
				}
			}
		}

		return null;
	}

	@Override
	public DataSeries addSeries(DataSeries... new_series) {

		DataSeries returnSeries = null;
		for (DataSeries newSeries : new_series) {
			if (!seriesList.contains(newSeries)) {
				seriesList.add(newSeries);
			}
			if (returnSeries == null) {
				returnSeries = newSeries;
			}

		}

		return returnSeries;
	}

	@Override
	public DataSeries removeSeries(DataSeries series) {

		if (seriesList.contains(series)) {
			seriesList.remove(series);
			return series;
		}
		return null;
	}

	@Override
	public boolean exportToCSV(File output) {

		return FileParser.createCSVFile(output, this);
	}

	@Override
	public <D> List<D> getDomain() {

		ArrayList<D> domain = new ArrayList<D>();
		ArrayList<DataSeries> dataSeries = new ArrayList<DataSeries>();
		for (DataSeries s : seriesList) {
			dataSeries.add(s);
		}
		for (DataSeries series : getSeriesMap().values()) {
			for (Object xValueObj : series.getXValues()) {
				if (!domain.contains(xValueObj)) {
					D xAdd = null;
					try {
						xAdd = (D) xValueObj;
						domain.add(xAdd);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (domain.size() > 0) {
			if (ObjectEvaluator.isDoubleValue(domain.get(0))) {
				List<Double> doubleList = (List<Double>) domain;
				Collections.sort(doubleList);
				domain = (ArrayList<D>) doubleList;
			}
		}
		return domain;
	}

	@Override
	public String getDomainLabel() {

		if (getDomain().size() > 0) {
			// if (domainLabel.equals(EnvironmentSettings.defaultDomainLabel)) {

			// if (getDomain().get(0).getClass().equals(HybridTime.class)) {
			// return "t,j";
			// }
			// }
		}
		return domainLabel;
	}

	@Override
	public Object[][] getDataGrid() {

		List<DataSeries> series = this.getSeriesList();
		List<Object> domain = this.getDomain();
		int DomainWidth = getDomainWidth(this);
		// System.out.println(series.toString());
		Object[][] output = new Object[domain.size() + 1][series.size() + DomainWidth];
		output[0] = getHeaderValue(this);
		Object[] previousValues = getInitialValues(this);
		int dCount = 1;
		for (Object domainPoint : domain) {
			Object[] currentValues = new Object[series.size() + DomainWidth];
			loadDomain(domainPoint, currentValues);
			int sCount = 0;
			for (DataSeries ser : series) {
				Object datVal = ser.getYValue(domainPoint);
				// System.out.println(ser.getLabel());
				// XMLParser.print(currentValues);
				// System.out.println(datVal + " " + domainPoint);
				if (datVal == null) {
					datVal = previousValues[sCount];
				} else {
					previousValues[sCount] = datVal;
				}
				currentValues[DomainWidth + sCount] = datVal;
				sCount++;
			}
			output[dCount++] = currentValues;
		}
		return output;
	}

	public static int getDomainWidth(DataSet data) {

		List<Object> domain = data.getDomain();
		int domainWidth = 1;
		if (domain.size() > 0) {
			// if (domain.get(0).getClass().equals(HybridTime.class)) {
			// domainWidth = 2;
			// }
		}
		return domainWidth;
	}

	public static <T extends XYDataSeries<?, ?>> void loadDomain(Object domain_object, Object[] data_line) {

		// if (domain_object.getClass().equals(HybridTime.class)) {
		// HybridTime ht = (HybridTime) domain_object;
		//
		// data_line[0] = ht.getTime();
		// data_line[1] = ht.getJumps();
		// System.exit(0);
		// } else {
		data_line[0] = domain_object;
		// }

	}

	public static <T extends XYDataSeries<?, ?>> Object[] getHeaderValue(DataSet data) {

		int domainWidth = getDomainWidth(data);
		Object[] headerValue = new Object[data.getSeriesList().size() + domainWidth];
		headerValue[0] = data.getDomainLabel();
		int i = domainWidth;
		// if (domainWidth > 1) {
		// headerValue[0] = EnvironmentSettings.defaultHybridDomainTimeLabel;
		// headerValue[1] = EnvironmentSettings.defaultHybridDomainJumpLabel;

		// }
		for (String serName : data.getSeriesMap().keySet()) {
			headerValue[i++] = serName;
		}
		return headerValue;

	}

	public static Object[] getInitialValues(DataSet data) {

		Object[] previousValues = new Object[data.getSeriesList().size()];
		int i = 0;
		for (DataSeries ser : data.getSeriesList()) {
			Object val = ser.getYValue(ser.getXValues().get(0));
			if (val == null) {
				val = new Null();
			}
			previousValues[i++] = val;
		}
		return previousValues;

	}

	@Override
	public String setDomainLabel(String label) {

		domainLabel = label;
		return domainLabel;
	}

	@Override
	public DataSet[] addDataSet(DataSet... datasets) {

		for (DataSet dataset : datasets) {
			for (DataSeries series : dataset.getSeriesList()) {
				seriesList.add(series);
			}
		}
		return datasets;
	}

	@Override
	public ArrayList<Integer> setJumpDomain(DataSeries series, ArrayList<Integer> times) {

		series.setJumpDomain(times);
		return times;
	}

	@Override
	public ArrayList<Integer> getJumpDomain(DataSeries series) {

		ArrayList<Integer> dom = null;
		if (series.getJumpDomain() != null) {
			dom = series.getJumpDomain();
		}
		return dom;
	}

}
