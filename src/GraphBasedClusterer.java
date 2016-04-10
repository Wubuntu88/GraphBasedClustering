import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GraphBasedClusterer {
    private ArrayList<Record> records;
    private int numberOfClusters = -1; // to be determined in
				       // assignClustersToAdjacencyList()
    private boolean hasSetParameters = false;
    private boolean hasPerformedClustering = false;
    private HashMap<Integer, double[]> clusterCentroids;
    private double threshold;
    private double[][] distMatrix;

    private List<Node> adjacencyList;

    public GraphBasedClusterer(ArrayList<Record> records) {
	this.records = records;
	this.distMatrix = this.createDistanceMatrix(records);
    }

    public List<Node> adjacencyListCopy() {
	return new ArrayList<>(this.adjacencyList);
    }

    private void assignClustersToAdjacencyList(List<Node> adjacencyList) {
	Queue<Integer> queue = new LinkedList<>();
	int cluster = 0;
	for (int i = 0; i < adjacencyList.size(); i++) {
	    if (adjacencyList.get(i).getCluster() < 0) {
		queue.add(i);
		while (queue.isEmpty() == false) {
		    int index = queue.remove();
		    Node node = adjacencyList.get(index);
		    node.setCluster(cluster);
		    for (int neighbor : node.getNeighbors()) {
			if (adjacencyList.get(neighbor).getCluster() < 0) {
			    queue.add(neighbor);
			}
		    }
		}
		cluster++;
	    }
	}
	this.numberOfClusters = cluster;
    }

    private List<Node> buildAdjacencyList(ArrayList<Record> records, double[][] distanceMatrix) {
	List<Node> adjacencyList = new ArrayList<>();
	for (int i = 0; i < records.size(); i++) {
	    // find neighbors (i.e. records at indices) of node at i
	    List<Integer> neighbors = new ArrayList<>();
	    for (int j = 0; j < records.size(); j++) {
		if (i == j) {
		    continue;
		}
		if (distanceMatrix[i][j] < this.threshold) {
		    neighbors.add(j);
		}
	    }
	    Node nodeToAdd = new Node(-1, neighbors);
	    adjacencyList.add(nodeToAdd);// -1 means no cluster yet
	}
	return adjacencyList;
    }

    public void cluster() {
	if (this.hasSetParameters) {
	    this.distMatrix = this.createDistanceMatrix(this.records);
	    this.adjacencyList = this.buildAdjacencyList(this.records, this.distMatrix);
	    this.assignClustersToAdjacencyList(this.adjacencyList);
	    this.hasPerformedClustering = true;
	    this.clusterCentroids = this.computeCentroids();
	} else {
	    System.out.println("Set parameters before performing clustering.");
	}
    }

    private HashMap<Integer, double[]> computeCentroids() {
	HashMap<Integer, double[]> centroids = new HashMap<>();
	int[] quantityInCluster = new int[this.numberOfClusters];
	// first I get the sum of the cluster, then divide by the mean.
	for (int i = 0; i < this.adjacencyList.size(); i++) {
	    double[] attr = this.records.get(i).getAttrList();
	    int cluster = this.adjacencyList.get(i).getCluster();

	    if (centroids.containsKey(cluster) == false) {
		double[] firstPoint = Arrays.copyOf(attr, attr.length);
		centroids.put(cluster, firstPoint);
	    } else {// getting the sum of the points in a cluster
		double[] developingCentroid = centroids.get(cluster);
		for (int j = 0; j < developingCentroid.length; j++) {
		    developingCentroid[j] += attr[j];
		}
	    }
	    quantityInCluster[cluster]++;
	}

	for (Integer cluster : centroids.keySet()) {
	    double[] theCentroid = centroids.get(cluster);
	    for (int k = 0; k < theCentroid.length; k++) {
		theCentroid[k] /= quantityInCluster[cluster];
	    }
	}
	return centroids;
    }

    private double[][] createDistanceMatrix(ArrayList<Record> records) {
	double[][] distanceMatrix = new double[records.size()][records.size()];
	for (int row = 0; row < records.size(); row++) {
	    for (int col = 0; col < records.size(); col++) {
		double[] attrList1 = records.get(row).getAttrList();
		double[] attrList2 = records.get(col).getAttrList();
		double distance = this.euclideanDistance(attrList1, attrList2);
		distanceMatrix[row][col] = distance;
	    }
	}
	return distanceMatrix;
    }

    private double euclideanDistance(double[] p1, double[] p2) {
	assert p1.length == p2.length;
	ArrayList<Double> sq_err = new ArrayList<>();
	for (int i = 0; i < p1.length; i++) {
	    sq_err.add(Math.abs(p1[i] - p2[i]));
	}
	double sum_sq_err = sq_err.stream().map(x -> x * x).reduce((x, y) -> x + y).get();
	return Math.sqrt(sum_sq_err);
    }

    public int getNumberOfClusters() {
	return this.numberOfClusters;
    }

    public ArrayList<Record> recordsCopy() {
	ArrayList<Record> copy = new ArrayList<>();
	for (Record record : this.records) {
	    copy.add(new Record(record.getAttrList()));
	}
	return copy;
    }

    public boolean setParameters(double threshold) {
	if (this.hasSetParameters == false) {
	    this.threshold = threshold;
	    this.hasSetParameters = true;
	    return true;
	} else {
	    System.out.println("error: you may only set the parameters once for an instance of this class.");
	    return false;
	}
    }

    public double sumSquaredError() {
	double sum = 0.0;
	for (int i = 0; i < this.adjacencyList.size(); i++) {
	    double[] attrs = this.records.get(i).getAttrList();
	    int clusterOfRecord = this.adjacencyList.get(i).getCluster();
	    double[] centroid = this.clusterCentroids.get(clusterOfRecord);
	    double dist = this.euclideanDistance(attrs, centroid);
	    sum += Math.pow(dist, 2);
	}
	return sum;
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer("");

	for (int cluster = 0; cluster < this.numberOfClusters; cluster++) {
	    sb.append(String.format("Cluster %d\n", cluster));
	    for (int k = 0; k < this.records.size(); k++) {
		if (this.adjacencyList.get(k).getCluster() == cluster) {
		    sb.append(String.format("%s\n", this.records.get(k).toString()));
		}
	    }
	}
	sb.replace(sb.length() - 1, sb.length(), "");
	return sb.toString();
    }

}
