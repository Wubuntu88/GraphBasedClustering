import java.util.ArrayList;
import java.util.List;

public class Driver4 {
    public static void main(String[] args) throws Exception {
	String inputFile = "Archive/file4";
	String outputFile = "outputs/output4.txt";

	final double threshold = .2;
	GBC_IO gbc_io = new GBC_IO();
	GraphBasedClusterer gbc = gbc_io.instantiateKMeansClusterer(inputFile);
	gbc.setParameters(threshold);
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
