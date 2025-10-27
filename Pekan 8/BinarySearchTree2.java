public class BinarySearchTree2 {

    class Node {
        int key;
        Node left, right;

        public Node(int item) {
            key = item;
            left = right = null;
        }
    }

    Node root;

    BinarySearchTree2() {
        root = null;
    }

    void insert(int key) {
        root = insertRec(root, key);
    }

    Node insertRec(Node root, int key) {
        if (root == null) {
            root = new Node(key);
            return root;
        }

        if (key < root.key) {
            root.left = insertRec(root.left, key);
        } else if (key > root.key) {
            root.right = insertRec(root.right, key);
        }

        return root;
    }

    void preorderTraversal() {
        System.out.print("Pre-order: ");
        printPreorder(root);
        System.out.println();
    }

    void printPreorder(Node node) {
        if (node != null) {
            System.out.print(node.key + " ");
            printPreorder(node.left);
            printPreorder(node.right);
        }
    }

    void inorderTraversal() {
        System.out.print("In-order:  ");
        printInorder(root);
        System.out.println();
    }

    void printInorder(Node node) {
        if (node != null) {
            printInorder(node.left);
            System.out.print(node.key + " ");
            printInorder(node.right);
        }
    }

    void postorderTraversal() {
        System.out.print("Post-order:");
        printPostorder(root);
        System.out.println();
    }

    void printPostorder(Node node) {
        if (node != null) {
            printPostorder(node.left);
            printPostorder(node.right);
            System.out.print(node.key + " ");
        }
    }

    public static void main(String[] args) {
        BinarySearchTree2 tree = new BinarySearchTree2();

        int[] keys = {50, 30, 70, 20, 40, 60, 80, 15, 25, 75};
        System.out.println("Memasukkan 10 key ke dalam tree:");
        for (int key : keys) {
            System.out.print(key + " ");
            tree.insert(key);
        }
        System.out.println("\n\nProses penyisipan selesai.");
        System.out.println("==========================================");

        System.out.println("Hasil penelusuran (traversal) tree:");
        tree.inorderTraversal();  
        tree.preorderTraversal(); 
        tree.postorderTraversal();
    }
}