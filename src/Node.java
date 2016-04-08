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
	public String toString(){
		return String.format("Cluster: %d, Neighbors: %s", cluster, neighbors);
	}
}
