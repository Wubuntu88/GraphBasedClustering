import java.util.ArrayList;
import java.util.List;

public class Driver3 {
    public static void main(String[] args) throws Exception {
	String inputFile = "Archive/file3";
	String outputFile = "outputs/output3.txt";

	final double threshold = 10.0;
	GBC_IO gbc_io = new GBC_IO();
	GraphBasedClusterer gbc = gbc_io.instantiateKMeansClusterer(inputFile);
	gbc.setParameters(threshold / 12.0);
	gbc.cluster();

	ArrayList<Record> records = gbc.recordsCopy();
	List<Node> adjacencyList = gbc.adjacencyListCopy();
	int numberOfClusters = gbc.getNumberOfClusters();
	gbc_io.writeClusteredRecordsToFile(outputFile, records, adjacencyList, numberOfClusters);

	System.out.println(String.format("%d Clusters", gbc.getNumberOfClusters()));
	double sumSquaredError = gbc.sumSquaredError();
	System.out.println(String.format("sum squared error: %.2f", sumSquaredError));
    }
}
