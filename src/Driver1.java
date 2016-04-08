import java.util.ArrayList;

public class Driver1 {
	public static void main(String[] args) throws Exception {
		String inputFile = "Archive/file2";
		String outputFile = "output2.txt";
		
		final double threshold = .1;
		GBC_IO gbc_io = new GBC_IO();
		GraphBasedClusterer gbc = gbc_io.instantiateKMeansClusterer(inputFile);
		gbc.setParameters(threshold);
		gbc.cluster();
		
		System.out.println(String.format("%d Clusters", gbc.getNumberOfClusters()));
		System.out.println(gbc);
	}
}
