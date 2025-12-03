package JavaDictionaryProject;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DictionaryGimmick {

    @FunctionalInterface
    private interface GimmickAction {
        void execute(String word);
    }

    private static final Color DEFAULT_HEADER_COLOR = new Color(0x2b5288);
    private static final Color DEFAULT_CONTENT_COLOR = Color.WHITE;

    private final JFrame mainFrame;
    private final JPanel headerPanel;
    private final JPanel contentPanel;
    private final JScrollPane scrollPane;

    private final Map<String, GimmickAction> gimmickRegistry = new HashMap<>();

    private boolean isFlipped = false;
    private JPanel flippedPanelPlaceholder;

    public DictionaryGimmick(JFrame mainFrame, JPanel headerPanel, JPanel contentPanel, JScrollPane scrollPane) {
        this.mainFrame = mainFrame;
        this.headerPanel = headerPanel;
        this.contentPanel = contentPanel;
        this.scrollPane = scrollPane;

        registerGimmicks();   
    }

    private void registerGimmicks() {

        gimmickRegistry.put("HANTU_EFFECT", word ->
                displayImageGimmick("Hantu", word, "Gimmick/Hantu.jpeg"));

        gimmickRegistry.put("FLIP_EFFECT", word ->
                simulateFlipEffect());

        gimmickRegistry.put("BARREL_ROLL_EFFECT", word ->
                JOptionPane.showMessageDialog(mainFrame,
                        "**GIMMICK AKTIF!** Efek 'Barrel Roll' dipicu oleh kata: "
                                + word, "Easter Egg: Barrel Roll",
                        JOptionPane.INFORMATION_MESSAGE));

        gimmickRegistry.put("VANISH_EFFECT", word ->
                simulateVanishEffect());

        gimmickRegistry.put("CALCULATOR_APP", word ->
                showSimpleCalculator());

        gimmickRegistry.put("COLOR_RESET", word ->
                changeAppBackgroundColor("RESET"));

        registerColor("RED");
        registerColor("YELLOW");
        registerColor("GREEN");
        registerColor("BLUE");
        registerColor("BLACK");
        registerColor("WHITE");
        registerColor("PURPLE");
        registerColor("GREY");
        registerColor("BROWN");
        registerColor("PINK");

        gimmickRegistry.put("HIDUP_EFFECT", word ->
                displayImageGimmick("Hidup", word, "Gimmick/HidupJ.gif"));

        gimmickRegistry.put("VEHICLE_INFO", this::triggerVehicleGimmick);
    }

    private void registerColor(String color) {
        gimmickRegistry.put("COLOR_" + color, word ->
                changeAppBackgroundColor(color));
    }

    public void execute(String actionCode, String word) {
        GimmickAction action = gimmickRegistry.get(actionCode);

        if (action != null) { 
            action.execute(word);
        }
    }

    private void displayImageGimmick(String title, String word, String imagePath) {
        JDialog imageDialog = new JDialog(mainFrame, "Easter Egg: " + title);
        imageDialog.setModalityType(Dialog.ModalityType.MODELESS);
        imageDialog.setUndecorated(true);

        imageDialog.setSize(title.equalsIgnoreCase("Hantu") ? 1920 : 980,
                title.equalsIgnoreCase("Hantu") ? 1080 : 700);

        imageDialog.setLocationRelativeTo(mainFrame);
        JLabel imageLabel = new JLabel("Memuat Gimmick...", SwingConstants.CENTER);

        try {
            URL imageUrl = getClass().getResource(imagePath);

            if (imageUrl == null) throw new IOException("File Gimmick tidak ditemukan: " + imagePath);

            if (imagePath.toLowerCase().endsWith(".gif")) {
                imageLabel.setIcon(new ImageIcon(imageUrl));
            } else {
                BufferedImage image = ImageIO.read(imageUrl);
                Image scaledImage = image.getScaledInstance(
                        imageDialog.getWidth(),
                        imageDialog.getHeight(),
                        Image.SCALE_SMOOTH);

                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
            imageLabel.setText(null);

        } catch (Exception e) {
            imageLabel.setText("Gimmick ERROR: Gambar gagal dimuat!");
        }

        imageDialog.add(imageLabel, BorderLayout.CENTER);
        imageDialog.setVisible(true);

        new Timer(4000, e -> imageDialog.dispose()).start();
    }


    private void simulateVanishEffect() {
        mainFrame.setVisible(false);
        new Timer(4000, e -> mainFrame.setVisible(true)).start();
    }

    private void showSimpleCalculator() {
        JFrame calculatorFrame = new JFrame("Kalkulator Sederhana");
        calculatorFrame.setSize(300, 350);
        calculatorFrame.setLocationRelativeTo(mainFrame);

        JTextField display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.BOLD, 24));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", "=", "+"
        };

        ActionListener calcListener = new ActionListener() {
            String op = "";
            double first = 0;
            boolean fresh = true;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                String cmd = e.getActionCommand();

                if (cmd.matches("[0-9]")) {
                    display.setText(fresh ? cmd : display.getText() + cmd);
                    fresh = false;
                    return;
                }

                if (cmd.matches("[+\\-*/]")) {
                    first = Double.parseDouble(display.getText());
                    op = cmd;
                    fresh = true;
                    return;
                }

                if (cmd.equals("=")) {
                    double result = switch (op) {
                        case "+" -> first + Double.parseDouble(display.getText());
                        case "-" -> first - Double.parseDouble(display.getText());
                        case "*" -> first * Double.parseDouble(display.getText());
                        case "/" -> first / Double.parseDouble(display.getText());
                        default -> 0;
                    };
                    display.setText(String.valueOf(result));
                    op = "";
                    fresh = true;
                    return;
                }

                if (cmd.equals("C")) {
                    display.setText("0");
                    op = "";
                    fresh = true;
                }
            }
        };

        for (String b : buttons) {
            JButton btn = new JButton(b);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btn.addActionListener(calcListener);
            buttonPanel.add(btn);
        }

        calculatorFrame.add(display, BorderLayout.NORTH);
        calculatorFrame.add(buttonPanel, BorderLayout.CENTER);
        calculatorFrame.setVisible(true);
    }

    private void simulateFlipEffect() {
        if (isFlipped) return;

    Container contentPane = mainFrame.getContentPane();
        BufferedImage image = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        contentPane.print(g2d);
        g2d.dispose();

        contentPane.removeAll();

        flippedPanelPlaceholder = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.rotate(Math.PI, getWidth() / 2.0, getHeight() / 2.0);
                g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                g2.dispose();
            }
        };
        flippedPanelPlaceholder.setBackground(new Color(30, 30, 30));

        flippedPanelPlaceholder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                execute("COLOR_RESET", ""); // Panggil fungsi reset
            }
        });

        flippedPanelPlaceholder.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                execute("COLOR_RESET", "");
                SwingUtilities.invokeLater(() -> {
                });
            }
        });

        contentPane.add(flippedPanelPlaceholder, BorderLayout.CENTER);
        isFlipped = true;
        contentPane.revalidate();
        contentPane.repaint();
        SwingUtilities.invokeLater(() -> flippedPanelPlaceholder.requestFocusInWindow());
    }

    private void triggerVehicleGimmick(String word) {
        Map<String, String> files = Map.of(
                "mobil", "Gimmick/MobilListrik.gif",
                "car", "Gimmick/MobilListrik.gif",
                "motor", "Gimmick/MotorListrik.gif",
                "motorcycle", "Gimmick/MotorListrik.gif"
        );

        String key = files.keySet().stream()
                .filter(word.toLowerCase()::contains)
                .findFirst()
                .orElse(null);

        if (key != null) {
            displayImageGimmick("Kendaraan", word, files.get(key));
        }
    }


    /** Warna */
    private void changeAppBackgroundColor(String colorName) {

        if (colorName.equals("RESET")) {
            headerPanel.setBackground(DEFAULT_HEADER_COLOR);
            scrollPane.getViewport().setBackground(DEFAULT_CONTENT_COLOR);
            contentPanel.setBackground(DEFAULT_CONTENT_COLOR);
            mainFrame.getContentPane().setBackground(DEFAULT_HEADER_COLOR);
            return;
        }

        Color color = switch (colorName) {
            case "RED" -> new Color(255, 100, 100);
            case "YELLOW" -> new Color(255, 255, 150);
            case "GREEN" -> new Color(100, 255, 100);
            case "BLUE" -> new Color(100, 200, 255);
            case "BLACK" -> new Color(50, 50, 50);
            case "WHITE" -> Color.WHITE;
            case "PURPLE" -> new Color(200, 100, 255);
            case "GREY" -> new Color(200, 200, 200);
            case "BROWN" -> new Color(165, 42, 42);
            case "PINK" -> new Color(255, 182, 193);
            default -> DEFAULT_CONTENT_COLOR;
        };

        headerPanel.setBackground(color);
        scrollPane.getViewport().setBackground(color);
        contentPanel.setBackground(color);
        mainFrame.getContentPane().setBackground(color);
    }
}
