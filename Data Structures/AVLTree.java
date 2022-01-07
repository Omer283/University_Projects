import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * public class AVLTree
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 * <p>
 * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
 * arguments. Changing these would break the automatic tester, and would result in worse grade.
 * <p>
 * However, you are allowed (and required) to implement the given functions, and can add functions of your own
 * according to your needs.
 */

public class AVLTree {

    int size;
    AVLNode min, max, root;

    /**
     * This constructor creates an empty AVLTree.
     * O(1)
     */
    public AVLTree() {
        size = 0;
        min = null;
        max = null;
        root = null;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     * O(1)
     */
    public boolean empty() {
        return (size == 0);
    }


    /**
     * public int searchNode(int k)
     * <p>
     * Searches node with key k
     * If finds, it returns it
     * Otherwise, returns null
     * O(depth) = O(log(n))
     */
    public AVLNode searchNode(int k) {
        AVLNode cur = this.root;
        while (cur != null && cur.isRealNode) { //classical tree search
            if (cur.key == k) {
                return cur;
            } else if (cur.key < k) {
                cur = cur.rightChild;
            } else {
                cur = cur.leftChild;
            }
        }
        return null;
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     * Does it by calling the searchNode method
     * O(depth) = O(log(n))
     */
    public Boolean search(int k) {
        AVLNode node = searchNode(k);
        if (node == null) {
            return null;
        } else {
            return node.getValue();
        }
    }

    /**
     * public int insert(int k, boolean i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
     * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     *  O(depth) = O(log(n))
     */
    public int insert(int k, boolean i) {
        if (this.empty()) { //empty tree
            min = max = root = new AVLNode(k, i, null);
            size++;
            return 1;
        }
        AVLNode cur = this.getRoot();
        while (cur.isRealNode) { //searches where to insert k
            if (cur.key == k) { //key already exists, just update value
                return -1;
            } else if (cur.key < k) { //need to go right
                cur = cur.getRight();
            } else { //need to go left
                cur = cur.getLeft();
            }
        }
        this.size++;
        cur = cur.getParent();
        AVLNode newNode = new AVLNode(k, i, cur);
        if (cur.key < k) {
            cur.setRight(newNode);
        } else {
            cur.setLeft(newNode);
        }

        if (this.min == null || k < this.min.getKey()) { //update min
            this.min = newNode;
        }

        if (this.max == null || k > this.max.getKey()) { //update max
            this.max = newNode;
        }

        return rebalance(cur) + 1; //rebalance and go up the tree
    }


    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     * Does so by calling deleteNode
     * O(depth) = O(log(n))
     */
    public int delete(int k) {
        if (this.empty()) {
            return -1;
        }
        AVLNode cur = searchNode(k);
        if (cur == null) {
            return -1;
        } else {
            int actions = deleteNode(cur);
            this.size--;
            return actions;
        }
    }

    /**
     * public int deleteNode(AVLNode cur)
     * <p>
     * deletes the node cur from the tree
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * or, -1 if cur == null
     * O(depth) = O(log(n))
     */
    public int deleteNode(AVLNode cur) {
        if (cur == null) {
            return -1;
        }
        if (cur == min) { //update min
            min = successor(min);
        }
        if (cur == max) { //update max
            max = predecessor(max);
        }
        if (size() == 1) { //edge case
            min = max = null;
            root = null;
            return 0;
        } else if (cur.getLeft().isRealNode() && cur.getRight().isRealNode()) { //has both children
            AVLNode suc = successor(cur); //can't be null since cur has right
            swapPlaces(cur, suc);
            return deleteNode(cur); //calls again, but on node with one child (proven in class)
        } else if (cur.getLeft().isRealNode() ^ cur.getRight().isRealNode()) { //has one child
            AVLNode child = null;
            if (cur.getLeft().isRealNode()) {
                child = cur.getLeft();
            } else {
                child = cur.getRight();
            }
            if (cur == root) { //edge case
                min = max = root = child;
                child.setParent(null);
                return 0;
            } else {
                AVLNode par = cur.getParent();
                child.setParent(par);
                if (par.getRight() == cur) {
                    par.setRight(child);
                } else {
                    par.setLeft(child);
                }
                return rebalance(par); //rebalancing starts from parent of removed node
            }
        } else { //leaf
            if (cur.getParent().getLeft() == cur) {
                cur.getParent().setLeft(new AVLNode().createDummy(cur.getParent()));
            } else {
                cur.getParent().setRight(new AVLNode().createDummy(cur.getParent()));
            }
            return rebalance(cur.getParent());
        }
    }

    /**
     * public void swapPlaces(AVLNode n1, AVLNode n2)
     * <p>
     * Swaps the locations of n1, n2 in the tree
     * that is, swaps their left and right children, and their parents.
     * Also, updates root field if needed
     * O(1)
     */
    public void swapPlaces(AVLNode n1, AVLNode n2) {
        AVLNode tmp1, tmp2;
        //left child
        tmp1 = n1.getLeft();
        tmp2 = n2.getLeft();
        n1.setLeft(tmp2);
        n2.setLeft(tmp1);
        tmp2.setParent(n1);
        tmp1.setParent(n2);
        //right child
        tmp1 = n1.getRight();
        tmp2 = n2.getRight();
        n1.setRight(tmp2);
        n2.setRight(tmp1);
        tmp2.setParent(n1);
        tmp1.setParent(n2);
        //parents
        tmp1 = n1.getParent();
        tmp2 = n2.getParent();
        n1.setParent(tmp2);
        n2.setParent(tmp1);
        if (tmp1 != null) {
            if (tmp1.getLeft() == n1) {
                tmp1.setLeft(n2);
            } else {
                tmp1.setRight(n2);
            }
        }
        if (tmp2 != null) {
            if (tmp2.getLeft() == n2) {
                tmp2.setLeft(n1);
            } else {
                tmp2.setRight(n1);
            }
        }
        if (n1 == root) {
            root = n2;
        } else if (n2 == root) {
            root = n1;
        }
    }

    /**
     * private int rebalance(AVLNode node)
     * <p>
     * Starts the rebalancing process from the node upwards, to the root
     * Counts the number of rotations / height changes, and returns it
     * O(depth(node)) = O(log(n))
     */
    private int rebalance(AVLNode node) {
        int actionCount = 0;
        AVLNode cur = node;
        while (cur != null) {
            int bf = cur.getBF();
            boolean hasChanged = cur.updateAll(); //flag checks if height has changed
            if (Math.abs(bf) < 2 && hasChanged) { //only height changed
                actionCount++;
            } else if (Math.abs(bf) >= 2) { //need rotation
                AVLNode tmpNode = this.rotate(cur);
                if (tmpNode.getParent() == null) {
                    this.root = tmpNode;
                }
                actionCount++;
            }
            cur = cur.getParent(); //up to parent
        }
        return actionCount;
    }

    /**
     * private void rotateRight(AVLNode node)
     * <p>
     * Performs a right rotation, with the "head" as node
     * O(1)
     */
    private void rotateRight(AVLNode node) {
        AVLNode child = node.getLeft();
        AVLNode parent = node.getParent();

        node.setLeft(child.getRight());
        child.setRight(node);
        child.setParent(node.getParent());
        node.setParent(child);
        node.getLeft().setParent(node);

        if (parent != null) {
            if (node == parent.getRight()) {
                parent.setRight(child);
            } else {
                parent.setLeft(child);
            }
        }

        node.updateAll();
        child.updateAll();
    }

    /**
     * private void rotateLeft(AVLNode node)
     * <p>
     * Performs a left rotation, with the "head" as node
     * O(1)
     */
    private void rotateLeft(AVLNode node) {
        AVLNode child = node.getRight();
        AVLNode parent = node.getParent();

        node.setRight(child.getLeft());
        child.setLeft(node);
        child.setParent(node.getParent());
        node.setParent(child);
        node.getRight().setParent(node);

        if (parent != null) {
            if (node == parent.getRight()) {
                parent.setRight(child);
            } else {
                parent.setLeft(child);
            }
        }

        node.updateAll();
        child.updateAll();
    }

    /**
     * private AVLNode rotate(AVLNode node)
     * <p>
     * Performs the correct rotation on node (if it needs one)
     * Returns the parent of node after the rotation
     * precondition: all nodes used in the rotation are already updated
     * O(1)
     */
    private AVLNode rotate(AVLNode node) {
        if (node.getBF() == -2) { //right heavier
            if (node.getRight().getBF() == 1) { //left heavier
                this.rotateRight(node.getRight());
                this.rotateLeft(node);
            } else { //right heavier
                this.rotateLeft(node);
            }
        } else if (node.getBF() == 2) { //left heavier
            if (node.getLeft().getBF() == -1) {//right heavier
                this.rotateLeft(node.getLeft());
                this.rotateRight(node);
            } else { //left heavier
                this.rotateRight(node);
            }
        }
        return node.getParent();
    }


    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     * O(1)
     */
    public Boolean min() {
        if (this.min == null) {
            return null;
        } else {
            return this.min.value;
        }
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     * O(1)
     */
    public Boolean max() {
        if (this.max == null) {
            return null;
        } else {
            return this.max.value;
        }
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     * O(n)
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size];
        AVLNode node = this.min;
        int curIndex = 0;
        while (node != null) {
            arr[curIndex] = node.getKey();
            node = successor(node);
            curIndex++;
        }
        return arr;
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     * O(n)
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.size];
        AVLNode node = this.min;
        int curIndex = 0;
        while (node != null) {
            arr[curIndex] = node.getValue();
            node = successor(node);
            curIndex++;
        }
        return arr;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * O(1)
     */
    public int size() {
        return this.size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * O(1)
     */
    public AVLNode getRoot() {
        return root;
    }

    /**
     * public boolean prefixXor(int k)
     * <p>
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     * <p>
     * precondition: this.search(k) != null
     * O(depth(k)) = O(log(n))
     */
    public boolean prefixXor(int k) {
        if (empty()) {
            return false;
        }
        AVLNode cur = this.root;
        boolean curXor = false;
        while (cur.isRealNode && cur.getKey() != k) { //"searches" for k, while maintaining the xor value
            if (cur.getKey() < k) {
                curXor ^= cur.getLeft().getOnes(); //xors the xor value of left subtree, which are all less than k
                curXor ^= cur.getValue(); //xors current value
                cur = cur.getRight();
            } else {
                cur = cur.getLeft();
            }
        }
        if (cur.isRealNode) { //if found k, xor its node value and left subtree xor
            curXor ^= (cur.getValue() ^ cur.getLeft().getOnes());
        }
        return curXor;
    }

    /**
     * public AVLNode successor(AVLNode node)
     * <p>
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     * O(|height(node) - height(successor(node))|) = O(log(n))
     */
    public AVLNode successor(AVLNode node) {
        if (node == null || node == this.max) { //null successor
            return null;
        }
        if (node.getRight().isRealNode()) { //case 1: return minimum of right subtree
            AVLNode cur = node.getRight();
            while (cur.getLeft().isRealNode()) {
                cur = cur.getLeft();
            }
            return cur;
        } else { //case 2: no right subtree, we go up left
            AVLNode cur = node.getParent(), prv = node;
            while (cur != null && cur.getRight() == prv) {
                prv = cur;
                cur = cur.getParent();
            }
            return cur;
        }
    }


    /**
     * public AVLNode predecessor(AVLNode node)
     * <p>
     * given a node 'node' in the tree, return the predecessor of 'node' in the tree (or null if predecessor doesn't exist)
     *
     * @param node - the node whose predecessor should be returned
     * @return the predecessor of 'node' if exists, null otherwise
     * O(|height(node) - height(successor(node))|) = O(log(n))
     */
    public AVLNode predecessor(AVLNode node) {
        if (node == null || node == this.min) { //null predecessor
            return null;
        }
        if (node.getLeft().isRealNode()) { //case 1: left subtree, get maximum there
            AVLNode cur = node.getLeft();
            while (cur.getRight().isRealNode()) {
                cur = cur.getRight();
            }
            return cur;
        } else { //case 2: no left subtree, go up right
            AVLNode cur = node.getParent(), prv = node;
            while (cur != null && cur.getLeft() == prv) {
                prv = cur;
                cur = cur.getParent();
            }
            return cur;
        }
    }


    /**
     * public boolean succPrefixXor(int k)
     * <p>
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     * <p>
     * precondition: this.search(k) != null
     * O(rank(k) * log(rank(k))) = O(n)
     */
    public boolean succPrefixXor(int k) {
        AVLNode cur = this.min;
        boolean curXor = false;
        while (cur != null && cur.getKey() <= k) { //brute force
            curXor ^= cur.getValue();
            cur = successor(cur);
        }
        return curXor;
    }


    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     * <p>
     * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
     * arguments. Changing these would break the automatic tester, and would result in worse grade.
     * <p>
     * However, you are allowed (and required) to implement the given functions, and can add functions of your own
     * according to your needs.
     *
     */
    public class AVLNode {

        private int key, height;
        private boolean ones;
        private Boolean value;
        private AVLNode leftChild, rightChild, parent;
        private boolean isRealNode;

        /**
         * This constructor creates an empty, dummy AVLNode.
         */
        public AVLNode() {
            this.isRealNode = false;
            this.leftChild = null;
            this.rightChild = null;
            this.key = -1;
            this.value = null;
            this.height = -1;
            this.ones = false;
            this.isRealNode = false;
        }

        /**
         * This constructor creates a real AVLNode, with given key value and parent.
         * O(1)
         */
        public AVLNode(int key, Boolean value, AVLNode parent) { //constructor for real node
            this.isRealNode = true;
            this.key = key;
            this.value = value;
            this.height = 0;
            this.ones = value;
            this.parent = parent;
            this.leftChild = createDummy(this);
            this.rightChild = createDummy(this);
        }

        /**
         * This function returns a dummy AVLNode with given parent
         * O(1)
         */
        public AVLNode createDummy(AVLNode parent) {
            AVLNode dummy = new AVLNode();
            dummy.setParent(parent);
            return dummy;
        }

        /**
         * public boolean updateAll()
         * <p>
         * This function update a node's height and amount of ones in subtree, based on these values of their children.
         * The function returns true if and only if the node's height has changed.
         * O(1)
         */
        public boolean updateAll() {
            boolean hasChanged = false;
            if (this.isRealNode) {
                if (this.height != 1 + Math.max(this.leftChild.height, this.rightChild.height)) {
                    this.height = 1 + Math.max(this.leftChild.height, this.rightChild.height);
                    hasChanged = true;
                }
                this.ones = (value == Boolean.TRUE) ^ this.leftChild.ones ^ this.rightChild.ones;
            }
            return hasChanged;
        }

        // returns BF based on children
        // O(1)
        public int getBF() {
            return this.leftChild.getHeight() - this.rightChild.getHeight();
        }

        //returns key
        public int getKey() {
            return this.key;
        }

        //returns node's value [info] (for virtual node return null)
        public Boolean getValue() {
            return this.value;
        }

        //sets left child
        public void setLeft(AVLNode node) {
            this.leftChild = node;
        }

        //returns left child
        //if called for virtual node, return value is ignored.
        public AVLNode getLeft() {
            return this.leftChild;
        }

        //sets right child
        public void setRight(AVLNode node) {
            this.rightChild = node;
        }

        //returns right child
        //if called for virtual node, return value is ignored.
        public AVLNode getRight() {
            return this.rightChild;
        }

        //sets parent
        public void setParent(AVLNode node) {
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        public AVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            return this.isRealNode;
        }

        // sets the height of the node
        public void setHeight(int height) {
            this.height = height;
        }

        // Returns the height of the node (-1 for virtual nodes)
        public int getHeight() {
            return this.height;
        }

        //returns the parity of the amount of true values in the subtree
        public boolean getOnes() {
            return this.ones;
        }
    }
}