import java.awt.*;
import javax.swing.*;

class Node {
    String data; 
    Node left, right, parent;
    boolean color; 

    Node(String data) { 
        this.data = data;
        color = true; 
    }
}

class RedBlackTree {
    private Node root;
    private final boolean RED = true;
    private final boolean BLACK = false;

    public Node getRoot() {
        return root;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;

        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;

        if (x.parent == null)
            root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else
            x.parent.right = y;

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;

        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;

        if (x.parent == null)
            root = y;
        else if (x == x.parent.right)
            x.parent.right = y;
        else
            x.parent.left = y;

        y.right = x;
        x.parent = y;
    }

    public void insert(String data) {
        Node newNode = new Node(data);
        root = bstInsert(root, newNode);
        fixViolation(newNode);
    }

    private Node bstInsert(Node root, Node pt) {
        if (root == null)
            return pt;

        if (pt.data.compareTo(root.data) < 0) {
            root.left = bstInsert(root.left, pt);
            root.left.parent = root;
        } else if (pt.data.compareTo(root.data) > 0) {
            root.right = bstInsert(root.right, pt);
            root.right.parent = root;
        }
        return root;
    }

    private void fixViolation(Node pt) {
        Node parentPt = null;
        Node grandParentPt = null;

        while (pt != root && pt.color == RED && pt.parent.color == RED) {
            parentPt = pt.parent;
            grandParentPt = pt.parent.parent;

            if (parentPt == grandParentPt.left) {
                Node unclePt = grandParentPt.right;

                if (unclePt != null && unclePt.color == RED) {
                    grandParentPt.color = RED;
                    parentPt.color = BLACK;
                    unclePt.color = BLACK;
                    pt = grandParentPt;
                } else {
                    if (pt == parentPt.right) {
                        rotateLeft(parentPt);
                        pt = parentPt;
                        parentPt = pt.parent;
                    }
                    rotateRight(grandParentPt);
                    boolean t = parentPt.color;
                    parentPt.color = grandParentPt.color;
                    grandParentPt.color = t;
                    pt = parentPt;
                }
            } else {
                Node unclePt = grandParentPt.left;

                if (unclePt != null && unclePt.color == RED) {
                    grandParentPt.color = RED;
                    parentPt.color = BLACK;
                    unclePt.color = BLACK;
                    pt = grandParentPt;
                } else {
                    if (pt == parentPt.left) {
                        rotateRight(parentPt);
                        pt = parentPt;
                        parentPt = pt.parent;
                    }
                    rotateLeft(grandParentPt);
                    boolean t = parentPt.color;
                    parentPt.color = grandParentPt.color;
                    grandParentPt.color = t;
                    pt = parentPt;
                }
            }
        }
        root.color = BLACK;
    }
}

class TreePanel extends JPanel {
    private final RedBlackTree tree;

    public TreePanel(RedBlackTree tree) {
        this.tree = tree;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTree(g, tree.getRoot(), getWidth() / 2, 50, getWidth() / 4);
    }

    private void drawTree(Graphics g, Node node, int x, int y, int xOffset) {
        if (node == null) return;

        if (node.left != null) {
            g.setColor(Color.BLACK); 
            g.drawLine(x, y, x - xOffset, y + 80);
            drawTree(g, node.left, x - xOffset, y + 80, xOffset / 2);
        }

        if (node.right != null) {
            g.setColor(Color.BLACK); 
            g.drawLine(x, y, x + xOffset, y + 80);
            drawTree(g, node.right, x + xOffset, y + 80, xOffset / 2);
        }

        g.setColor(node.color ? Color.RED : Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);

        g.setColor(Color.WHITE);
        g.drawOval(x - 15, y - 15, 30, 30);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String val = String.valueOf(node.data); 
        g.drawString(val, x - g.getFontMetrics().stringWidth(val) / 2, y + 5);
    }
}

public class RedBlackTreeVisualizer extends JFrame {
    private final RedBlackTree tree = new RedBlackTree();
    private final TreePanel treePanel = new TreePanel(tree);

    public RedBlackTreeVisualizer() {
        setTitle("Red-Black Tree Visualizer");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        controlPanel.setPreferredSize(new Dimension(900, 70));
        controlPanel.setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel("Masukkan data:"); 
        JTextField input = new JTextField();
        JButton insertButton = new JButton("Insert");

        Font font = new Font("Arial", Font.BOLD, 18);
        label.setFont(font);
        input.setFont(font);
        insertButton.setFont(font);

        input.setPreferredSize(new Dimension(300, 40));
        insertButton.setPreferredSize(new Dimension(130, 40));

        insertButton.addActionListener(e -> {
            String val = input.getText(); 

            if (val == null || val.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Input tidak boleh kosong!");
                return;
            }
            
            tree.insert(val.trim()); 
            treePanel.repaint();
            input.setText("");

        });

        controlPanel.add(label);
        controlPanel.add(input);
        controlPanel.add(insertButton);

        add(treePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RedBlackTreeVisualizer().setVisible(true));
    }
}