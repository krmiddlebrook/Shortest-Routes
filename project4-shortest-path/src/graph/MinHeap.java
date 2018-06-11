package graph;

public class MinHeap {
    private Elem[] heap;
    private int size;
    private int[] pos;

    public MinHeap(int numNodes) {
        this.heap = new Elem[numNodes];
        this.size = numNodes;
        this.pos = new int[numNodes];
    }


    public void insert(int nodeId, int cost) {
        heap[nodeId] = new Elem(nodeId, cost);
        pos[nodeId] = nodeId;
    }

    public int removeMin() {
        if (isEmpty()) {
            System.out.println("Heap is empty.");
            return -1;
        }

        Elem root = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = root;

        pos[root.getNodeId()] = size - 1;
        pos[heap[0].getNodeId()] = 0;

        size--;
        pushdown(0);
        return root.getNodeId();
    }

    public void reduceKey(int nodeId, int newCost) {
        int idx = pos[nodeId];
        heap[idx].setCost(newCost);

        while ((idx > 0) && (heap[idx].getCost() < heap[parentIdx(idx)].getCost())) {
            pos[heap[idx].getNodeId()] = parentIdx(idx);
            pos[heap[parentIdx(idx)].getNodeId()] = idx;
            swapElems(idx, parentIdx(idx));

            idx = parentIdx(idx);
        }
    }

    public void pushdown(int index) {
        int smallest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if ((left < size) && (heap[left].getCost() < heap[smallest].getCost())) {
            smallest = left;
        }
        if ((right < size) && (heap[right].getCost() < heap[smallest].getCost())) {
            smallest = right;
        }

        if (smallest != index) {
            pos[heap[smallest].getNodeId()] = index;
            pos[heap[index].getNodeId()] = smallest;
            swapElems(smallest, index);
            pushdown(smallest);
        }

    }

    public boolean isEmpty() {
        if (size <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isInHeap(int vertex) {
        if (pos[vertex] < size) {
            return true;
        } else {
            return false;
        }
    }

    private int parentIdx(int childIdx) {
        return (childIdx - 1) / 2;
    }

    private void swapElems(int a, int b) {
        Elem tmp = heap[a];
        heap[a] = heap[b];
        heap[b] = tmp;
    }

    /**
     * Private inner class to store vertex id and cost to travel to vertex
     */
    private class Elem {
        private int nodeId;
        private int cost;

        public Elem(int nodeId, int cost) {
            this.nodeId = nodeId;
            this.cost = cost;
        }


        public void setNodeId(int nodeId) {
            this.nodeId = nodeId;
        }

        public int getNodeId() {
            return nodeId;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }
    }
}
