package JavaDictionaryProject;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
// import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Kelas ini mengurus semua logika tampilan Gimmick
public class DictionaryGimmick {
    private static final Color DEFAULT_HEADER_COLOR = new Color(0x2b5288); 
    private static final Color DEFAULT_CONTENT_COLOR = Color.WHITE;
    private final JFrame mainFrame;
    private final JPanel headerPanel;
    private final JPanel contentPanel;
    private final JScrollPane scrollPane;
    private boolean isFlipped = false;
    private JPanel flippedPanelPlaceholder;

    // Konstruktor: Menerima referensi komponen utama dari DictionaryApp
    public DictionaryGimmick(JFrame mainFrame, JPanel headerPanel, JPanel contentPanel, JScrollPane scrollPane) {
        this.mainFrame = mainFrame;
        this.headerPanel = headerPanel;
        this.contentPanel = contentPanel;
        this.scrollPane = scrollPane;
    }

    // --- Metode Inti untuk Eksekusi ---
    public void execute(String actionCode, String word) {
        switch (actionCode) {
            case "HANTU_EFFECT":
                displayImageGimmick("Hantu", word, "Gimmick/Hantu.jpeg");
                break;
            case "FLIP_EFFECT":
                simulateFlipEffect();
                break;
            case "BARREL_ROLL_EFFECT":
                JOptionPane.showMessageDialog(mainFrame, 
                    "**GIMMICK AKTIF!** Efek 'Barrel Roll' dipicu oleh kata: " + word + "\n(Simulasi: Layar akan berputar.)", 
                    "Easter Egg: Barrel Roll", 
                    JOptionPane.INFORMATION_MESSAGE);
                break;
            case "VANISH_EFFECT":
                simulateVanishEffect();
                break;
            case "CALCULATOR_APP":
                showSimpleCalculator(); 
                break;
            case "COLOR_RESET":
                 changeAppBackgroundColor("RESET");
                 break;
            case "COLOR_RED":
            case "COLOR_YELLOW":
            case "COLOR_GREEN":
            case "COLOR_BLUE":
            case "COLOR_BLACK":
            case "COLOR_WHITE":
            case "COLOR_PURPLE":
            case "COLOR_GREY":
            case "COLOR_BROWN":
            case "COLOR_PINK":
                changeAppBackgroundColor(actionCode.split("_")[1]);
                break;
            case "VEHICLE_INFO":
                triggerVehicleGimmick(word);
                break;
            case "HIDUP_EFFECT":
                displayImageGimmick("Hidup", word, "Gimmick/Hidup.gif");
                break;
        }
    }
    
    // --- Logika Gimmick (Dipindahkan dari DictionaryApp) ---

    // GIMMICK 1, 7: Gambar/Video (Hantu, Mobil)
    private void triggerVehicleGimmick(String word) {
        String file = "";
        String lowercaseWord = word.toLowerCase();

        
        if (lowercaseWord.contains("mobil") || lowercaseWord.contains("car")) {
            file = "Gimmick/MobilListrik.gif";
        } else if (lowercaseWord.contains("motor") || lowercaseWord.contains("motorcycle")) {
            file = "Gimmick/MotorListrik.gif";
        }
        
        if (!file.isEmpty()) {
            displayImageGimmick("Info Kendaraan: " + word, word, file);
        } else {
             JOptionPane.showMessageDialog(mainFrame, "Gimmick Kendaraan untuk kata '" + word + "' tidak ditemukan filenya.", "Easter Egg", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayImageGimmick(String title, String word, String imagePath) {
        JDialog imageDialog = new JDialog(mainFrame, "Easter Egg: " + title);
        imageDialog.setModalityType(Dialog.ModalityType.MODELESS);
        imageDialog.setUndecorated(true);

        if (title.equalsIgnoreCase("Hantu")) {
            imageDialog.setSize(1920, 1080); 
        } else {
            imageDialog.setSize(980, 700); 
        }

        imageDialog.setLocationRelativeTo(mainFrame);

        JLabel imageLabel = new JLabel("Memuat Gimmick...", SwingConstants.CENTER);

        try {
            // Menggunakan ClassLoader untuk resource
            URL imageUrl = getClass().getResource(imagePath);

            if (imageUrl == null) {
                throw new IOException("File Gimmick tidak ditemukan di path: " + imagePath);
            }

            if (imagePath.toLowerCase().endsWith(".gif")) {
                ImageIcon gifIcon = new ImageIcon(imageUrl);
                
                imageLabel.setIcon(gifIcon);
                imageLabel.setText(null);
            } else {
                // JPEG (boleh scale)
                BufferedImage image = ImageIO.read(imageUrl);
                Image scaledImage = image.getScaledInstance(
                        imageDialog.getWidth(),
                        imageDialog.getHeight(),
                        Image.SCALE_SMOOTH);

                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setText(null);
            }

        } catch (Exception e) {
            imageLabel.setText("Gimmick " + title + " ERROR: Gambar/Video gagal dimuat!");
            System.err.println("Gagal memuat Gimmick: " + e.getMessage());
        }

        imageDialog.add(imageLabel, BorderLayout.CENTER);
        imageDialog.setVisible(true);

            Timer timer = new Timer(3000, e -> {
            imageDialog.dispose();
            ((Timer)e.getSource()).stop();
        });
        timer.setRepeats(false); 
        timer.start();
    }

    // GIMMICK 4: Hilang (Vanish)
    private void simulateVanishEffect() {
        mainFrame.setVisible(false);
        new Timer(5000, e -> {
            mainFrame.setVisible(true);
            ((Timer)e.getSource()).stop();
        }).start();
    }

    // GIMMICK 5: Kalkulator Sederhana
    private void showSimpleCalculator() {
        // Logika kalkulator yang sudah Anda buat
        JFrame calculatorFrame = new JFrame("Kalkulator Sederhana");
        calculatorFrame.setSize(300, 350);
        calculatorFrame.setLocationRelativeTo(mainFrame);
        
        JTextField display = new JTextField("0");
        // ... (lanjutkan semua kode kalkulator yang sudah Anda buat)
        display.setFont(new Font("Segoe UI", Font.BOLD, 24));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", "=", "+"
        };
        
        // Logika sederhana kalkulator
        ActionListener calcListener = new ActionListener() {
            String operation = "";
            double firstNum = 0;
            boolean newNumber = true;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String command = e.getActionCommand();
                
                if (command.matches("[0-9]")) {
                    if (newNumber) {
                        display.setText(command);
                        newNumber = false;
                    } else {
                        display.setText(display.getText() + command);
                    }
                } else if (command.matches("[+\\-*/]")) {
                    firstNum = Double.parseDouble(display.getText());
                    operation = command;
                    newNumber = true;
                } else if (command.equals("=")) {
                    if (!operation.isEmpty()) {
                        double secondNum = Double.parseDouble(display.getText());
                        double result = calculate(firstNum, secondNum, operation);
                        display.setText(String.valueOf(result));
                        operation = "";
                        newNumber = true;
                    }
                } else if (command.equals("C")) {
                    display.setText("0");
                    firstNum = 0;
                    operation = "";
                    newNumber = true;
                }
            }
            
            private double calculate(double num1, double num2, String op) {
                switch (op) {
                    case "+": return num1 + num2;
                    case "-": return num1 - num2;
                    case "*": return num1 * num2;
                    case "/": return num1 / num2;
                    default: return 0;
                }
            }
        };
        
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 18));
            button.addActionListener(calcListener);
            buttonPanel.add(button);
        }
        
        calculatorFrame.add(display, BorderLayout.NORTH);
        calculatorFrame.add(buttonPanel, BorderLayout.CENTER);
        calculatorFrame.setVisible(true);
    }

    // GIMMICK 2: Flip (Dibalik)
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
                // Kita anggap user ingin menghapus/mengganti kata, jadi kita reset dulu
                execute("COLOR_RESET", "");
                SwingUtilities.invokeLater(() -> {
                });
            }
        });

        contentPane.add(flippedPanelPlaceholder, BorderLayout.CENTER);
        isFlipped = true;
        contentPane.revalidate();
        contentPane.repaint();
        // Paksa fokus ke panel ini agar keyboard terdeteksi
        SwingUtilities.invokeLater(() -> flippedPanelPlaceholder.requestFocusInWindow());
    }
    //     Color originalColor = mainFrame.getContentPane().getBackground();
    //     mainFrame.getContentPane().setBackground(Color.CYAN); 
    //     new Timer(100, e -> {
    //         mainFrame.getContentPane().setBackground(Color.MAGENTA);
    //         new Timer(100, e2 -> {
    //             mainFrame.getContentPane().setBackground(Color.YELLOW);
    //             new Timer(100, e3 -> {
    //                 mainFrame.getContentPane().setBackground(originalColor);
    //                 JOptionPane.showMessageDialog(mainFrame, "**GIMMICK AKTIF!** Efek 'Flip' (simulasi bolak-balik warna) Selesai!", "Easter Egg: Flip", JOptionPane.WARNING_MESSAGE);
    //                 ((Timer)e3.getSource()).stop();
    //             }).start();
    //             ((Timer)e2.getSource()).stop();
    //         }).start();
    //         ((Timer)e.getSource()).stop();
    //     }).start();
    // }
    
    // GIMMICK 6: Warna Latar Belakang
    private void changeAppBackgroundColor(String colorName) {
        Color color;
        Color headerColorToUse = DEFAULT_HEADER_COLOR;

        String hexColor;
        if (colorName.equals("RESET")) {
            color = DEFAULT_CONTENT_COLOR;
            Color headerColor = DEFAULT_HEADER_COLOR;
        if (isFlipped) {
                Container contentPane = mainFrame.getContentPane();
                // Hapus panel gambar terbalik
                if (flippedPanelPlaceholder != null) {
                    contentPane.remove(flippedPanelPlaceholder);
                    flippedPanelPlaceholder = null;
                }

                contentPane.add(headerPanel, BorderLayout.NORTH);
                contentPane.add(scrollPane, BorderLayout.CENTER);

                isFlipped = false;

                contentPane.revalidate();
                contentPane.repaint();
            }

        mainFrame.getContentPane().setBackground(DEFAULT_HEADER_COLOR); // Frame background ikut header
        headerPanel.setBackground(DEFAULT_HEADER_COLOR);
        scrollPane.getViewport().setBackground(DEFAULT_CONTENT_COLOR);
        contentPanel.setBackground(DEFAULT_CONTENT_COLOR);
        
        mainFrame.revalidate();
        mainFrame.repaint();
        return;
        }

        switch (colorName) {
            case "RED":
                color = new Color(255, 100, 100); break;
            case "YELLOW":
                color = new Color(255, 255, 150); break; // Sedikit lebih soft
            case "GREEN":
                color = new Color(100, 255, 100); break;
            case "BLUE":
                color = new Color(100, 200, 255); break; // Biru muda (beda dengan header)
            case "BLACK":
                color = new Color(50, 50, 50); break; // Abu gelap (biar teks hitam masih terbaca dikit)
            case "WHITE":
                color = Color.WHITE; break;
            case "PURPLE":
                color = new Color(200, 100, 255); break;
            case "GREY":
                color = new Color(200, 200, 200); break;
            case "BROWN":
                color = new Color(165, 42, 42); break;
            case "PINK":
                color = new Color(255, 182, 193); break;
            default: return;
        }

        // Mengatur warna di semua komponen
        headerPanel.setBackground(color); 
        mainFrame.getContentPane().setBackground(color);
        scrollPane.getViewport().setBackground(color);
        contentPanel.setBackground(color);
        
        mainFrame.revalidate();
        mainFrame.repaint();
        
    }
}