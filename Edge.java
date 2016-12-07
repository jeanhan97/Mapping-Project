/*
 * Name: Ji Eun Han
 * Assignment: Project 04
 * Section: TR 9:40am
 * Lab TA: Chengyu Deng, Matt Delsordo
 * I collaborated with Mackenzie Lee. 
 */

public class Edge implements Comparable<Edge> {
	public String name;
	public String v;
	public String w;
	public double weight;
	
	//constructor 
	public Edge(String name, String v, String w, double weight) {
		this.name = name;
		this.v = v;
		this.w = w;
		this.weight = weight;
	}

	public int compareTo(Edge that) {
		if (weight == that.weight) {
			return 0;
		} else if (weight > that.weight) {
			return 1;
		} else {
			return -1;
		}
	}
}