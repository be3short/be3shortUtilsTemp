
package com.be3short.util.io;

public class CSVFileCompiler {

	// public static <T extends DataSeries<Object, Object>> boolean
	// createCSVFile(File output, DataSet<T> data) {
	//
	// String outputString = "";
	// Object[][] outputData = data.getOutputGrid(data);
	// for (Object[] outputDataRow : outputData) {
	// String line = "";
	// for (int i = 0; i < outputDataRow.length; i++) {
	// if (i > 0) {
	// line = line + ",";
	// }
	// System.out.println("i=" + i + " " + outputDataRow[i]);
	// line = line + outputDataRow[i].toString();
	// }
	//
	// outputString = outputString + (line + "\n");
	// }
	// return FileSystemWorker.createOutputFile(output, outputString);
	// }
	//
	// public static <T extends DataSeries<Object, Object>> Object[][]
	// createOutputGrid(DataSet<T> data) {
	//
	// List<T> series = data.getSeriesList();
	// List<Object> domain = data.getDomain();
	// Object[][] output = new Object[domain.size() + 1][series.size() + 1];
	// output[0] = getHeaderValue(data);
	// Object[] previousValues = getInitialValues(data);
	// int dCount = 1;
	// for (Object domainPoint : domain) {
	// Object[] currentValues = new Object[series.size() + 1];
	// currentValues[0] = domainPoint;
	// int sCount = 0;
	// for (T ser : series) {
	// Object datVal = ser.getYValue(domainPoint);
	// if (datVal == null) {
	// datVal = previousValues[sCount];
	// } else {
	// previousValues[sCount] = datVal;
	// }
	// currentValues[1 + sCount] = datVal;
	// sCount++;
	// }
	// output[dCount++] = currentValues;
	// }
	// return output;
	// }
	//
	// public static <T extends DataSeries<Object, Object>> Object[]
	// getHeaderValue(DataSet<T> data) {
	//
	// Object[] headerValue = new Object[data.getSeriesList().size() + 1];
	// int i = 1;
	// headerValue[0] = data.getDomainLabel();
	// for (String serName : data.getSeriesMap().keySet()) {
	// headerValue[i++] = serName;
	// }
	// return headerValue;
	//
	// }
	//
	// public static <T extends DataSeries<Object, Object>> Object[]
	// getInitialValues(DataSet<T> data) {
	//
	// Object[] previousValues = new Object[data.getSeriesList().size()];
	// int i = 0;
	// for (T ser : data.getSeriesList()) {
	// Object val = ser.getYValue(ser.getXValues().get(0));
	// if (val == null) {
	// val = new Null();
	// }
	// previousValues[i++] = val;
	// }
	// return previousValues;
	//
	// }
}
