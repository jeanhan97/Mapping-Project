/*
 * Name: Ji Eun Han
 * Assignment: Project 04
 * Section: TR 9:40am
 * Lab TA: Chengyu Deng, Matt Delsordo
 * I collaborated with Mackenzie Lee. 
 */

//node class for the graph 
public class MapNode implements Comparable<MapNode> {
	public String ID;
	public MapNode next;
	public double weight;
	public int cons;
	public String parent;
	public String road;
	
	//constructor
	public MapNode(String ID) {
		this.ID = ID;
		cons = 0;
	}
	
	//insert method 
	public void insert(MapNode node, String string, Double weight, String parent, String road) {
		if (node.next == null) {
			node.next = new MapNode(string);
			node.next.road = road;
			node.next.weight = weight;
			node.next.parent = parent;
			cons++;
		} else {
			insert(node.next, string, weight, parent, road);
		}
	}

	
	public double getWeight(MapNode n, String a) {
		if (n.ID.equals(a)) {
			return n.weight;
		} else {
			return getWeight(n.next, a);
		}
	}

	public MapNode[] getConnections() {
		MapNode[] nodeArray = new MapNode[cons];
		auxilary(this, nodeArray, 0);
		return nodeArray;
	}
	
	//helper class for finding connections
	public MapNode[] auxilary(MapNode n, MapNode[] a, int p) {
		if (n.next == null) {
			return a;
		} else {
			a[p] = n.next;
			return auxilary(n.next, a, p + 1);
		}
	}

	public int compareTo(MapNode that) {
		if (this.weight == that.weight) {
			return 0;
		} else if (this.weight > that.weight) {
			return 1;
		} else {
			return -1;
		}
	}
}