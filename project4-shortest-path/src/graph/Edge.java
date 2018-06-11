package graph;

/** Edge class represents a link in the linked list of edges for a vertex.
 *  Each Edge stores the id of the "neighbor" (the vertex where this edge is going =
 *  "destination" vertex), the cost and the reference to the next Edge.
 */
class Edge {
    private int neighbor; // id of the neighbor ("destination" vertex of this edge)
	private int cost; // cost of this edge
	private Edge next; // reference to the next "edge" in the linked list


    public Edge(int neighbor, int cost) {
        this.neighbor = neighbor;
        this.cost = cost;
        this.next = null;
    }

    /**
     * Get the next "edge" in the linked list
     * @return reference to the next edge
     */
    public Edge next() { return next; }

    /**
     * Set the next "edge" in the linked list
     * @param nextEdge the next edge you want in the linked list
     */
    public void setNext(Edge nextEdge) { this.next = nextEdge; }

    /**
     * Get the id of the neighbor ("destination" vertex of this edge)
     * @return id of the neighbor
     */
    public int getNeighbor() { return neighbor; }


    /**
     * Set the id of the neighbor
     * @param neighborId the new neighbor id
     */
    public void setNeighbor(int neighborId) { this.neighbor = neighborId; }

    /**
     * Get the cost of this edge
     * @return the cost of this edge
     */
    public int getCost() { return cost; }

    /**
     * Set the cost of this edge
     * @param edgeCost the new edge cost
     */
    public void setCost(int edgeCost) { this.cost = edgeCost; }


    /**
     * To string method to display the contents of the edge
     */
    public void printEdge() {
        System.out.print("[" + neighbor + ", " + cost + "] --> ");
    }



 }