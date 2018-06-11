package graph;

import java.util.LinkedList;
import java.util.Iterator;

/** Custom implementation of a hash table using open hashing, separate chaining.
 *  Each key is a String (name of the city), each value is an integer (node id). */
public class HashTable {
	 // FILL IN CODE: add variables and methods
    private LinkedList<HashEntry>[] table;
    private int tableSize;

    public HashTable(int arraySize) {
        this.tableSize = arraySize;  // remove this later: TODO: make prime number function
        this.table = new LinkedList[11]; // 11 is size for now, will change to be dynamic later

        // initialize table entries as null linked lists that store HashEntries
        for(int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<HashEntry>();
        }
    }


    /**
     * Maps a given string to an integer
     * @param key string we want to map to an integer
     * @return the compressed hash code for the given string
     */
    private int hash(String key) {
        int a = 33;
        int hashCode = 0;
        char c;
        int power = key.length() - 1;

        for(int i = 0; i < key.length(); i++) {
            c = key.charAt(i);
            hashCode = (int) c * (a^(power - i)) + hashCode;
        }
        hashCode = Math.abs(hashCode);
        return hashCode % table.length;
    }

    /**
     * Inserts a key, nodeId pair into the hash table
     * @param key string we want to add
     * @param nodeId integer id of the key we are inserting
     */
    public void insert(String key, int nodeId) {
        int hashvalue = hash(key);
        HashEntry entry = new HashEntry(key, nodeId);
        table[hashvalue].addFirst(entry);
    }

    /**
     * Finds the node id given the string (i.e. the string = the name of the city)
     * @param key the string whose node id you want to find
     * @return nodeId of the given string, returns -1 if string was not found.
     */
    public int find(String key) {
        int nodeId = -1;
        Iterator<HashEntry> iter;
        HashEntry curr;
        String currKey;
        int hashvalue = hash(key);

        iter = table[hashvalue].iterator();
        curr = iter.next();
        while (curr != null) {
            currKey = curr.getKey();
            if(currKey.equals(key)) {
                nodeId = curr.getNodeId();
                break;
            }
            curr = iter.next();
        }

        if(nodeId == -1) {
            System.out.println("The city you were searching for isn't in the table or it doesn't exist.");
        }

        return nodeId;
    }

    /**
     * Prints out our hash table. Each hash value will print on its own line.
     */
    public void print() {
        for(int i = 0; i < table.length; i++) {
            String nodes = "hash value: " + i + " nodes: ";
            Iterator<HashEntry> iter = table[i].iterator();

            while (iter.hasNext()) {
                String node = iter.next().toString();
                nodes = nodes.concat(node);
            }
            System.out.println(nodes);
        }
    }

}