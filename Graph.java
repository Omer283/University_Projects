

import java.util.Random;

/**
 * This class represents a graph that efficiently maintains the heaviest neighborhood over edge addition and
 * vertex deletion.
 *
 */
public class Graph {
    HashTable table;
    int numNodes, numEdges;
    MaxHeap<UpdatableNode> heap;

    /**
     * Initializes the graph on a given set of nodes. The created graph is empty, i.e. it has no edges.
     * You may assume that the ids of distinct nodes are distinct.
     *
     * @param nodes - an array of node objects
     */
    public Graph(Node [] nodes){
        this.numNodes = nodes.length;
        this.numEdges = 0;
        this.table = new HashTable(this.numNodes);
        HeapNode<UpdatableNode>[] heapNodes = new HeapNode[this.numNodes];
        for (int i = 0; i<this.numNodes; i++) {
            this.table.insert(nodes[i]);
            heapNodes[i] = new HeapNode<UpdatableNode>(nodes[i].getNeighborWeight(), new UpdatableNode(nodes[i]));
        }
        this.heap = new MaxHeap<UpdatableNode>(numNodes, heapNodes);
    }

    /**
     * This method returns the node in the graph with the maximum neighborhood weight.
     * Note: nodes that have been removed from the graph using deleteNode are no longer in the graph.
     * @return a Node object representing the correct node. If there is no node in the graph, returns 'null'.
     */
    public Node maxNeighborhoodWeight(){
        return this.numNodes == 0 ? null : this.heap.max().value.original;
    }

    /**
     * given a node id of a node in the graph, this method returns the neighborhood weight of that node.
     *
     * @param node_id - an id of a node.
     * @return the neighborhood weight of the node of id 'node_id' if such a node exists in the graph.
     * Otherwise, the function returns -1.
     */
    public int getNeighborhoodWeight(int node_id){
        Node node = table.get(node_id);
        if (node == null) {
            return -1;
        }
        else {
            return (int)node.getNeighborWeight();
        }
    }

    /**
     * This function adds an edge between the two nodes whose ids are specified.
     * If one of these nodes is not in the graph, the function does nothing.
     * The two nodes must be distinct; otherwise, the function does nothing.
     * You may assume that if the two nodes are in the graph, there exists no edge between them prior to the call.
     *
     * @param node1_id - the id of the first node.
     * @param node2_id - the id of the second node.
     * @return returns 'true' if the function added an edge, otherwise returns 'false'.
     */
    public boolean addEdge(int node1_id, int node2_id){
        Node n1 = table.get(node1_id), n2 = table.get(node2_id);
        if (n1 == null || n2 == null) {
            return false;
        }

        // update neighborhood weights locally:
        n1.setNeighborWeight(n1.getNeighborWeight() + n2.getWeight());
        n2.setNeighborWeight(n2.getNeighborWeight() + n1.getWeight());

        // update neighborhood weights in the heap:
        this.heap.increaseKey(this.heap.getData()[n1.heapIndex], n2.getWeight());
        this.heap.increaseKey(this.heap.getData()[n2.heapIndex], n1.getWeight());

        // insert the given nodes to the proper adjacency lists:
        AdjInfo n1_info = new AdjInfo(node1_id, null, n2);
        AdjInfo n2_info = new AdjInfo(node2_id, null, n1);
        MyNode<AdjInfo> n2_listnode = n1.adjList.insert(n2_info);
        MyNode<AdjInfo> n1_listnode = n2.adjList.insert(n1_info);
        n1_listnode.getVal().ptr = n2_listnode;
        n2_listnode.getVal().ptr = n1_listnode;

        this.numEdges++;
        return true;
    }

    /**
     * Given the id of a node in the graph, deletes the node of that id from the graph, if it exists.
     *
     * @param node_id - the id of the node to delete.
     * @return returns 'true' if the function deleted a node, otherwise returns 'false'
     */
    public boolean deleteNode(int node_id){
        Node node = this.table.get(node_id);
        if (node == null) {
            return false;
        }

        // delete from heap:
        this.heap.delete(this.heap.getData()[node.heapIndex]);

        // iterate over the node's adjacency list:
        MyNode<AdjInfo> curListNode = node.adjList.head;
        while (curListNode != null) {
            MyNode<AdjInfo> neighborListNode = curListNode.getVal().ptr;
            Node neighborNode = neighborListNode.getVal().srcNode;

            // decrease the node's weight from the neighbor's neighborhood weight locally and in the heap:
            neighborNode.setNeighborWeight(neighborNode.getNeighborWeight() - node.getWeight());
            this.heap.decreaseKey(heap.getData()[neighborNode.heapIndex], node.getWeight());

            // delete node from neighbor's adjacency list:
            neighborNode.adjList.deleteNode(neighborListNode);

            curListNode = curListNode.getNext();

            this.numEdges--;
        }

        // delete node from hash table:
        MyList<Node> list = this.table.table[this.table.getHash(node_id)];
        list.delete(node);

        this.numNodes--;
        return true;
    }

    /**
     * Returns the number of nodes currently in the graph.
     * @return the number of nodes in the graph.
     */
    public int getNumNodes(){
        return numNodes;
    }

    /**
     * Returns the number of edges currently in the graph.
     * @return the number of edges currently in the graph.
     */
    public int getNumEdges(){
        return numEdges;
    }


    /**
     * This class represents a node in the graph.
     */
    public static class Node{
        private int id, weight, heapIndex;
        private long neighborWeight;
        private MyList<AdjInfo> adjList;

        /**
         * Creates a new node object, given its id and its weight.
         * @param id - the id of the node.
         * @param weight - the weight of the node.
         */
        public Node(int id, int weight){
            this.id = id;
            this.weight = weight;
            this.neighborWeight = weight;
            adjList = new MyList<>();
        }

        /**
         * Returns the id of the node.
         * @return the id of the node.
         */
        public int getId(){
            return id;
        }

        /**
         * Returns the weight of the node.
         * @return the weight of the node.
         */
        public int getWeight(){
            return weight;
        }

        public long getNeighborWeight() {
            return neighborWeight;
        }

        public void setNeighborWeight(long neighborWeight) {
            this.neighborWeight = neighborWeight;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Node) {
                Node tmp = (Node)other;
                return tmp.id == this.id;
            }
            return false;
        }

    }

    /**
     * class Updatable-Node
     * used to wrap the class Node with an Updatable class that MaxHeap can handle as it's value's type.
     */
    public static class UpdatableNode implements Updatable
    {
        Node original;

        /**
         * Creates a new updatable node object, given an original node.
         *
         * @param node     - the original node.
         */
        public UpdatableNode(Node node) {
            this.original = node;
        }

        public void update(int index) {
            this.original.heapIndex = index;
        }
    }




    /**
     * Interface Updatable
     */
    public interface Updatable {
        void update(int arg);
    }




    /**
     * This class holds a doubly linked list, with values of class T (generic)
     * @param <T> generic class of values
     */
    public static class MyList<T> {
        int size = 0;
        MyNode<T> head = null, last = null; //first and last nodes

        /**
         * Inserts new node with value val to the end of the list
         * @param val the value of the node
         * @return the inserted node
         */
        public MyNode<T> insert(T val) {
            MyNode<T> node = new MyNode<>(val);
            if (size == 0) {
                head = last = node;
            }
            else {
                node.setPrev(last);
                last.setNext(node);
                this.last = node;
            }
            size++;
            return node;
        }

        /**
         * Deletes first instance with value val
         * @param val the value to search for
         * @return whether found or not
         */
        public boolean delete(T val) {
            MyNode<T> cur = head;
            while (cur != null) { //easy traversal!!
                if (cur.getVal().equals(val)) {
                    deleteNode(cur);
                    return true;
                }
                cur = cur.getNext();
            }
            return false;
        }


        /**
         * deletes node using pointer to node
         * @param node the node to be deleted
         */
        public void deleteNode(MyNode<T> node) {
            if (node != null) {
                size--;
                MyNode<T> prv = node.getPrev(), nxt = node.getNext();
                if (prv != null) {
                    prv.setNext(nxt);
                }
                if (nxt != null) {
                    nxt.setPrev(prv);
                }
                if (node == this.head) { //address
                    this.head = nxt;
                }
                if (node == this.last) { //address
                    this.last = prv;
                }
            }
        }
    }


    /**
     * The class represents a linked list node with value of generic type T
     * @param <T> the generic type
     */
    static class MyNode<T>{
        T val;
        MyNode<T> prev, next;

        /**
         * The default constructor
         * @param val the value of the new node
         */
        public MyNode(T val) {
            this.val = val;
        }

        /**
         * Gets next
         * @return next node
         */
        public MyNode<T> getNext() {
            return next;
        }

        /**
         * Gets prev
         * @return prev node
         */
        public MyNode<T> getPrev() {
            return prev;
        }

        /**
         * Sets next
         * @param next new next
         */
        public void setNext(MyNode<T> next) {
            this.next = next;
        }

        /**
         * Sets prev
         * @param prev new prev
         */
        public void setPrev(MyNode<T> prev) {
            this.prev = prev;
        }

        /**
         * Gets value
         * @return value
         * */
        public T getVal() {
            return val;
        }

    }




    /**
     * class MaxHeap
     * @param <V> is the type of the values in the Heap's nodes.
     */
    public class MaxHeap<V extends Updatable> {

        private final int MAX_SIZE;
        private final HeapNode<V>[] data;
        private int size;

        // NOT NECESSARY AND NOT DOCUMENTED EITHER:
//    public MaxHeap(int maxSize)
//    {
//        this.MAX_SIZE = maxSize;
//        this.data = new HeapNode[this.MAX_SIZE];
//        this.size = 0;
//    }

        /**
         * Initializes Heap from an unsorted array, as we saw in class.
         * Complexity: O(n)
         */
        public MaxHeap(int maxSize, HeapNode<V>[] arr)
        {
            // doesn't happen in our usage in the Graph of the class MaxHeap:
            if(maxSize < arr.length)
            {
                throw new RuntimeException();
            }

            this.MAX_SIZE = maxSize;
            this.data = new HeapNode[this.MAX_SIZE];
            for(int i=0;i<arr.length;i++)
            {
                this.data[i] = arr[i];
                this.data[i].pos = i;
                this.data[i].value.update(i); // update after position change is necessary.
            }
            this.size = arr.length;
            for(int index=this.size-1;index>=0;index--)
            {
                HeapifyDown(index); // this is the algorithm learned in class.
            }
        }

        public HeapNode<V>[] getData()
        {
            return this.data;
        }

        // NOT NECESSARY AND NOT DOCUMENTED EITHER:
//    public void insert(HeapNode<V> x)
//    {
//        if(this.size == this.MAX_SIZE) { return; }
//        this.size++;
//        this.data[this.size-1] = x;
//        x.pos = this.size-1;
//        x.value.update(x.pos);
//        this.HeapifyUp(this.size-1);
//    }

        public HeapNode<V> max()
        {
            return this.size == 0 ? null : this.data[0];
        }

        // NOT NECESSARY AND NOT DOCUMENTED EITHER:
//    public void deleteMax()
//    {
//        this.data[0] = this.data[this.size-1];
//        this.data[0].pos = 0;
//        this.data[0].value.update(0);
//        this.data[this.size-1] = null;
//        this.size--;
//        this.HeapifyDown(0);
//    }

        /**
         * Changes accordingly the key of the given node, and use Heapify to fix it
         * Complexity: O(log(n)) [=depth]
         */
        public void increaseKey(HeapNode<V> x, long delta)
        {
            if(delta < 0)
            {
                throw new RuntimeException();
            }
            x.key += delta;
            this.HeapifyUp(x.pos);
        }

        /**
         * Changes accordingly the key of the given node, and use Heapify to fix it
         * Complexity: O(log(n)) [=depth]
         */
        public void decreaseKey(HeapNode<V> x, long delta)
        {
            if(delta < 0)
            {
                throw new RuntimeException();
            }
            x.key -= delta;
            this.HeapifyDown(x.pos);
        }

        /**
         * Replaces the given node with the last node, and use Heapify to fix it.
         * Complexity: O(log(n)) [=depth]
         */
        public void delete(HeapNode<V> x)
        {
            int delPos = x.pos;
            this.data[delPos] = this.data[this.size-1];
            this.data[delPos].pos = delPos;
            this.data[delPos].value.update(delPos); // update after position change is necessary.
            this.data[this.size-1] = null;
            this.size--;
            // if delPos was the last node, then we shouldn't do more work
            if(delPos != this.size) {
                // Sometimes need Up and sometimes Down, anyway, these lines will
                // do it as the redundant one will run in O(1) and will do nothing.
                this.HeapifyUp(delPos);
                this.HeapifyDown(delPos);
            }
        }

        /**
         * fixes the validity of the heap assuming the only problem
         * is with the given index and upward to the root.
         * Complexity: O(log(n)) [=depth]
         */
        private void HeapifyUp(int index)
        {
            while(index > 0 && this.data[index].key > this.data[parentOf(index)].key)
            {
                swapNodes(index, parentOf(index));
                index = parentOf(index);
            }
        }

        /**
         * fixes the validity of the heap assuming the only problem
         * is with the given index and downward to a leaf.
         * Complexity: O(log(n)) [=depth]
         */
        private void HeapifyDown(int index)
        {
            int biggest = index;
            boolean finish = false;
            while(!finish)
            {
                int left = leftOf(index);
                int right = rightOf(index);
                if(left < this.size && this.data[left].key > this.data[biggest].key)
                {
                    biggest = left;
                }
                if(right < this.size && this.data[right].key > this.data[biggest].key)
                {
                    biggest = right;
                }
                finish = biggest == index;
                if(!finish)
                {
                    swapNodes(index, biggest);
                    index = biggest;
                }
            }
        }

        /**
         * swaps two nodes.
         * Complexity: O(1)
         */
        private void swapNodes(int index1, int index2)
        {
            HeapNode<V> tmp = this.data[index1];
            this.data[index1] = this.data[index2];
            this.data[index2] = tmp;
            this.data[index1].pos = index1;
            this.data[index2].pos = index2;
            this.data[index1].value.update(index1); // update after position change is necessary.
            this.data[index2].value.update(index2); // update after position change is necessary.
        }


        // THE FOLLOWING 3 METHODS ARE STATIC METHODS USED IN
        // ORDER TO NAVIGATE IN THE ARRAY FORM OF THE HEAP:

        private int leftOf(int i)
        {
            return 2*(i+1)-1;
        }

        private int rightOf(int i)
        {
            return 2*(i+1);
        }

        private int parentOf(int i)
        {
            return (i+1)/2-1;
        }
    }


    /**
     * class Heap-Node:
     * Simply A node of a heap.
     */
    public static class HeapNode<V>{
        public long key;
        public V value;
        public int pos;

        public HeapNode(long key, V value)
        {
            this.key = key;
            this.value = value;
            this.pos = 0;
        }

        // NOT NECESSARY AND NOT DOCUMENTED EITHER:
//        public HeapNode(long key, V value, int pos)
//        {
//            this.key = key;
//            this.value = value;
//            this.pos = pos;
//        }

    }





    /**
     * This class holds a hash table, mapping integers to Graph.Node
     */
    public class HashTable {
        final int p = (int)1e9 + 9;
        final double coeff = 10.0; //hash table size is coeff * N
        int tableSize = 0, tableElements = 0;
        MyList<Graph.Node>[] table;
        long a, b;

        /**
         * Constructor
         * @param N is the expected allocated cells
         */
        public HashTable(int N) {
            tableSize = (int)(coeff * N);
            table = new MyList[tableSize];
            Random rnd = new Random();
            a = rnd.nextInt(p); //randomizing parameters
            b = rnd.nextInt(p);
            tableElements = 0;
        }

        /**
         * Computes hash value
         * @param x input
         * @return (ax+b)%p%m
         */
        public int getHash(int x) {
            return (int)(((a * x + b) % p) % tableSize);
        }

        /**
         * Inserts Graph.Node into hash table
         * @param node the Graph.Node
         */
        public void insert(Graph.Node node) {
            int hsh = getHash(node.getId());
            if (table[hsh] == null) {
                table[hsh] = new MyList<>();
            }
            table[hsh].insert(node); //linked list method
        }


        /**
         * searches for Graph.Node with given id
         * @param id the id
         * @return the Graph.Node or null if doesn't exist
         */
        public Graph.Node get(int id) {
            int hsh = getHash(id);
            if (table[hsh] == null) { //no Graph.Node in that hash value
                return null;
            }
            MyNode<Graph.Node> cur = table[hsh].head;
            while (cur != null) { //searches in chain (hashing is by chaining)
                if (cur.getVal().getId() == id) {
                    return cur.getVal(); //found, returns
                }
                cur = cur.getNext();
            }
            return null; //didn't find in list
        }
    }




    /**
     * Class AdjInfo (Adjacency Information):
     */
    public class AdjInfo {
        Graph.Node srcNode;
        int id;
        MyNode<AdjInfo> ptr;

        public AdjInfo(int id, MyNode<AdjInfo> ptr, Graph.Node src) {
            this.id = id;
            this.ptr = ptr;
            this.srcNode = src;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof AdjInfo) {
                AdjInfo tmp = (AdjInfo)other;
                return tmp.id == this.id;
            }
            return false;
        }
    }

}
