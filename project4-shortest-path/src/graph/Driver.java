package graph;

import java.util.ArrayList;
import java.util.List;

/** The Driver class for project Dijkstra */
public class Driver {
    public static void main(String[] args) {
        // Initialize a graph
        Graph graph = new Graph(); // graph for dijkstra's
//        Graph primGraph = new Graph(); // graph for prim's

        // Create an instance of the Dijkstra class
        Dijkstra dijkstra = new Dijkstra("USA.txt", graph); // runs dijkstra's to compute the minimum spanning tree

//        PrimsMST prims = new PrimsMST("USA.txt", primGraph); // runs prim's algorithm to find the minimum spanning tree

        // Create a graphical user interface and wait for user to click on two cities:
        GUIApp app = new GUIApp(dijkstra, graph); // test dijkstra's
//        PrimGUIApp primApp = new PrimGUIApp(prims, primGraph); // test prim's



    }
}
