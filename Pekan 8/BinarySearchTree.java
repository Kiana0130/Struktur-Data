import java.util.Random;

class Node {
    int key;
    Node left, right;

    public Node(int item) {
        key = item;
        left = right = null;
    }
}

class BinarySearchTree {
    Node root;

    BinarySearchTree() {
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

        if (key < root.key)
            root.left = insertRec(root.left, key);
        else if (key > root.key)
            root.right = insertRec(root.right, key);

        return root;
    }

    void preOrder(Node root) {
        if (root != null) {
            System.out.print(root.key + " ");
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    void inOrder(Node root) {
        if (root != null) {
            inOrder(root.left);
            System.out.print(root.key + " ");
            inOrder(root.right);
        }
    }

    void postOrder(Node root) {
        if (root != null) {
            postOrder(root.left);
            postOrder(root.right);
            System.out.print(root.key + " ");
        }
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        Random rand = new Random();

        int[] keys = new int[10];

        System.out.println("Deret key acak:");
        for (int i = 0; i < 10; i++) {
            keys[i] = rand.nextInt(100) + 1;
            System.out.print(keys[i] + " ");
            tree.insert(keys[i]);
        }

        System.out.println("\n\nAlgoritma:");

        System.out.print("Pre-order  : ");
        tree.preOrder(tree.root);

        System.out.print("\nIn-order   : ");
        tree.inOrder(tree.root);

        System.out.print("\nPost-order : ");
        tree.postOrder(tree.root);

    }
}
