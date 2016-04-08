import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GraphBasedClusterer {
	private ArrayList<Record> records;
	public ArrayList<Record> recordsCopy(){
		ArrayList<Record> copy = new ArrayList<>();
		for(Record record: records){
			copy.add(new Record(record.getAttrList()));
		}
		return copy;
	}
	private int numberOfClusters;
	public int getNumberOfClusters() {
		return numberOfClusters;
	}
	private long seed;
	private boolean hasSetParameters = false;
	private boolean hasPerformedClustering = false;
	private HashMap<Integer, double[]> clusterCentroids;
	private int[] clustersThatRecordsBelongTo;
	public int[] clustersThatRecordsBelongToCopy(){
		return Arrays.copyOf(clustersThatRecordsBelongTo, clustersThatRecordsBelongTo.length);
	}
	private double threshold;
	
	private int MAX_ITERATIONS = 10;
	
	private double[][] distMatrix;
	private List<Node> adjacencyList;
	
	public GraphBasedClusterer(ArrayList<Record> records) {
		this.records = records;
		this.numberOfClusters = 4;
		this.seed = 43546903;
		this.clustersThatRecordsBelongTo = new int[this.records.size()];
		this.distMatrix = createDistanceMatrix(records);
	}
	
	public boolean setParameters(int numberOfClusters, double threshold,int seed){
		if(hasSetParameters == false){
			if(numberOfClusters > this.records.size()){
				System.out.println("error: number of clusters cannot be greater than number of records.");
				return false;
			}
			this.numberOfClusters = numberOfClusters;
			this.threshold = threshold;
			this.seed = seed;
			this.clusterCentroids = new HashMap<>();
			this.hasSetParameters = true;
			return true;
		}else{
			System.out.println("error: you may only set the parameters once for an instance of this class.");
			return false;
		}
	}
	
	private double[][] createDistanceMatrix(ArrayList<Record> records){
		double[][] distanceMatrix = new double[records.size()][records.size()];
		for(int row = 0; row < records.size(); row++){
			for(int col = 0; col < records.size(); col++){
				double[] attrList1 = records.get(row).getAttrList();
				double[] attrList2 = records.get(col).getAttrList();
				double distance = euclideanDistance(attrList1, attrList2);
				distanceMatrix[row][col] = distance;
			}
		}
		return distanceMatrix;
	}
	
	private void cluster(){
		
	}
	
	private List<Node> buildAdjacencyList(ArrayList<Record> records, double[][] distanceMatrix){
		List<Node> adjacencyList = new ArrayList<>();
		for(int i = 0; i < records.size();i++){
			
		}
		return adjacencyList;
	}
	
	private double euclideanDistance(double[] p1, double[] p2){
		assert p1.length == p2.length;
		ArrayList<Double> sq_err = new ArrayList<>();
		for(int i = 0; i < p1.length; i++){
			sq_err.add(Math.abs(p1[i] - p2[i]));
		}
		double sum_sq_err = sq_err.stream().map(x -> x * x).reduce( (x, y) -> x + y).get();
		return Math.sqrt(sum_sq_err);
	}
	
	
	
	public double sumSquaredError(){
		double sum = 0.0;
		int recordIndex = 0;
		for (Record record : this.records) {
			double[] attrs = record.getAttrList();
			int clusterOfRecord = clustersThatRecordsBelongTo[recordIndex];
			double[] centroid = clusterCentroids.get(clusterOfRecord);
			double dist = euclideanDistance(attrs, centroid);
			sum += Math.pow(dist, 2);
			recordIndex++;
		}
		return sum;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("");
		
		for(int i = 0; i < numberOfClusters;i++){
			sb.append(String.format("Cluster %d\n", i));
			for(int k = 0; k < records.size(); k++){
				if (clustersThatRecordsBelongTo[k] == i) {
					sb.append(String.format("%s\n", records.get(k).toString()));
				}
			}
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}
}












