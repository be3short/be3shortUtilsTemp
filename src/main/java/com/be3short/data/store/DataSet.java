
package com.be3short.data.store;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 17074
 *
 */
public interface DataSet {

	public static String defaultDomainName = "Domain";

	public Object[][] getDataGrid();

	public Map<String, DataSeries> getSeriesMap();

	public List<DataSeries> getSeriesList();

	public DataSeries getSeries(String label);

	public DataSeries addSeries(DataSeries... series);

	public DataSeries removeSeries(DataSeries series);

	public DataSet[] addDataSet(DataSet... datasets);

	public <D> List<D> getDomain();

	public String getDomainLabel();

	public String setDomainLabel(String label);

	public boolean exportToCSV(File output);

	public ArrayList<Integer> setJumpDomain(DataSeries series, ArrayList<Integer> times);

	public ArrayList<Integer> getJumpDomain(DataSeries series);
}
