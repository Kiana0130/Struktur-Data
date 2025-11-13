import java.awt.*;
import javax.swing.*;

// ======================================
// Node class for Red-Black Tree
// ======================================
class Node {
    String data; // <--- PERUBAHAN: Tipe data diubah ke String
    Node left, right, parent;
    boolean color; // true = RED, false = BLACK

    Node(String data) { // <--- PERUBAHAN: Constructor menggunakan String
        this.data = data;
        color = true; // New nodes are always RED
    }
}

// ======================================
// Red-Black Tree Implementation
// ======================================
class RedBlackTree {
    private Node root;
    private final boolean RED = true;
    private final boolean BLACK = false;

    public Node getRoot() {
        return root;
    }

    // Left Rotate (Tidak ada perubahan)
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

    // Right Rotate (Tidak ada perubahan)
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

    // Insert node
    public void insert(String data) { // <--- PERUBAHAN: Tipe data parameter
        Node newNode = new Node(data);
        root = bstInsert(root, newNode);
        fixViolation(newNode);
    }

    // Standard BST insert
    private Node bstInsert(Node root, Node pt) {
        if (root == null)
            return pt;

        // <--- PERUBAHAN: Logika perbandingan menggunakan compareTo untuk String
        if (pt.data.compareTo(root.data) < 0) {
            root.left = bstInsert(root.left, pt);
            root.left.parent = root;
        } else if (pt.data.compareTo(root.data) > 0) {
            root.right = bstInsert(root.right, pt);
            root.right.parent = root;
        }
        // Jika data sama (compareTo == 0), tidak di-insert (menghindari duplikat)
        return root;
    }

    // Fix Red-Black Tree Violations (Tidak ada perubahan)
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

// ======================================
// Visualizer Panel
// ======================================
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

        // Draw left branch
        if (node.left != null) {
            g.setColor(Color.BLACK); // <--- PERUBAHAN: Menetapkan warna garis
            g.drawLine(x, y, x - xOffset, y + 80);
            drawTree(g, node.left, x - xOffset, y + 80, xOffset / 2);
        }

        // Draw right branch
        if (node.right != null) {
            g.setColor(Color.BLACK); // <--- PERUBAHAN: Menetapkan warna garis
            g.drawLine(x, y, x + xOffset, y + 80);
            drawTree(g, node.right, x + xOffset, y + 80, xOffset / 2);
        }

        // Draw node circle
        g.setColor(node.color ? Color.RED : Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);

        // Node border
        g.setColor(Color.WHITE);
        g.drawOval(x - 15, y - 15, 30, 30);

        // Draw text (value)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String val = String.valueOf(node.data); // Ini sudah otomatis menangani String
        g.drawString(val, x - g.getFontMetrics().stringWidth(val) / 2, y + 5);
    }
}

// ======================================
// Main Frame
// ======================================
public class RedBlackTreeVisualizer extends JFrame {
    private final RedBlackTree tree = new RedBlackTree();
    private final TreePanel treePanel = new TreePanel(tree);

    public RedBlackTreeVisualizer() {
        setTitle("Red-Black Tree Visualizer");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- PERUBAHAN BESAR PADA UI CONTROL PANEL ---

        // 1. Buat Panel Kontrol dengan FlowLayout
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        controlPanel.setPreferredSize(new Dimension(900, 70));
        controlPanel.setBackground(new Color(245, 245, 245));

        // 2. Siapkan Komponen
        JLabel label = new JLabel("Masukkan data:"); // <-- PERUBAHAN: Teks label
        JTextField input = new JTextField();
        JButton insertButton = new JButton("Insert");

        // 3. Atur Font dan Ukuran yang Diinginkan (PreferredSize)
        Font font = new Font("Arial", Font.BOLD, 18);
        label.setFont(font);
        input.setFont(font);
        insertButton.setFont(font);

        // Gunakan setPreferredSize, bukan setBounds
        input.setPreferredSize(new Dimension(300, 40));
        insertButton.setPreferredSize(new Dimension(130, 40));

        // 4. Tombol aksi
        insertButton.addActionListener(e -> {
            // <--- PERUBAHAN: Logika untuk menangani String
            String val = input.getText(); 

            // Validasi input agar tidak kosong
            if (val == null || val.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Input tidak boleh kosong!");
                return;
            }
            
            // Tidak perlu Integer.parseInt() lagi
            tree.insert(val.trim()); // Langsung insert String (trim untuk hapus spasi)
            treePanel.repaint();
            input.setText("");

            // Hapus try-catch karena tidak ada lagi NumberFormatException
        });

        // 5. Tambahkan komponen ke controlPanel
        controlPanel.add(label);
        controlPanel.add(input);
        controlPanel.add(insertButton);

        // 6. Tambahkan ke frame
        add(treePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RedBlackTreeVisualizer().setVisible(true));
    }
}