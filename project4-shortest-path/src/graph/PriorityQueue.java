package graph;

/** A priority queue: implemented using a min heap.
 *  You may not use any Java built-in classes, you should implement
 *  PriorityQueue yourself. You may use/modify the MinHeap code posted
 *  by the instructor under Examples, as long as you understand it. */
public class PriorityQueue {

	private HeapElem[] heap; // the array to store the heap
	private int maxsize; // the size of the array
	private int size; // the current number of elements in the array
	private int[] positions; // an array of pointers. maps nodeId to the index in the heap


	/**
	 * Constructor
	 * @param numNodes the largest nodeId to be inserted into the size of the heap
	 */
	public PriorityQueue(int numNodes) {
		maxsize = numNodes;
		heap = new HeapElem[maxsize + 1];
		size = 1;
		positions = new int[maxsize + 1];
		heap[0] = new HeapElem(-1, Integer.MIN_VALUE);
		// Note: no actual data is stored at heap[0].
		// Assigned MIN_VALUE so that it's easier to bubble up
		initializeHeap(); // builds the initial heap with nodeIds and priority values
	}

	/**
	 * Initialize the min heap with nodeIds up to the max value and priority value set to Integer.Max_Value
	 * Also, initializes the positions array
	 */
	private void initializeHeap() {
		for (int i = 1; i < heap.length; i++) {
			int nodeId = i - 1;
			heap[i] = new HeapElem(nodeId, Integer.MAX_VALUE);
			positions[nodeId] = i;
		}
	}

	/** Return the index of the left child of the element at index pos
	 *
	 * @param pos the index of the element in the heap array
	 * @return the index of the left child
	 */
	private int leftChild(int pos) {
		return 2 * pos;
	}

	/** Return the index of the right child
	 *
	 * @param pos the index of the element in the heap array
	 * @return the index of the right child
	 */
	private int rightChild(int pos) {
		return 2 * pos + 1;
	}

	/** Return the index of the parent
	 *
	 * @param pos the index of the element in the heap array
	 * @return the index of the parent
	 */
	private int parent(int pos) {
		return pos / 2;
	}

	/** Returns true if the node in a given position is a leaf
	 *
	 * @param pos the index of the element in the heap array
	 * @return true if the node is a leaf, false otherwise
	 */
	private boolean isLeaf(int pos) {
		return ((pos > maxsize / 2) && (pos <= maxsize));
	}

	/** Swap given heap elements: one at heapIndex_1, another at heapIndex_2
	 * as well as the position values in the positions array
	 *
	 * @param heapIndex_1 the heap index of the first element in the heap
	 * @param heapIndex_2 the heap index of the second element in the heap
	 */
	private void swap(int heapIndex_1, int heapIndex_2) {
		swapPositions(heapIndex_1, heapIndex_2); // swap the heap elements positions in the positions array

		// swap the heap elements in the heap array
		HeapElem tmp;
		tmp = heap[heapIndex_1];
		heap[heapIndex_1] = heap[heapIndex_2];
		heap[heapIndex_2] = tmp;
	}

	/**
	 * Swaps the given heap indexes' positions in the positions array
	 *
	 * @param heapIndex_1 the heap index of the first element in the heap
	 * @param heapIndex_2 the heap index of the second element in the heap
	 */
	private void swapPositions(int heapIndex_1, int heapIndex_2) {
		int nodeId_1 = heap[heapIndex_1].nodeId;
		int nodeId_2 = heap[heapIndex_2].nodeId;

		int tmp = positions[nodeId_1];
		positions[nodeId_1] = positions[nodeId_2];
		positions[nodeId_2] = tmp;
	}

	/** Insert a new element (nodeId, priority) into the heap.
     *  For this project, the priority is the current "distance"
     *  for this nodeId in Dikstra's algorithm. */
	public void insert(int nodeId, int priority) {
		if (heap[getHeapIndex(nodeId)].priority >= priority) {
			reduceKey(nodeId, priority);
		}
	}

	/**
	 * Get the nodeId of the heap element at a given heap index
	 * @param heapIndex the heap index
	 * @return the nodeId at the heapIndex
	 */
	public int getNodeIdAtHeapIndex(int heapIndex) {
		return heap[heapIndex].nodeId;
	}

    /**
     * Remove the element with the minimum priority
     * from the min heap and return its nodeId.
     * @return nodeId of the element with the smallest priority
     */
	public int removeMin() {
		HeapElem minElem; // stores the values associated with the removed element
		minElem = heap[1];

		for (int i = 2; i < heap.length; i++) {
			swap(i - 1, i);
		}
		maxsize--; // removed the end of the heap
		size--; // removed element is no longer included in the size of the heap

		return minElem.nodeId; // return the nodeId of the removed heap element
	}

    /**
     * Reduce the priority of the element with the given nodeId to newPriority.
     * You may assume newPriority is less or equal to the current priority for this node.
     * @param nodeId id of the node
     * @param newPriority new value of priority
     */
	public void reduceKey(int nodeId, int newPriority) {
		size++;
		int heapIndex = getHeapIndex(nodeId);
		heap[heapIndex].setPriority(newPriority);

		// fix heap by bubbling up
		int currHeapIndex = heapIndex;
		int previousHeapIndex = heapIndex -1;
		while ((previousHeapIndex >= 0) && (heap[currHeapIndex].priority < heap[previousHeapIndex].priority)) {
			swap(currHeapIndex, previousHeapIndex); // swap the element at heapIndex with the element at the previousHeapIndex
			currHeapIndex = previousHeapIndex;
			previousHeapIndex = previousHeapIndex - 1;
		}
	}

	/**
	 * Get the index of the elem with the given nodeId
	 *
	 * @param nodeId the id of the elem you want
	 * @return the position of the elem in the heap
	 */
	public int getHeapIndex(int nodeId) {
		return positions[nodeId];
	}

	/**
	 * Returns the max size of the priority queue
	 * @return maxSize the max size of the heap
	 */
	public int getMaxsize() {
		return maxsize;
	}

	public int getSize() { return size; }

	/**
	 * Checks if the heap is empty
	 * @return true if size is 0, false otherwise
	 */
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	/** Print the array that stores the heap */
	public void printHeap() {
		int i;
		for (i = 1; i < heap.length; i++)
			System.out.print("[" + heap[i].nodeId + ", " + heap[i].priority + "] ");
		System.out.println();
	}






	/**
	 * Private inner class to represent an element in the heap
	 */
	private class HeapElem {
		private int nodeId; // stores the city's nodeId
		private int priority; // stores the cost of traveling to the city

		public HeapElem(int nodeId, int priority) {
			this.nodeId = nodeId;
			this.priority = priority;
		}

		public int getNodeId() { return this.nodeId; }

		public int getPriority() { return this.priority; }

		public void setNodeId(int nodeId) {
			this.nodeId = nodeId;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}
	}
}

