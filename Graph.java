import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.ArrayList;
import javax.swing.JFrame;
/*
 * Name: Ji Eun Han
 * Assignment: Project 04
 * Section: TR 9:40am
 * Lab TA: Chengyu Deng, Matt Delsordo
 * I collaborated with Mackenzie Lee. 
 */

public class Graph {
	public static Scanner scan;
	public int nodeNum;
	public int edgeNum;
	public PQueue<Node> pathQueue;
	public ArrayList<Node> nodeList;
	public ArrayList<Edge> edgeList;
	public HashMap<String, Node> nodeHashMap;
	public HashMap<String, Edge> edgeHashMap;
	public boolean directed;
	public boolean adj[][];
	public double dist;
	public MapNode adjNode[];
	public HashMap<String, MapNode> adjNodeHashMap;
	public double weight[][];
	
	//constructor
	public Graph() {
		nodeHashMap = new HashMap<String, Node>();
		edgeHashMap = new HashMap<String, Edge>();
		adjNodeHashMap = new HashMap<String, MapNode>();
		pathQueue = new PQueue<Node>();
		nodeList = new ArrayList<Node>();
		edgeNum = 0;
	}
	

	
	//creating the graph from ur.txt, nys.txt, or monroe.txt
	public static Graph createFromFile(String fileName) {
		Graph graph = new Graph();
		File file = new File(fileName);
		try {
			scan = new Scanner(file);
		} catch (Exception e) {
			System.out.println("File Not Found");
			return null;
		}
		//the format of the text files separates the inputs by tabs and new lines
		//help taken from http://www.vogella.com/tutorials/JavaRegularExpressions/article.html
		scan.useDelimiter("\\t|\\n");//use of regex separates by tab and line
		int temp = 0;
		while (scan.hasNext()) {
			if (scan.next().equals("r")) {
				break;
			} else {//use to gather intersection data
				String name = scan.next();
				double latitude = scan.nextDouble();
				double longitude = scan.nextDouble();
				Node node = new Node(name, temp, longitude, latitude);
				graph.nodeHashMap.put(name, node);
			}
			temp++;
		}
		graph.setnodeNum(temp);
		graph.setnodeNum(temp);
		String road = scan.next();
		String intersection1 = scan.next();
		String intersection2 = scan.next();
		Node first = graph.nodeHashMap.get(intersection1);
		Node second = graph.nodeHashMap.get(intersection2);
		double weight = getDistance(first, second);
		graph.insertList(intersection1, intersection2, weight, road);
		graph.edgeHashMap.put(road, new Edge(road, intersection1, intersection2, weight));
		graph.edgeNum++;
		while (scan.hasNext()) {
			String skip = scan.next(); //read the r just to move past it 
			String roadName = scan.next();
			String inter1 = scan.next();
			String inter2 = scan.next();
			Node node1 = graph.nodeHashMap.get(inter1);
			Node node2 = graph.nodeHashMap.get(inter2);
			double distt = getDistance(node1, node2);
			graph.insertList(inter1, inter2, distt, roadName);
			graph.edgeHashMap.put(roadName, new Edge(roadName, inter1, inter2, distt));
			graph.edgeNum++;
		}
		return graph;
	}
	
	//insert into hashMap
	public void insertList(String start, String end, double weight, String road) {
		MapNode node = adjNodeHashMap.get(start);
		if (node == null) {
			adjNodeHashMap.put(start, new MapNode(start));
			node = adjNodeHashMap.get(start);
		}
		node.insert(node, end, weight, node.ID, road);
		node = adjNodeHashMap.get(end);
		if (node == null) {
			adjNodeHashMap.put(end, new MapNode(end));
			node = adjNodeHashMap.get(end);
		}
		node.insert(node, start, weight, node.ID, road);
	}
	
	//based on distance formula
	public static double getDistance(Node node1, Node node2) {
		double distance = 0;
		double yDist = Math.abs(node2.longitude) - Math.abs(node1.longitude);
		double xDist = Math.abs(node2.latitude) - Math.abs(node1.latitude);
		double square = xDist * xDist + yDist * yDist;
		distance = Math.sqrt(square);
		return distance;
	}
	
	//used to find the minimum weight spanning tree 
	/**
	 * Prim's Algorithm.
	 * Help taken form: 
	 * http://www.mathcs.emory.edu/~cheung/Courses/171/Syllabus/11-Graph/prim2.html
	 * http://algs4.cs.princeton.edu/43mst/PrimMST.java.html
	 * http://sourcecodesforfree.blogspot.in/2013/05/10-prims-algorithm.html
	 * https://www.cs.auckland.ac.nz/software/AlgAnim/prim.html
	 * 
	 * Prim's Algorithm works by starting at a node/vertex and moving to the
	 * least weighted path based on all available adjacent node connections. 
	 * This is a greedy algorithm. 
	 * 
	 * Runtime is O(|E|log|V|) because O(VlogV + ElogV) = O((E+V)logV)=O(ElogV)
	 * Using the priority queue/binary heap and adjacency list with the run time 
	 * of binary heap being VlogV and adjacency list being ElogV 
	 * The number of edges is always greater than or equal to the number of vertices
	 * so V is chosen in place of E for the runtime.
	 */
	public ArrayList<Edge> primAlgorithm(String start) {
		
		HashMap<String, String> added = new HashMap<String, String>();
		ArrayList<Edge> unvisited = new ArrayList<Edge>();
		PriorityQueue<Edge> edgeHeap = new PriorityQueue<Edge>();
		edgeList = new ArrayList<>();
		ArrayList<Edge> edgeMapList = new ArrayList<Edge>(edgeHashMap.values());
		Edge beginning = edgeMapList.get(0);
		edgeList.add(beginning);
		added.put(beginning.w, beginning.w);
		added.put(beginning.v, beginning.v);
		priorityEdgeInsert(edgeHeap, beginning);
		while (added.size() <= getEdgeNum() && edgeHeap.size() != 0) { 
			Edge removed = edgeHeap.poll();
			String w = added.get(removed.w);
			String v = added.get(removed.v);
			if ((w == null) ^ (v == null)) {
				priorityEdgeInsert(edgeHeap, removed);
				for (Edge edge : unvisited) {
					if (edge.v == removed.v || edge.v == removed.w || edge.w == removed.v || edge.w == removed.w) {
						//does nothing and acts as an eliminator
					} else {
						edgeHeap.add(edge);
					}
				}
				unvisited.clear();
				edgeList.add(removed);
				added.put(removed.w, removed.w); 
				added.put(removed.v, removed.v);
			} else if ((w == null) && (v == null)) {
				unvisited.add(removed);
			} else if (!(w == null) && !(v == null)) {
				unvisited.remove(removed);
				edgeHeap.remove(removed);
			}

		}
		return edgeList;
	}
	/**
	 *Dijkstra's Algorithm for finding the shortest path for weighted graphs.
	 *Help taken from:
	 *http://www.personal.kent.edu/~rmuhamma/Algorithms/MyAlgorithms/GraphAlgor/dijkstraAlgor.htm
	 *https://medium.com/@l__j__l/implementing-dijkstra-in-java-69bef4d54d4b#.dhxye63cu	
	 *
	 *Runtime is O(ElogV). The delete operation for the priority queue/heap 
	 *takes O(logV) time since there are V operations. The priority queue is also
	 *built in O(V) time. The bubble down operation takes O(logV) so the runtime can become 
	 *O(ElogV)
	 */
	public void dijkstraAlgorithm(Node start, Node end) { 
		start.distance = 0;
		start.parent = start;
		pathQueue.add(start);
		 // runtime is O(logV) because it is based on vertices
		while (pathQueue.size() != 0) {
			if (end.known) {
				return;
			}
			Node node = pathQueue.poll();
			node.known = true;
			MapNode hashMapNodes = adjNodeHashMap.get(node.name);
			MapNode[] connections = hashMapNodes.getConnections();
			 // O(E) since it goes for each edge
			for (MapNode mapNode : connections) {
				Node nodeinMaps = nodeHashMap.get(mapNode.ID);
				if (nodeinMaps != null) {
					if (!(nodeinMaps.known)) {
						double weight = nodeWeight(node, nodeinMaps);
						if (node.distance + weight < nodeinMaps.distance) {
							nodeinMaps.distance = node.distance + weight;
							nodeinMaps.parent = node;
							pathQueue.add(nodeinMaps);
						}
					}
				}
			}
		}
	}

	public void priorityEdgeInsert(PriorityQueue<Edge> heap, Edge edge) {
		MapNode node = adjNodeHashMap.get(edge.w);
		String rootName = node.ID;
		while (true) {
			if (node.next == null) {
				break;
			} else {
				heap.add(new Edge(node.next.road, rootName, node.next.ID, node.next.weight));
				node = node.next;
			}
		}

		node = adjNodeHashMap.get(edge.v);
		rootName = node.ID;
		while (true) {
			if (node.next == null) {
				break;
			} else {
				heap.add(new Edge(node.next.road, rootName, node.next.ID, node.next.weight));
				node = node.next;
			}
		}
	}
	
	//graphics for Dijkstra
	public void shortestPath() {
		JFrame window = new JFrame("Shortest Path Map");
		Canvas canvas = new Canvas(nodeHashMap, edgeHashMap, edgeList, nodeList, true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setSize(650, 650);
		window.add(canvas);
		window.setVisible(true);
	} 
	
	//setting up the graphics for MWST
	public void minimumWeightTree() {
		JFrame window = new JFrame("Meridian Map");
		Canvas canvas = new Canvas(nodeHashMap, edgeHashMap, edgeList, nodeList, false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setSize(650, 650);
		window.add(canvas);
		window.setVisible(true);
	}



	public double nodeWeight(Node node1, Node node2) {
		MapNode node = adjNodeHashMap.get(node1.name);
		if (node == null) {
			return Double.POSITIVE_INFINITY;
		}
		return node.getWeight(node, node2.name);
	}

	// prints the distance in miles
	public void printDistance(String start, String end, boolean print) throws Exception {
		dist = 0;
		if (start.equals(end)) {
			System.out.println("Those are the same point.");
		} else {
			Node hashStart = nodeHashMap.get(start);
			Node hashEnd = nodeHashMap.get(end);
			dijkstraAlgorithm(hashStart, hashEnd);
			printPath(hashStart, hashEnd, print);
			if (print) {
				//http://gis.stackexchange.com/questions/142326/calculating-longitude-length-in-miles
				//1 latitude and longitude is about 69 miles
				System.out.println("~~~~~~~~~~ Distance Traveled: " + hashEnd.distance*69 + " miles");
			}
		}
	}

	// print each node names that it goes through
	public void printPath(Node start, Node end, boolean print) {
		if (start.name.equals(end.name)) {
			if (print) {
				System.out.print(start.name + ", ");
			}
			dist = start.distance;
			nodeList.add(start);
			return;
		} else if (end == null || end.parent==null) {
			System.out.println("There is no path");
			return;
		} 
		printPath(start, end.parent, print);
		if (print) {
			System.out.print(end.name + ", ");
		}
		nodeList.add(end);
	}
	

	//set number of Nodes
	public void setnodeNum(int v) {
		nodeNum = v;
	}
	
	//checks if graph is directed
	public boolean isDirected() {
		return directed;
	}

	//returns the number of edges
	public int getEdgeNum() {
		return edgeNum;
	}

}