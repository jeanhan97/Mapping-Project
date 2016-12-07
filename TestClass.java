import java.util.ArrayList;
/*
 * Name: Ji Eun Han
 * Assignment: Project 04
 * Section: TR 9:40am
 * Lab TA: Chengyu Deng, Matt Delsordo
 * I collaborated with Mackenzie Lee. 
 */

public class TestClass {
	
	public static void main(String[] args) {
		String fileName = args[0];//takes in the file name first
		boolean meridianMap = false;
		boolean direction = false;
		boolean show = false;
		String start = "";
		String end = "";
		Graph graph = Graph.createFromFile(fileName);
		try {
			if (args[1].equals("-show")) {//decide whether or not to show
				show = true;
				if (args[2].equals("-meridianmap")) {
					meridianMap = true;
				} else if (args[2].equals("-directions")) {
					direction = true;
					start = args[3];
					end = args[4];
				}
			} else {//if no show, print path
				if (args[1].equals("-meridianmap")) {
					meridianMap = true;
				} else if (args[1].equals("-directions")) {
					direction = true;
					start = args[2];
					end = args[3];
				}
			}
			
			if (direction == true) {//show shortest path
				if (show) {//show map
					graph.printDistance(start, end, false);
					graph.shortestPath();
				} else {//print path and distance
					graph.printDistance(start, end, true);
				}
			}else if (meridianMap == true) {//show meridian map (MWST)
				ArrayList<Edge> edgeArray = graph.primAlgorithm("i1");//any point can be chosen as a start for prim's algorithm
				if (show) {
					graph.minimumWeightTree();
				} else {
					for (Edge e : edgeArray) {
						System.out.print(e.name + ", ");
					}
					System.out.println();
				}
			} 

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
			System.out.println(
					"Try again with the following formats (Do not enter square brackets[] since they are just for organization in showing the input):");
			System.out.println();
			System.out.println(
					"java TestClass map.txt [-show] [-directions startingIntersection endingIntersection] [-meridianmap]");
			System.out.println();
			System.out.println(
					"java TestClass map.txt [-show] [-directions startingIntersection endingIntersection] ---This will show the map displaying the shortest path between two points");
			System.out.println();
			System.out.println(
					"java TestClass map.txt [-show] [-meridianmap] ---This will show the map displaying shortest way to cover all points");
			System.out.println();
			System.out.println(
					"java TestClass map.txt [-directions startingIntersection endingIntersection] ---This will show the path and distance travelled between 2 points");
			System.out.println();
			System.out.println(
					"java TestClass map.txt [-meridianmap] ---This will show the shortest path to cover all points");
		}
	}
}