package graph;

/** Class Dijkstra. Implementation of Dijkstra's
 *  algorithm on the graph for finding the shortest path.
 *  Fill in code. You may add additional helper methods or classes.
 */

import java.util.*;
import java.awt.Point;

public class Dijkstra {
	private Graph graph; // stores the graph of CityNode-s and edges connecting them
    private List<Integer> shortestPath = null; // nodes that are part of the shortest path
	private Vertex[] mst; // the min spanning tree
    private int numNodes; // stores the number of nodes in the graph
    private Edge[] adjacencyList; // adjacency list; for each vertex stores a linked list of edges

    private MinHeap minHeap; // for testDijkstra method


    /** Constructor
	 *
	 * @param filename name of the file that contains info about nodes and edges
     * @param graph graph
	 */
	public Dijkstra(String filename, Graph graph) {
	    this.graph = graph;
		graph.loadGraph(filename);
        this.numNodes = graph.numNodes();
		this.adjacencyList = graph.getAdjacencyList();
		this.mst = new Vertex[numNodes];
	}

	/**
	 * Returns the shortest path between the origin vertex and the destination vertex.
	 * The result is stored in shortestPathEdges.
	 * This function is called from GUIApp, when the user clicks on two cities.
	 * @param origin source node
	 * @param destination destination node
     * @return the ArrayList of nodeIds (of nodes on the shortest path)
	 */
	public List<Integer> computeShortestPath(CityNode origin, CityNode destination) {
        Stack<Integer> stackForShortestPath; // holds node id's on the shortest path

        // Run Dijkstra's
        dijkstra(origin);

        // Compute the nodes on the shortest path by "backtracking" using the table
        stackForShortestPath = buildStackForShortestPath(origin, destination); // add node ids on the shortest path to stack
        buildShortestPath(stackForShortestPath); // add node ids from the stack to the shortestPath list in the correct order

	    return shortestPath;
    }

    private void dijkstra(CityNode origin) {
	    int v;
	    Edge u;
	    minHeap = new MinHeap(numNodes);

	    // initialize the heap with all vertices
        // and set the cost of traveling to vertex v to Inf
        // and set the path of traveling to the given vertex to -1
        for (v = 0; v < numNodes; v++) {
            mst[v] = new Vertex(v, Double.POSITIVE_INFINITY, -1, false);
            minHeap.insert(v, Integer.MAX_VALUE);
        }
        mst[graph.getId(origin)].setCost(0);
        minHeap.reduceKey(graph.getId(origin), (int) mst[graph.getId(origin)].getCost());

        // run dijkstra's
        while (!minHeap.isEmpty()) {
            v = minHeap.removeMin();

            for (u = adjacencyList[v].next(); u != null; u = u.next()) {
                if ((minHeap.isInHeap(u.getNeighbor())) && (u.getCost() + mst[v].getCost() < mst[u.getNeighbor()].getCost())) {
                    mst[u.getNeighbor()].setCost(u.getCost() + mst[v].getCost());
                    mst[u.getNeighbor()].setParent(v);
                    minHeap.reduceKey(u.getNeighbor(), (int) mst[u.getNeighbor()].getCost());
                }
            }
        }
    }


    /**
     * Creates a stack to store the nodes on the shortest path
     * @param origin the origin CityNode
     * @param destination the destination CityNode
     * @return the shortest path from origin to destination
     */
    private Stack<Integer> buildStackForShortestPath(CityNode origin, CityNode destination) {
        // Compute the nodes on the shortest path by "backtracking" using the table
        int originId = getNodeId(origin);
        int destId = getNodeId(destination);

        Stack<Integer> stackShortestPath = new Stack<>(); // stores the nodeIds on the shortest path to the destination vertex
        int vId = destId;
        while ((vId != originId)) {
            stackShortestPath.push(vId);
            vId = mst[vId].getParent();
        }
        stackShortestPath.push(vId); // add the origin id last
        return stackShortestPath;
    }

    /**
     * Removes node id's from the stack for the shortest path
     * @param stackShortestPath the stack with node id's on the shortest path
     */
    private void buildShortestPath(Stack<Integer> stackShortestPath) {
        shortestPath = new ArrayList<>();
        while (!stackShortestPath.empty()) {
            int nodeId = stackShortestPath.pop();
            shortestPath.add(nodeId);
        }
    }


    /**
     * Print the cost array
     */
    public void printCost() {
        for (int i = 0; i < mst.length; i++) {
            System.out.print("[" + i + ", " + mst[i].getCost() + "], ");
        }
        System.out.println();
    }

    /**
     * Print the path array
     */
    public void printPath() {
        for (int i = 0; i < mst.length; i++) {
            System.out.print("[" + i + ", " + mst[i].getParent() + "], ");
        }
        System.out.println();
    }

    /**
     * Gets the nodeId of a given CityNode
     * @param cityNode the CityNode whose nodeId we want
     * @return the nodeId of the cityNode
     */
    private int getNodeId(CityNode cityNode) {
        return graph.getId(cityNode);
    }

    /**
     * Return the shortest path as a 2D array of Points.
     * Each element in the array is another array that has 2 Points:
     * these two points define the beginning and end of a line segment.
     * @return 2D array of points
     */
    public Point[][] getPath() {
        if (shortestPath == null)
            return null;
        return graph.getPath(shortestPath); // delegating this task to the Graph class
    }

    /** Set the shortestPath to null.
     *  Called when the user presses Reset button.
     */
    public void resetPath() {
        shortestPath = null;
    }

    /**
     * Private inner class to represent vertices.
     * Each vertex object stores a vertex v's nodeId, the cost to travel to v,
     * and the
     */
    private class Vertex {
        private int nodeId; // the node id for this vertex
        private double cost; // the cost to add vertex Vk to the tree
        private int parent; // the lowest cost path to this vertex Vk
        private boolean known; // whether or not this vertex has been added to the tree

        public Vertex(int nodeId, double cost, int parent, boolean known) {
            this.nodeId = nodeId;
            this.cost = cost;
            this.parent = parent;
            this.known = known;
        }

        public int getNodeId() { return this.nodeId; }
        public void setNodeId(int nodeId) { this.nodeId = nodeId; }

        public double getCost() { return this.cost; }
        public void setCost(double cost) {
            this.cost = cost;
        }

        public int getParent() { return this.parent; }
        public void setParent(int parent) { this.parent = parent; }

        public boolean isKnown() { return known; }
        public void setKnown(boolean known) { this.known = known; }
    }

}