/*
 * Name: Ji Eun Han
 * Assignment: Project 04
 * Section: TR 9:40am
 * Lab TA: Chengyu Deng, Matt Delsordo
 * I collaborated with Mackenzie Lee. 
 */

public class Node implements Comparable<Node> {
	
	public double latitude;
	public double longitude;
	public double distance;
	public Node parent;
	public boolean known;
	public String name;
	public int num;

	public Node(String name, int num, double longitude, double latitude) {
		this.num = num;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		known = false;
		distance = Double.POSITIVE_INFINITY;
		parent = null;
	}

	public int compareTo(Node that) {
		if (this.distance > that.distance) {
			return 1;
		} else if (this.distance < that.distance) {
			return -1;
		} else {
			return 0;
		}
	}
}
