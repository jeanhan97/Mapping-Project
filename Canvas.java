import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
/*
 * Name: Ji Eun Han
 * Assignment: Project 04
 * Section: TR 9:40am
 * Lab TA: Chengyu Deng, Matt Delsordo
 * I collaborated with Mackenzie Lee. 
 */

public class Canvas extends JPanel {

	public HashMap<String, Node> nodeHashMap;
	public HashMap<String, Edge> edgeHashMap;
	public ArrayList<Edge> edgeList;
	public ArrayList<Node> nodeList;
	public double xCoord;
	public double yCoord;
	public double minLat;
	public double minLong;
	public double maxLat;
	public double maxLong;
	public boolean type;

	//constructor
	public Canvas(HashMap<String, Node> nodeHashMap, HashMap<String, Edge> edgeHashMap, ArrayList<Edge> edgeList,
			ArrayList<Node> nodeLIst, boolean type) {
		setFocusable(true);
		this.nodeHashMap = nodeHashMap;
		this.edgeHashMap = edgeHashMap;
		this.edgeList = edgeList;
		this.nodeList = nodeLIst;
		this.type = type;
		double[] extreme = bound();
		minLat = extreme[0];
		maxLat = extreme[1];
		minLong = extreme[2];
		maxLong = extreme[3];
	}
	
	public double[] bound() {
		double[] bounds = { Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY };
		for (Node n : nodeHashMap.values()) {
			if (n.latitude < bounds[0]) {
				bounds[0] = n.latitude;
			}
			if (n.latitude > bounds[1]) {
				bounds[1] = n.latitude;
			}
			if (n.longitude < bounds[2]) {
				bounds[2] = n.longitude;
			}
			if (n.longitude > bounds[3]) {
				bounds[3] = n.longitude;
			}
		}
		return bounds;
	}
	/**
	 * First draws the map in its entirety, then draws either the shortest path
	 * or meridian map in a different color.
	 * 
	 * The total runtime is O(E) + O(E) = O(2E) for the minimum weight spanning
	 * tree. This is based on the fact that all the edges must be painted which
	 * is found to be O(E) due to the fact that all edges must be iterated
	 * through. Then the minimum weight spanning tree has a O(E) based on the
	 * fact that the worst case tree would be all the edges. So this would mean
	 * you must take both O(E) into account so you get O(E) in the end.
	 * 
	 * The total runtime is O(E) + O(V) = O(E+V) for the shortest path
	 * algorithm. Again, drawing the entire map takes O(E), but finding the
	 * shortest path has O(V) because of iterating through all the nodes. This
	 * means you would get O(E+V) in the end.
	 */

	// help taken from
	// https://www.ntu.edu.sg/home/ehchua/programming/java/J8b_Game_2DGraphics.html
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		// g2d.setStroke(new BasicStroke(1));
		yCoord = getWidth() / (maxLong - minLong);// use of getWidth and getHeight so map can be resized
		xCoord = getHeight() / (maxLat - minLat);
		edgePainter(g2d);// runtime is O(E) based on below
		if (type == true) {// draw shortest path
			dijkstraPath(g2d);// runtime is O(V) based on below
		} else {// draw meridian map
			meridianMap(g2d); // O(E)
		}
	}
	//draws all the edges for the map so the map is visible
	//The Big-Oh runtime depends on the number of edges so the total runtime is O(E)
	public void edgePainter(Graphics2D g2d) {
		//iterates through all the edges in the hashMap of edges so the runtime is O(E)
		for (Edge e : edgeHashMap.values()) { 
			Node node1 = nodeHashMap.get(e.w);
			Node node2 = nodeHashMap.get(e.v);
			int xCoord1 = (int) ((getHeight() - Math.abs(node1.latitude - Math.abs(minLat)) * xCoord));
			int yCoord1 = (int) (((node1.longitude * yCoord)) - minLong * yCoord);
			int xCoord2 = (int) ((getHeight() - Math.abs(node2.latitude - Math.abs(minLat)) * xCoord));
			int yCoord2 = (int) (((node2.longitude * yCoord)) - minLong * yCoord);
			g2d.drawLine(yCoord1, xCoord1, yCoord2, xCoord2);//draws all the edges which is O(E)
		}
	}
	
	//the worst case runtime of drawing a minimum weight spanning tree is that all edges must be visited
	//this would mean the worst case is also O(E)
	public void meridianMap(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.MAGENTA);
		//must iterate through all the edges so the runtime is also O(E) where E is number of edges 
		for (Edge e : edgeList) {
			Node node1 = nodeHashMap.get(e.w);
			Node node2 = nodeHashMap.get(e.v);
			int xCoord1 = (int) ((getHeight() - Math.abs(node1.latitude - Math.abs(minLat)) * xCoord));
			int yCoord1 = (int) (((node1.longitude * yCoord)) - minLong * yCoord);
			int xCoord2 = (int) ((getHeight() - Math.abs(node2.latitude - Math.abs(minLat)) * xCoord));
			int yCoord2 = (int) (((node2.longitude * yCoord)) - minLong * yCoord);
			g2d.drawLine(yCoord1, xCoord1, yCoord2, xCoord2);//draws all the lines and worst case is O(E) since all edges could be drawn
		}
	}
	
	//This draws the shortest path using Dijkstra's Algorithm and iterates through all the nodes
	public void dijkstraPath(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.ORANGE);
		Node node = nodeList.get(0);
		//iterates through all the nodes so this takes O(V) where v is number of vertices/nodes
		for (int i = 1; i < nodeList.size(); i++) {
			Node b = nodeList.get(i);
			int xCoord1 = (int) ((getHeight() - Math.abs(node.latitude - Math.abs(minLat)) * xCoord));
			int yCoord1 = (int) (((node.longitude * yCoord)) - minLong * yCoord);
			int xCoord2 = (int) ((getHeight() - Math.abs(b.latitude - Math.abs(minLat)) * xCoord));
			int yCoord2 = (int) (((b.longitude * yCoord)) - minLong * yCoord);
			g2d.drawLine(yCoord1, xCoord1, yCoord2, xCoord2);//draws the path based on the nodes visited 
			node = b;
		}
	}

	
}