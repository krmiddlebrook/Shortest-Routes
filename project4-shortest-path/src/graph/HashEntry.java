package graph;

public class HashEntry {
    private String key;
    private int nodeId;

    public HashEntry(String key, int nodeId) {
        this.key = key;
        this.nodeId = nodeId;
    }

    public String getKey() { return key; }

    public int getNodeId() { return nodeId; }

    public String toString() { return key + " " + nodeId + "; "; }

}
