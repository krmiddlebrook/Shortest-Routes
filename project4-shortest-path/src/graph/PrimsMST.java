package graph;

/** Class PrimsMST. Implementation of Prims
 *  algorithm on the graph for finding the shortest path; uses
 *  a minimum spanning tree.
 */

import java.util.*;
import java.awt.Point;

public class PrimsMST {

    private Vertex[] mstTree; // stores the MST table
    private Graph graph; // stores the graph of CityNode-s and edges connecting them
    private List<Edges> minSpanningTree = null; // edges that are in the minimum spanning tree
    private MinHeap minHeap; // stores the min heap
    private int numNodes; // stores the number of nodes in the graph
    private Edge[] adjacencyList; // adjacency list; for each vertex stores a linked list of edges


    /**
     * Constructor
     *
     * @param filename name of the file that contains info about nodes and edges
     * @param graph    graph
     */
    public PrimsMST(String filename, Graph graph) {
        this.graph = graph;
        graph.loadGraph(filename);
        numNodes = graph.numNodes();
        adjacencyList = graph.getAdjacencyList();
        mstTree = new Vertex[numNodes];
        minHeap = new MinHeap(numNodes);
        minSpanningTree = new ArrayList<Edges>();
    }

    /**
     * Returns the shortest path between the origin vertex and the destination vertex.
     * The result is stored in shortestPathEdges.
     * This function is called from GUIApp, when the user clicks on two cities.
     *
     * @return the ArrayList of nodeIds (of nodes on the shortest path)
     */
    public List<Edges> computeShortestPath() {

        // Run Prim's
        prims(); // creates the table for Dijkstra's algorithm


        // The result should be in an instance variable called "minSpanningTree" and
        return minSpanningTree;
    }

    /**
     * Runs Prims algorithm to find the MST
     */
    public void prims() {
        int v;
        Edge u;
        minHeap = new MinHeap(numNodes);

        // initialize the heap with all vertices
        // and set the cost of traveling to vertex v to Inf
        // and set the path of traveling to the given vertex to -1
        for (v = 0; v < numNodes; v++) {
            mstTree[v] = new Vertex(v, Double.POSITIVE_INFINITY, -1, false);
            minHeap.insert(v, Integer.MAX_VALUE);
        }
        mstTree[0].setCost(0);
        minHeap.reduceKey(0, (int) mstTree[0].getCost());

        // run prim's
        while (!minHeap.isEmpty()) {
            v = minHeap.removeMin();

            for (u = adjacencyList[v].next(); u != null; u = u.next()) {
                if ((minHeap.isInHeap(u.getNeighbor())) && (u.getCost() < mstTree[u.getNeighbor()].getCost())) {
                    mstTree[u.getNeighbor()].setCost(u.getCost());
                    mstTree[u.getNeighbor()].setParent(v);
                    minHeap.reduceKey(u.getNeighbor(), (int) mstTree[u.getNeighbor()].getCost());
                }
            }
        }
//        printMST();
        buildMST();
    }

    /**
     * A utility function to print prim's MST
     */
    private void printMST() {
        int v;
        for (v = 1; v < numNodes; v++) {
            System.out.println(mstTree[v].getParent() + " ---> " + mstTree[v].getNodeId());
        }
    }

    private void buildMST() {
        int v;
        for (v = 1; v < numNodes; v++) {
            minSpanningTree.add(new Edges(mstTree[v].getParent(), mstTree[v].getNodeId()));
        }
    }

    /**
     * Return the shortest path as a 2D array of Points.
     * Each element in the array is another array that has 2 Points:
     * these two points define the beginning and end of a line segment.
     * @return 2D array of points
     */
    public Point[][] getPath() {
        if (minSpanningTree == null)
            return null;
        return graph.getPrimPath(minSpanningTree); // delegating this task to the Graph class
    }

    /** Set the shortestPath to null.
     *  Called when the user presses Reset button.
     */
    public void resetPath() {
        minSpanningTree = null;
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

    /**
     * Private inner class to represent edges in the MST.
     * Each edges object stores a vertex v's nodeId and a vertex u's nodeId.
     * Vertex v is connected to vertex u
     */
    public class Edges {
        private int nodeId1;
        private int nodeId2;

        public Edges(int id1, int id2) {
            this.nodeId1 = id1;
            this.nodeId2 = id2;
        }

        // getters
        public int getNodeId1() {
            return nodeId1;
        }

        public int getNodeId2() {
            return nodeId2;
        }


        // setters
        public void setNodeId1(int nodeId1) {
            this.nodeId1 = nodeId1;
        }

        public void setNodeId2(int nodeId2) {
            this.nodeId2 = nodeId2;
        }
    }

}
