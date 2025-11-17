public class RedBlackTree {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    class Node {
        int key;
        boolean color;
        Node left, right, parent;

        Node(int key) {
            this.key = key;
            this.color = RED; 
        }
    }

    private Node root;

    // ===== INSERT NODE =====
    public void insert(int key) {
        Node newNode = new Node(key);
        root = bstInsert(root, newNode);
        fixInsert(newNode);
    }

    // Insert seperti BST biasa
    private Node bstInsert(Node root, Node node) {
        if (root == null)
            return node;

        if (node.key < root.key) {
            root.left = bstInsert(root.left, node);
            root.left.parent = root;
        } else if (node.key > root.key) {
            root.right = bstInsert(root.right, node);
            root.right.parent = root;
        }

        return root;
    }

    // ===== FIX VIOLATION =====
    private void fixInsert(Node node) {
        Node parent, grandparent;

        while (node != root && node.parent.color == RED) {
            parent = node.parent;
            grandparent = parent.parent;

            // Orang tua adalah child kiri dari grandparent
            if (parent == grandparent.left) {

                Node uncle = grandparent.right;

                // CASE 1: Uncle RED -> Recolor
                if (uncle != null && uncle.color == RED) {
                    grandparent.color = RED;
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    node = grandparent;
                } else {
                    // CASE 2: Node adalah right child -> rotate kiri
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                    }

                    // CASE 3: Node adalah left child -> rotate kanan
                    parent.color = BLACK;
                    grandparent.color = RED;
                    rotateRight(grandparent);
                }

            } else {  
                // Parent adalah child kanan dari grandparent
                Node uncle = grandparent.left;

                // CASE 1: uncle RED
                if (uncle != null && uncle.color == RED) {
                    grandparent.color = RED;
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    node = grandparent;
                } else {
                    // CASE 2
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                    }
                    // CASE 3
                    parent.color = BLACK;
                    grandparent.color = RED;
                    rotateLeft(grandparent);
                }
            }
        }
        root.color = BLACK; 
    }

    // ===== ROTATIONS =====
    private void rotateLeft(Node node) {
        Node r = node.right;
        node.right = r.left;

        if (r.left != null)
            r.left.parent = node;

        r.parent = node.parent;

        if (node.parent == null)
            root = r;
        else if (node == node.parent.left)
            node.parent.left = r;
        else
            node.parent.right = r;

        r.left = node;
        node.parent = r;
    }

    private void rotateRight(Node node) {
        Node l = node.left;
        node.left = l.right;

        if (l.right != null)
            l.right.parent = node;

        l.parent = node.parent;

        if (node.parent == null)
            root = l;
        else if (node == node.parent.left)
            node.parent.left = l;
        else
            node.parent.right = l;

        l.right = node;
        node.parent = l;
    }

    // ===== TRAVERSAL =====
    public void preorderTraversal() {
        System.out.print("Pre-order: ");
        preorder(root);
        System.out.println();
    }

    private void preorder(Node node) {
        if (node != null) {
            System.out.print(node.key + "(" + (node.color == RED ? "R" : "B") + ") ");
            preorder(node.left);
            preorder(node.right);
        }
    }

    public void inorderTraversal() {
        System.out.print("In-order:  ");
        inorder(root);
        System.out.println();
    }

    private void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.key + "(" + (node.color == RED ? "R" : "B") + ") ");
            inorder(node.right);
        }
    }

    public void postorderTraversal() {
        System.out.print("Post-order:");
        postorder(root);
        System.out.println();
    }

    private void postorder(Node node) {
        if (node != null) {
            postorder(node.left);
            postorder(node.right);
            System.out.print(node.key + "(" + (node.color == RED ? "R" : "B") + ") ");
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();

        int[] keys = {38, 3, 73, 30, 22, 75, 76, 57, 34, 20};
        System.out.println("10 Key:");
        for (int key : keys) {
            System.out.print(key + " ");
            tree.insert(key);
        }
        System.out.println("\n");

        System.out.println("Hasil penelusuran (traversal) tree:");
        tree.inorderTraversal();
        tree.preorderTraversal();
        tree.postorderTraversal();
    }
}
