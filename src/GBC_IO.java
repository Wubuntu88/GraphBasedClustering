import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class GBC_IO {
    public static final String BINARY = "binary";
    public static final String CATEGORICAL = "categorical";
    public static final String ORDINAL = "ordinal";
    public static final String CONTINUOUS = "continuous";
    public static final TreeSet<String> attributeTypeSet = new TreeSet<String>(
	    Arrays.asList(BINARY, CATEGORICAL, ORDINAL, CONTINUOUS));

    // for continuous data
    private HashMap<Integer, double[]> rangeAtColumn = new HashMap<>();
    // for binary, categorical, ordinal data
    private HashMap<Integer, HashMap<String, Double>> symbolToValueAtColumn = new HashMap<>();

    // list of attr types: continuous, oridinal, categorical, etc.
    private ArrayList<String> attributeTypeList = new ArrayList<>();

    public int attributeTypeListSize() {
	return this.attributeTypeList.size();
    }

    private String clusterResultsString(ArrayList<Record> records, List<Node> adjacencyList, int numberOfClusters) {
	StringBuffer sb = new StringBuffer("");
	for (int i = 0; i < numberOfClusters; i++) {
	    sb.append(String.format("Cluster %d\n", i));
	    for (int k = 0; k < records.size(); k++) {
		if (adjacencyList.get(k).getCluster() == i) {
		    sb.append(String.format("%s\n", records.get(k).toString()));
		}
	    }
	}
	sb.replace(sb.length() - 1, sb.length(), "");
	return sb.toString();
    }

    private double denormalizeContinousNumberAtColumn(double normalizedNumber, int column) {
	double[] theRange = this.rangeAtColumn.get(column);
	double origionalNumber = normalizedNumber * (theRange[1] - theRange[0]) + theRange[0];
	return origionalNumber;
    }

    public GraphBasedClusterer instantiateKMeansClusterer(String fileName) throws Exception {
	String whitespace = "[ ]+";
	List<String> lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
	// first line is unneeded

	// second line ( these is the attribute types )
	String[] componentsOfFirstLine = lines.get(1).split(whitespace);
	for (String attrType : componentsOfFirstLine) {
	    if (GBC_IO.attributeTypeSet.contains(attrType) == true) {
		this.attributeTypeList.add(attrType);
	    } else {
		throw new Exception("attribute in file not one of the correct attributes");
	    }
	}
	String[] listOfRanges = lines.get(2).split(whitespace);
	for (int colIndex = 0; colIndex < listOfRanges.length; colIndex++) {
	    // range symbols are low to high
	    String[] strRange = listOfRanges[colIndex].split(",");
	    double[] dubRange = new double[strRange.length];
	    String typeOfAttrAtIndex = this.attributeTypeList.get(colIndex);
	    if (typeOfAttrAtIndex.equals(GBC_IO.CONTINUOUS) == false) {
		HashMap<String, Double> symbolToVal = new HashMap<>(10);
		int denominator = dubRange.length - 1;
		int counter = 0;
		for (String symbol : strRange) {
		    symbolToVal.put(symbol, (double) counter / denominator);
		    counter++;
		}
		this.symbolToValueAtColumn.put(colIndex, symbolToVal);// for non
								      // continuous
								      // columns
	    } else {// if it is continuous
		assert strRange.length == 2;
		dubRange[0] = Double.parseDouble(strRange[0]);
		dubRange[1] = Double.parseDouble(strRange[1]);
		this.rangeAtColumn.put(colIndex, dubRange);// for continuous
							   // columns
	    }
	}

	// now I create the records after initializing the hasmaps
	ArrayList<Record> records = new ArrayList<>();
	for (int i = 3; i < lines.size(); i++) {
	    String[] comps = lines.get(i).split(whitespace);
	    Record record = this.translateLineComponentsIntoRecords(comps);
	    records.add(record);
	}
	GraphBasedClusterer gbc = new GraphBasedClusterer(records);
	return gbc;
    }

    private double normalizeContinuousVariableAtColumn(double number, int column) {
	double[] theRange = this.rangeAtColumn.get(column);
	double translation = (number - theRange[0]) / (theRange[1] - theRange[0]);
	return translation;
    }

    private Record translateLineComponentsIntoRecords(String[] comps) {
	double[] attrs = new double[comps.length];

	for (int colIndex = 0; colIndex < comps.length; colIndex++) {
	    String attrType = this.attributeTypeList.get(colIndex);
	    if (attrType.equals(CONTINUOUS)) {// for continous columns
		double number = Double.parseDouble(comps[colIndex]);
		double normalizedNumber = this.normalizeContinuousVariableAtColumn(number, colIndex);
		attrs[colIndex] = normalizedNumber;
	    } else {// for non-continous data
		HashMap<String, Double> symbolToValue = this.symbolToValueAtColumn.get(colIndex);
		double value = symbolToValue.get(comps[colIndex]);
		attrs[colIndex] = value;
	    }
	}
	Record record = new Record(attrs);
	return record;
    }

    public void writeClusteredRecordsToFile(String fileName, ArrayList<Record> records, List<Node> adjacencyList,
	    int numberOfClusters) {
	ArrayList<Record> denormalizedRecords = new ArrayList<>();
	for (Record record : records) {
	    double[] attrs = new double[record.getAttrList().length];
	    for (int i = 0; i < attrs.length; i++) {
		attrs[i] = this.denormalizeContinousNumberAtColumn(record.getAttrList()[i], i);
	    }
	    Record recordToAdd = new Record(attrs);
	    denormalizedRecords.add(recordToAdd);
	}
	PrintWriter pw = null;
	try {
	    pw = new PrintWriter(fileName);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	pw.println(String.format("%d clusters", numberOfClusters));
	String str = this.clusterResultsString(denormalizedRecords, adjacencyList, numberOfClusters);
	pw.print(str);
	pw.close();
    }
}
