import java.util.ArrayList;
import java.util.List;

public class Node {
	private List<Integer> neighbors;
	public List<Integer> getNeighbors() {
		return neighbors;
	}
	private int cluster; 
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}

	public int getCluster() {
		return cluster;
	}

	public Node(int cluster, List<Integer> list){
		this.cluster = cluster;
		this.neighbors = list;
	}
	
	public Node(Node node){
		this.neighbors = new ArrayList<>(node.getNeighbors());
		this.cluster = node.getCluster();
	}
	
	public String toString(){
		return String.format("Cluster: %d, Neighbors: %s", cluster, neighbors);
	}
}
