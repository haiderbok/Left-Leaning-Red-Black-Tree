import java.util.*;

public class LeftLeaningRedBlackTreeMap<K extends Comparable<? super K>, V> extends Object {
    //The root of the tree
    private Node<K,V> root;

    //The size of this map
    private int size;

    // Constructor
    public LeftLeaningRedBlackTreeMap(){

        //Initializing root to be null at first
        this.root = null;

        //Initialising this size to be 0
        this.size = 0;
    } // Constructor

    // Returning the size of this map
    public int size(){
        return this.size;
    }//size

    //Checking if the tree is empty
    public boolean isEmpty(){
        return this.size == 0;
    } //isEmpty


    // Puts key and valued in the tree
    public V put​(K key, V value) throws NullPointerException{

        if (key == null){
            throw new NullPointerException();
        }
        V temp = null;
        //Create a new node
        Node node =  new Node(Color.RED, key, value, null, null, null);

        // check if the size is 0
            if (size == 0){

                //Assign the root a new node with no parents and children
                this.root = new Node(Color.BLACK, key,value,null,null,null);

                //Increment the size
                size++;

            } else {

                // Call the get function to see if we have have this key in the tree and store that value in a temp to return
                temp =  this.get​(key);

                // Call the insert function and insert the node at the right position.
                // After the call make root back to the original root
                this.root = Insert(this.root, node);

                // Increment the size
                size++;
            }

            Fixup(this.root);

        // Debug
//            PostorderTras(this.root);
//            System.out.println();


    return temp;
    }


    //Fix up function to check if the properties of the bblt are met
    private void Fixup (Node node){

        // if the we reach the end just return (Base case)
        if (node  == null){
            return;
        }

        // Do a color flip if the right and the left both children are red
        if (node.getLeftChild() != null && node.getRightChild() != null && (node.getRightChild().getColor().compareTo(Color.RED) == 0) &&(node.getLeftChild().getColor().compareTo(Color.RED) == 0)){
            colorFlip(node);
            node = this.root;
            Fixup(node);
        }

        // if there is a right leaning red link do a LEFT rotation
        if (node.getRightChild() != null && (node.getRightChild().getColor().compareTo(Color.RED) == 0)) {
            Leftrotation(node);
            node = this.root;
            Fixup(node);
        }


        // if there are two red nodes do a RIGHT ROTATION
        if (node.getLeftChild() != null && node.getLeftChild().getLeftChild() != null &&
                (node.getLeftChild().getColor().compareTo(Color.RED) == 0) &&
                (node.getLeftChild().getLeftChild().getColor().compareTo(Color.RED) ==0) ){

            // Do a right rotation
            RightRotation(node.getLeftChild());
            // Go over the tree again
            node = this.root;
            Fixup(node);
        }




        //Transverse Pre order
        Fixup(node.getLeftChild());


        Fixup(node.getRightChild());

    }

    // Debug check your Insert function
    private void PostorderTras (Node node){

        if (node == null){
            return;
        }

        PostorderTras(node.getLeftChild());

        PostorderTras(node.getRightChild());

        System.out.print(node.getKey());
        System.out.print(" ");
        System.out.print(node.getValue() + ",");

    }

    Node parent = null;

    // To insert a node (helper function)
    private Node Insert (Node root, Node node) {

        if (root == null){
            root = new Node(Color.RED,node.getKey(),node.getValue(),parent,null,null);
            return root;
        }

        // if the key already exsists then update the value
        if (node.getKey().compareTo(root.getKey()) == 0){

            // update the value of the node
            root.setValue(node.getValue());

            // Decrement the size if the value is being updated
            size--;

        }

        // if the key is less than current root we refer to the left subtree
        if (node.getKey().compareTo(root.getKey()) < 0){

            // Parent node to know the parent of the inserted node
            parent = root;

            // make our root point to the right
            root.setLeftChild(Insert(root.getLeftChild(),node));
        }

        // if the key is greater than current root we refer to the right subtree
        if (node.getKey().compareTo((K)root.getKey()) > 0){

            // Parent node to know the parent of the inserted node
            parent = root;

            // make our root point to the right
            root.setRightChild(Insert(root.getRightChild(),node));
        }

        // return root to undo the effect of this recursion can store the unchanged pointer back
        return root;
    } // Insert

    // Color flip
    private void colorFlip(Node node){

        // set the left and right nodes to be Black
        node.getLeftChild().setColor(Color.BLACK);
        node.getRightChild().setColor(Color.BLACK);

        // If its not the root then set this node to be a red node
        if (node != this.root){
            node.setColor(Color.RED);
        }

    }

    // Pass the parent node of the right leaning red node
    private void Leftrotation (Node node){

        // Save the three nodes in consideration
        Node exchange = node.getRightChild(); // The right child of the current node
        Node top = node.getParent(); // parent of the current node
        Node down = exchange.getLeftChild(); /// the left child of the the right child of the current node

        //Save the current color of the
        Color color = node.getColor();

        // Check if its not the root
        // and then switch the right child to the parent of that
        if (top != null){

            //If parent rights child is under consideration
            if (top.getRightChild() != null && top.getRightChild().equals(node)){

                //Set the right child of the top to be the exchanged
                top.setRightChild(exchange);

                // Make the parent of exchange top
                exchange.setParent(top);
            }

            //If parent left child is under consideration
            if (top.getLeftChild().equals(node)){

                //Set the left child of the top to be the exchanged
                top.setLeftChild(exchange);

                // Make the parent of exchange top
                exchange.setParent(top);
            }
            // if this exchange node is going to be the root
        } else {
            // make the parent null
            exchange.setParent(null);

            // The right child is the root
            this.root = exchange;
        }

        // make the exchange node the parent of the node
        node.setParent(exchange);

        // Make this left child of the exchange to be the current node
        exchange.setLeftChild(node);




        // _______DEBUG_______
       // System.out.println(exchange.getLeftChild().getLeftChild());

        //Check if the down (left child of exchange) is not null
        if (down != null){

            // make down the right child of the current node
            node.setRightChild(down);

            // reset the parent to be the current node
            down.setParent(node);

        } else {
            // if the down is null then make the right child of the current node null
            node.setRightChild(null);
        }

        //Make the current node color to be red
         node.setColor(Color.RED);

        //The exchange node will have the color that the current had before the flip
        exchange.setColor(color);


    }

    // Pass the node that will become the parent of the current parent
    private void RightRotation (Node node){

        // Save all the nodes for the rotation
        Node exchange1 = node.getParent(); // Parent of the current node
        Node down1 = node.getRightChild(); // Right child of the node
        Node top1 = exchange1.getParent();
//        top1 = (exchange1.getParent().equals(null)) ? null : exchange1.getParent();

        //if its not the root
        if (top1 != null) {

            //check if the parent is left child of the grandparent
            if (top1.getLeftChild().equals(exchange1)){

                // Grandparents children is now the current node
                top1.setLeftChild(node);

                // set the parent of the current node which was the grandparent
                node.setParent(top1);
            }

            //check if the parent is right child of the grandparent
            if (top1.getRightChild().equals(exchange1)){

                // Grandparents children is now the current node
                top1.setRightChild(node);

                // set the parent of the current node which was the grandparent
                node.setParent(top1);
            }

            // other wise set the Grandparent to null
        } else {
            node.setParent(null);

            // Set this node as the root
            this.root = node;

            // make the color Black
            node.setColor(Color.BLACK);
        }

        // set the right children of the current node to be it parent(exchange)
        node.setRightChild(exchange1);

        // Make the color of the exchange code RED
        exchange1.setColor(Color.RED);

        // point the parent of the exchange to the node that takes its position
        exchange1.setParent(node);

        // Check if the down(right child is current) is empty
        if (down1 != null){

            //set this node now to be the left child of the exchange
            exchange1.setLeftChild(down1);

            // set the parent of down 1 to be exchange 1
            down1.setParent(exchange1);

        } else {

            //Other wise set the left child to be null
            exchange1.setLeftChild(null);
        }

        //flip the color so that the tree is balanced
        colorFlip(node);
    }




    //clearing the tree
    public void clear(){

        // Set the root to be null and garbage collector will erase everything else
        this.root = null;

        //set the size to be 0
        size = 0;

    }// clear

    // Check to see if the current key exsists in the tree
    public boolean containsKey​(K key) throws NullPointerException {

        // throw a null pointer exception if the key is empty
        if (key == null){
            throw new NullPointerException();
        }

        // Call the get function if it doesn't return null then the key
        // exsists other wise it doesn't
        if(get​(key) == null){
            return  false;
        } else {
            return true;
        }
    }

    // A global boolean flag for the containsValue function

//    public boolean containsValue​(V value) {
//        boolean equal = false;
//    Queue<Node> nodes = Bft(this);
//
//    if (this.root == null){
//        return false;
//    }
//
//    if (nodes.isEmpty()){
//            return false;
//        }
//        Node temp = nodes.remove();
//    while (!nodes.isEmpty()){
//        if (value.equals(temp.getValue())){
//            equal = true;
//        }
//      if (equal){
//          break;
//      }
//      temp = nodes.remove();
//    }
//    return equal;
//    }

    // A global boolean flag for the containsValue function

    public boolean containsValue​(V value) {

        return checkInorder(this.root, value);

    }

    private boolean checkInorder (Node node, V value){

        if (node == null){
            return false;
        }

        // If we found the value return true
        if (node.getValue().equals(value))
            // Assign the flag value of true
            return true;

        //Transverse in order
       return checkInorder(node.getLeftChild(), value) ||  checkInorder(node.getRightChild(), value);




    }

    public K firstKey() throws NoSuchElementException {
        // If the map is empty throw No such element Exception
        if (this.size == 0){
            throw new NoSuchElementException();
        }

        // Store the root into a temporary node to transverse
        // and define a temp node to find the first key
        Node node = this.root;
        Node temp = null;

        while (true){
            node = node.getLeftChild();
            if (node != null){
                temp = node;
            }
            if (node == null){
                break;
            }
        }

        return (K)temp.getKey();
    }



    public K lastKey() throws NoSuchElementException {

        // If the map is empty throw No such element Exception
        if (this.size == 0){
            throw new NoSuchElementException();
        }

        // Store the root into a temporary node and define a temp node to find the last key
        Node node = this.root;
        Node temp = null;

        while (true){
            node = node.getRightChild();
            if (node != null){
                temp = node;
            }
            if (node == null){
                break;
            }
        }

        return (K)temp.getKey();
    }

    public K ceilingKey​(K key) throws NullPointerException {

        // If the key is null throw null pointer exception
        if (key == null){
            throw new NullPointerException();
        }

        // Call the ceil helper  function
         K result = Ceil(this.root,key);

        return result;
    }

    K result = null;

    private K Ceil (Node node, K key){

        // Run he loop until we reach on of the ends of the loop
        while (node != null) {

            // If the node's key is less thean provided key
            // The ceil value must be in the right subtree
            if (node.getKey().compareTo(key) < 0) {
                node = node.getRightChild();

                // Store the value of this key if noe is not null
                //and the node key is greater than our key
                if (node != null && !(node.getKey().compareTo(key) < 0)) {

                    result = (K) node.getKey();
                    if (result == key){
                        return result;
                    }
                }
                continue;
            }

            // Store the value of this key if noe is not null
            //and the node key is greater than our key
            if (node != null && !(node.getKey().compareTo(key) < 0)) {

                result = (K) node.getKey();
                if (result == key){
                    return result;
                }
            }

            // eLse the ceil must be the root or in the left subtree
            node = node.getLeftChild();
        }
        return result;
        }

    public K floorKey​(K key) throws NullPointerException {
        // If the key is null throw null pointer exception
        if (key == null){
            throw new NullPointerException();
        }

        // Call the ceil helper  function
        K result = Floor(this.root,key);

        return result;
    }

    private K Floor (Node node, K key){
        // Runt he loop until we reach on of the ends of the loop
        while (node != null) {

            // If the node's key is less then provided key
            // The ceil value must be in the right subtree
            if (node.getKey().compareTo(key) > 0) {
                node = node.getLeftChild();

                // Store the value of this key if noe is not null
                //and the node key is greater than our key
                if (node != null && !(node.getKey().compareTo(key) > 0)) {

                    result = (K) node.getKey();
                    if (result == key){
                        return result;
                    }
                }
                continue;
            }

            // Store the value of this key if noe is not null
            //and the node key is greater than our key
            if (node != null && !(node.getKey().compareTo(key) > 0)) {

                result = (K) node.getKey();
                if (result == key){
                    return result;
                }
            }

            // eLse the ceil must be the root or in the left subtree
            node = node.getRightChild();
        }
        return result;
    }

    public K higherKey​(K key) throws NullPointerException {
        // If the key is null throw null pointer exception
        if (key == null){
            throw new NullPointerException();
        }

        // Call the ceil helper  function
        K result = higher(this.root,key);

        return result;
    }

    private K higher (Node node, K key){
        // Run he loop until we reach on of the ends of the loop
        while (node != null) {

            // If the node's key is less thean provided key
            // The ceil value must be in the right subtree
            if (!(node.getKey().compareTo(key) > 0)) {
                node = node.getRightChild();

                // Store the value of this key if noe is not null
                //and the node key is greater than our key
                if (node != null && !(node.getKey().compareTo(key) <= 0)) {

                    result = (K) node.getKey();

                }

//                if (node != null && node.getKey().compareTo(key) == 0 && node.getRightChild() == null){
//                    result = null;
//                }
                continue;
            }

            // Store the value of this key if noe is not null
            //and the node key is greater than our key
            if (node != null && !(node.getKey().compareTo(key) <= 0)) {

                result = (K) node.getKey();
//                if (result == key){
//                    return result;
//                }
            }

            // eLse the ceil must be the root or in the left subtree
            node = node.getLeftChild();


        }

        return result;
    }


    public K lowerKey​(K key) throws NullPointerException {
        // If the key is null throw null pointer exception
        if (key == null){
            throw new NullPointerException();
        }

        // Call the ceil helper  function
        K result = lower(this.root,key);

        return result;
    }

    private K lower (Node node , K key){
        // Run he loop until we reach on of the ends of the loop
        while (node != null) {

            // If the node's key is less thean provided key
            // The ceil value must be in the right subtree
            if (!(node.getKey().compareTo(key) < 0)) {
                node = node.getLeftChild();

                // Store the value of this key if noe is not null
                //and the node key is greater than our key
                if (node != null && !(node.getKey().compareTo(key) >= 0)) {

                    result = (K) node.getKey();

                }

//                if (node != null && node.getKey().compareTo(key) == 0 && node.getRightChild() == null){
//                    result = null;
//                }
                continue;
            }

            // Store the value of this key if noe is not null
            //and the node key is greater than our key
            if (node != null && !(node.getKey().compareTo(key) >= 0)) {

                result = (K) node.getKey();
//                if (result == key){
//                    return result;
//                }
            }

            // eLse the ceil must be the root or in the left subtree
            node = node.getRightChild();


        }

        return result;
    }



    public V get​(K key) throws NullPointerException {

        //Throw a null pointer exception if the key is null
        if (key == null){
            throw new NullPointerException();
        }

        // Declare a queue and store all the nodes in
        Queue<Node> queue = Bft(this);

        //Create a temp node to store the temp node
        Node node;

        // While the queue is not empty
        while (queue != null && !queue.isEmpty()){
            // Save the think in the queue
            node = queue.remove();

            // If we find the key return it
            if (key == node.getKey() ){
                return (V)node.getValue();
            }
        }

        // Other wise return null
        return null;
    }


    public Queue<Node> Bft (LeftLeaningRedBlackTreeMap map){

        // Checking to see if the list is empty and returning null in this case
        if (map.size == 0){
            return null;
        }

        //Create two queue's to save the nodes in order and to transverse through
        Queue<Node> queue = new LinkedList<>();
        Queue<Node> transverse = new LinkedList<>();

        //Node to start the iteration
        Node root = map.root;

        // Insert this node into the queue
        ((LinkedList<Node>) queue).add(root);
        ((LinkedList<Node>) transverse).add(root);



        // While loop to transverse through the map(Breath-First-Transverse)
        while (!transverse.isEmpty()) {

            //pop the stored node in the transverse loop and store that in the root node
            root = ((LinkedList<Node>) transverse).pop();

            //check if the left child is not null
            if(root.getLeftChild() != null) {
                ((LinkedList<Node>) queue).add(root.getLeftChild());
                ((LinkedList<Node>) transverse).add(root.getLeftChild());

            }

            //Check if right node is not null
            if(root.getRightChild() != null) {
                ((LinkedList<Node>) queue).add(root.getRightChild());
                ((LinkedList<Node>) transverse).add(root.getRightChild());
            }

        }

        //Return queue full of nodes
        return queue;
    } //Bft


    // Checks to see of the two maps are equal
    public boolean equals(Object object) {

        //Check to see if the we are comparing the tree to itself
        if (this == object) {
            return true;
        }

        if (!(object instanceof LeftLeaningRedBlackTreeMap)) {
            return false;
        }

            if (this == object) {
                return true;
            } else if (object instanceof LeftLeaningRedBlackTreeMap) {

                // Cast the object into a Left Leaning Red Black tree
                LeftLeaningRedBlackTreeMap map = (LeftLeaningRedBlackTreeMap) object;


                //if the size of the the two trees is not the same
                if (this.size != map.size) {
                    return false;
                }


                //If both the trees are empty they are the same
                if (this.root == null && map.root == null) {
                    return true;
                }

                // If the two roots are not the same return false
                if (!this.root.equals(map.root)) {
                    return false;
                }

                Queue<Node> node = Bft(this);
                Queue<Node> map1 = Bft(map);

                return node.equals(map1);

            } else {
                return false;
            }
    }



    public String toString(){
        // Store the tree in a queue;
        Queue<Node> queue = Bft(this);
        System.out.println(queue);
        //Create a String to store the result
        String str = "";

        //Create a node to store the pooped items from the Queue
        Node node;

        while (queue != null && !queue.isEmpty()) {
            // Format of the string
            String format = "%s=%s, ";

            //Node to store the poped item
            node = queue.remove();

            // Add the element to the String
            str  += String.format(format, String.valueOf(node.getKey()), String.valueOf(node.getValue()));
        }

        if (!str.equals("")) {
            str = "{" + str.substring(0, str.length() - 2) + "}";
        } else {
            str = "{}";
        }

        return str;
    } // to String

}
