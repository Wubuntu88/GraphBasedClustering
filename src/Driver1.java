import java.util.ArrayList;

public class Driver1 {
	public static void main(String[] args) throws Exception {
		String inputFile = "Archive/file4";
		String outputFile = "output4.txt";
		
		final int NUMBER_OF_CENTROIDS = 3;
		final double threshold = .3;
		GBC_IO gbc_io = new GBC_IO();
		GraphBasedClusterer gbc = gbc_io.instantiateKMeansClusterer(inputFile);
		gbc.setParameters(NUMBER_OF_CENTROIDS, threshold, 92378);
		System.out.println(gbc);
		//gbc.cluster();
		
		
	}
}
