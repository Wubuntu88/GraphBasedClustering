import java.util.List;

public class Node {
	List<Node> neighbors;
	public List<Node> getNeighbors() {
		return neighbors;
	}
	int cluster; 
	public int getCluster() {
		return cluster;
	}

	public Node(int cluster ,List<Node> list){
		this.cluster = cluster;
		this.neighbors = list;
	}
}
