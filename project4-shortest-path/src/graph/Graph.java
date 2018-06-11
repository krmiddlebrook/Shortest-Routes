package graph;

/**
 * A class that represents a graph where nodes are cities (of type CityNode)
 * and edges connect them and the cost of each edge is the distance between
 * the cities.
 * Fill in code in this class. You may add additional methods and variables.
 * You are required to implement a HashTable and a PriorityQueue from scratch.
 */
import java.util.*;
import java.io.*;
import java.awt.Point;

public class Graph {
    public final int EPS_DIST = 5;

    private CityNode[] nodes; // nodes of the graph
	private int numNodes;     // total number of nodes
	private int numEdges; // total number of edges
	private Edge[] adjacencyList; // adjacency list; for each vertex stores a linked list of edges
    // Your HashTable that maps city names to node ids should probably be here as well
	private HashTable hashTable;

	/**
	 * Read graph info from the given file, and create nodes and edges of
	 * the graph.
	 *
	 * @param filename name of the file that has nodes and edges
	 */
	public void loadGraph(String filename) {
		String[] tmp; // stores a tmp array of the split string for each line in the file. The line is split by whitespace
		boolean isNode;  // returns true if we are reading a node, false if we are reading an edge
		String cityName; // stores the city name
		double x; // stores the x coordinate
		double y; // stores the y coordinate
		int nodeId1; // stores the node id of the first city in an arc
		int nodeId2; // stores the node id of the second city in an arc
		int cost; // stores the cost of an edge
		Edge edge1; // the edge for the first city
		Edge edge2; // the edge for the second city
		Edge vertex; // the vertex vi in the adjacency list

		try (FileReader file = new FileReader(filename)) {
			BufferedReader br = new BufferedReader(file);
			String line;
			line = br.readLine();
			while (line != null) {
				tmp = line.split("\\s+");
				if (tmp.length >= 2) {
					isNode = Character.isDigit(tmp[1].charAt(0)); // returns true if we are reading a node, false if reading an edge
					if (isNode) {
						cityName = tmp[0];
						x = Double.parseDouble(tmp[1]);
						y = Double.parseDouble(tmp[2]);
						CityNode node = new CityNode(cityName, x, y); // creates CityNode
						addNode(node); // add node to the array of nodes

					}
					else {
						nodeId1 = hashTable.find(tmp[0]); // finds the nodeId for the city using the hash table
						nodeId2 = hashTable.find(tmp[1]);
						cost = Integer.parseInt(tmp[2]);

						edge1 = new Edge(nodeId1, cost);
						edge2 = new Edge(nodeId2, cost);

						addEdge(nodeId1, edge2);
						addEdge(nodeId2, edge1);
					}
				}
				line = br.readLine();
			}
		}
		catch (IOException e) {
			System.out.println("No such file");
			e.printStackTrace();
		}
	}

	/**
	 * Add a node to the array of nodes.
	 * Add a HashEntry to the HashTable
	 * Increment numNodes variable.
     * Called from loadGraph.
	 *
	 * @param node a CityNode to add to the graph
	 */
	public void addNode(CityNode node) {
		int nodeId; // stores the nodeId to create a HashEntry for the HashTable
		String cityName; // stores the city name stored in node

		if (numNodes == 0) {
			nodes = new CityNode[20];
			hashTable = new HashTable(20);
		}
		if (nodes.length <= numNodes) {
			resizeNodesGraph();
		}

		cityName = node.getCity();
		nodeId = numNodes;
		hashTable.insert(cityName, nodeId); // add the node to the hash table

	 	nodes[numNodes++] = node;
	}

	/**
	 * Increases the size of the nodes graph by a factor of 2.
	 * Used when the current nodes graph is full.
	 */
	public void resizeNodesGraph() {
		CityNode[] tmp = new CityNode[nodes.length];
		for(int i = 0; i < nodes.length; i++) {
			tmp[i] = nodes[i];
		}

		int size = numNodes * 2;
		nodes = new CityNode[size];
		for (int i = 0; i < tmp.length; i++) {
			nodes[i] = tmp[i];
		}
	}

	/**
	 * Prints the cities in the nodes graph
	 *
	 * @param nodes the array of city nodes
	 */
	public void printNodesGraph(CityNode[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			System.out.println(nodes[i].getCity());
		}
	}

	/**
	 * Return the number of nodes in the graph
	 * @return number of nodes
	 */
	public int numNodes() {
		return numNodes;
	}

	/**
	 * Adds the edge to the linked list for the given nodeId
	 * Called from loadGraph.
     *
	 * @param nodeId id of the node
	 * @param edge edge to add
	 */
	public void addEdge(int nodeId, Edge edge) {
		Edge vi; // the vertex vi in the adjacency list
		if (numEdges == 0) {
			adjacencyList = new Edge[20];
		}
		if (adjacencyList.length <= numEdges) {
			resizeAdjacencyList();
		}

		if (adjacencyList[nodeId] == null) {
			adjacencyList[nodeId] = new Edge(nodeId, 0);
		}

		vi = adjacencyList[nodeId];
		if (vi.next() == null) {
			vi.setNext(edge);
			numEdges++;
		} else {
			Edge tmp = vi.next();
			vi.setNext(edge);
			edge.setNext(tmp);
			numEdges++;
		}
	}

	/**
	 * Increases the size of the nodes graph by a factor of 2.
	 * Used when the current nodes graph is full.
	 */
	public void resizeAdjacencyList() {
		Edge[] tmp = new Edge[adjacencyList.length];
		for(int i = 0; i < nodes.length; i++) {
			tmp[i] = adjacencyList[i];
		}

		int size = numEdges * 2;
		adjacencyList = new Edge[size];
		for (int i = 0; i < tmp.length; i++) {
			adjacencyList[i] = tmp[i];
		}
	}

	/**
	 * Prints the adjacency list of edges
	 */
	public void printAdjecencyList() {
		Edge curr; // the current vertex vi in the adjacency list

		for(int i = 0; i < adjacencyList.length; i++) {
			curr = adjacencyList[i];
			if (curr != null) {
				System.out.print(i + ":  ");
				while (curr.next() != null) {
					curr.printEdge();
					curr = curr.next();
				}
				System.out.println();
			}
		}
	}

	/**
	 * Returns the adjacency list of type Edge
	 * @return adjacency list
	 */
	public Edge[] getAdjacencyList() {
		return adjacencyList;
	}

	/**
	 * Returns an integer id of the given city node
	 * @param city node of the graph
	 * @return its integer id
	 */
	public int getId(CityNode city) {
		String cityName = city.getCity();
		int nodeId = hashTable.find(cityName);
		if (nodeId == -1) {
			System.out.println("The city you requested is not in the graph.");
		}
        return nodeId;
    }

	/**
	 * Return the edges of the graph as a 2D array of points.
	 * Called from GUIApp to display the edges of the graph.
	 *
	 * @return a 2D array of Points.
	 * For each edge, we store an array of two Points, v1 and v2.
	 * v1 is the source vertex for this edge, v2 is the destination vertex.
	 * This info can be obtained from the adjacency list
	 */
	public Point[][] getEdges() {
		int i = 0;
		Point[][] edges2D = new Point[numEdges][2];

		Point v1;
		Point v2;
		for(int j = 0; j < nodes.length; j++) {
			int sourceNodeId = adjacencyList[j].getNeighbor();
			v1 = getPoint(sourceNodeId);
			Edge destinationEdge = adjacencyList[j].next();
			while (destinationEdge != null) {
				edges2D[i][0] = v1;
				int destinationNodeId = destinationEdge.getNeighbor();
				v2 = getPoint(destinationNodeId);
				edges2D[i][1] = v2;
				i++;
				destinationEdge = destinationEdge.next();
			}
		}
//		printEdges(edges2D);

		return edges2D;
	}

	/**
	 * Prints the 2D array of edges; each point represents the location of a vertex
	 * @param edgePoints the 2D array of Points between edges
	 */
	private void printEdges(Point[][] edgePoints) {
		for (int row = 0; row < edgePoints.length; row++) {
			System.out.print("Edge Points:  [Source: " + edgePoints[row][0] + ", Destination: " + edgePoints[row][1] + "]  ");
			System.out.println();
		}
	}

	/**
	 * Gets the location of a CityNode given a node id
	 * @param nodeId the CityNode's id
	 */
	public Point getPoint(int nodeId) {
		CityNode cityNode = nodes[nodeId];
		return cityNode.getLocation();
	}

	/**
	 * Get the nodes of the graph as a 1D array of Points.
	 * Used in GUIApp to display the nodes of the graph.
	 * @return a list of Points that correspond to nodes of the graph.
	 */
	public Point[] getNodes() {
	    if (this.nodes == null) {
            System.out.println("Graph has no nodes. Write loadGraph method first. ");
            return null;
        }
		Point[] pnodes = new Point[this.nodes.length];
		Point currPoint;
		for (int i = 0; i < pnodes.length; i++) {
			currPoint = nodes[i].getLocation();
			pnodes[i] = currPoint;
		}

		return pnodes;
	}

	/**
	 * Used in GUIApp to display the names of the airports.
	 * @return the list that contains the names of cities (that correspond
	 * to the nodes of the graph)
	 */
	public String[] getCities() {
        if (this.nodes == null) {
            System.out.println("Graph has no nodes. Write loadGraph method first. ");
            return null;
        }
		String[] labels = new String[nodes.length];
		String city;
		for (int i = 0; i < nodes.length; i++) {
			city = nodes[i].getCity();
			labels[i] = city;
		}
		return labels;

	}

	/**
	 * Print city names in the nodes array
	 */
	public void printCities() {
		for (int i = 0; i < nodes.length; i++) {
			System.out.print("[nodeId:" + i + ", " + nodes[i].getCity() + "] ");
		}
		System.out.println();
	}

	/** Take a list of node ids on the path and return an array where each
	 * element contains two points (an edge between two consecutive nodes)
	 * @param pathOfNodes A list of node ids on the path
	 * @return array where each element is an array of 2 points
	 */
	public Point[][] getPath(List<Integer> pathOfNodes) {
		int i;
		Point v;
		Point u;
		Point[][] edges2D = new Point[pathOfNodes.size() - 1][2];
        // Each "edge" is an array of size two (one Point is origin, one Point is destination)

		for (int j = 0; j < edges2D.length; j++) {
			v = getPoint(pathOfNodes.get(j));
			u = getPoint(pathOfNodes.get(j + 1));
			edges2D[j][0] = v;
			edges2D[j][1] = u;
		}

        return edges2D;
	}

	/** Take a list of node ids on prim's MST and return an array where each
	 * element contains two points (an edge between two consecutive nodes)
	 * @param minSpanningTree A list of node ids in the MST
	 * @return array where each element is an array of 2 points
	 */
	public Point[][] getPrimPath(List<PrimsMST.Edges> minSpanningTree) {
		PrimsMST.Edges edge;
		Point v;
		Point u;
		Point[][] edges2D = new Point[minSpanningTree.size() - 1][2];

		for (int j = 0; j < edges2D.length; j++) {
			edge = minSpanningTree.get(j);
			v = getPoint(edge.getNodeId1());
			u = getPoint(edge.getNodeId2());
			edges2D[j][0] = v;
			edges2D[j][1] = u;
		}

		return edges2D;
	}

	/**
	 * Return the CityNode for the given nodeId
	 * @param nodeId id of the node
	 * @return CityNode
	 */
	public CityNode getNode(int nodeId) {
		return nodes[nodeId];
	}

	/**
	 * Take the location of the mouse click as a parameter, and return the node
	 * of the graph at this location. Needed in GUIApp class.
	 * @param loc the location of the mouse click
	 * @return reference to the corresponding CityNode
	 */
	public CityNode getNode(Point loc) {
		for (CityNode v : nodes) {
			Point p = v.getLocation();
			if ((Math.abs(loc.x - p.x) < EPS_DIST) && (Math.abs(loc.y - p.y) < EPS_DIST))
				return v;
		}
		return null;
	}
}