package JavaDictionaryProject;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
// PERBAIKAN: Import yang benar untuk VLCJ 4 ada di sini (tanpa .events)
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Kelas ini mengurus semua logika tampilan Gimmick
public class DictionaryGimmick {

    // --- HAPUS STATIC BLOCK JAVAFX (TIDAK DIPERLUKAN) ---

    private static final Color DEFAULT_HEADER_COLOR = new Color(240, 240, 240);
    private static final Color DEFAULT_CONTENT_COLOR = Color.WHITE;
    private final JFrame mainFrame;
    private final JPanel headerPanel;
    private final JPanel contentPanel;
    private final JScrollPane scrollPane;

    // Konstruktor
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
            case "COLOR_RED":
            case "COLOR_YELLOW":
            case "COLOR_GREEN":
            case "COLOR_BLUE":
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

    // --- Logika Gimmick ---

    private void triggerVehicleGimmick(String word) {
        String file = "";
        String lowercaseWord = word.toLowerCase();

        if (lowercaseWord.contains("mobil") || lowercaseWord.contains("car")) {
            file = "Gimmick/MobilListrik.mp4";
        } else if (lowercaseWord.contains("motor") || lowercaseWord.contains("motorcycle")) {
            file = "Gimmick/MotorListrik.mp4";
        } else if (lowercaseWord.contains("hidup") || lowercaseWord.contains("life")) {
            file = "Gimmick/Hidup.mp4";
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
            // --- PERBAIKAN LOGIKA VIDEO ---
            // VLC butuh Absolute Path (alamat lengkap file di harddisk)
            if (imagePath.toLowerCase().endsWith(".mp4")) {
                File videoFile = new File(imagePath);

                // Cek 1: Apakah file ada di root folder project?
                if (!videoFile.exists()) {
                    // Cek 2: Coba cari di folder src/ (untuk struktur project Netbeans/IntelliJ standar)
                    videoFile = new File("src/" + imagePath);
                }

                // Cek 3: Coba cari di folder resources (src/main/resources untuk Maven)
                if (!videoFile.exists()) {
                    videoFile = new File("src/main/resources/" + imagePath);
                }

                if (videoFile.exists()) {
                    // Mainkan menggunakan Absolute Path
                    playVideo(videoFile.getAbsolutePath());
                    return;
                } else {
                    System.err.println("File video tidak ditemukan di: " + videoFile.getAbsolutePath());
                    imageLabel.setText("File Video Hilang: " + imagePath);
                }
            }
            // ------------------------------

            // Logika Gambar (tetap menggunakan Resource URL agar bisa masuk JAR)
            URL imageUrl = getClass().getResource(imagePath);

            // Fallback manual jika getResource gagal (saat run via IDE kadang path beda)
            if (imageUrl == null) {
                File f = new File("src/" + imagePath);
                if (f.exists()) {
                    imageUrl = f.toURI().toURL();
                }
            }

            if (imageUrl == null) {
                throw new IOException("File Gambar tidak ditemukan: " + imagePath);
            }

            if (imagePath.toLowerCase().endsWith(".gif")) {
                ImageIcon gifIcon = new ImageIcon(imageUrl);
                imageLabel.setIcon(gifIcon);
                imageLabel.setText(null);
            } else {
                BufferedImage img = ImageIO.read(imageUrl);
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(imageDialog.getWidth(), imageDialog.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImg));
                    imageLabel.setText(null);
                }
            }
        } catch (Exception e) {
            imageLabel.setText("Gimmick " + title + " ERROR!");
            System.err.println("Gagal memuat Gimmick: " + e.getMessage());
            e.printStackTrace();
        }

        imageDialog.add(imageLabel, BorderLayout.CENTER);
        imageDialog.setVisible(true);

        // Timer untuk menutup dialog otomatis
        Timer timer = new Timer(3000, e -> {
            imageDialog.dispose();
            ((Timer)e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void playVideo(String videoPath) {
        JFrame videoFrame = new JFrame("Memutar Video");
        videoFrame.setSize(980, 700);
        videoFrame.setLocationRelativeTo(mainFrame);

        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        videoFrame.setContentPane(mediaPlayerComponent);

        videoFrame.setVisible(true);

        MediaPlayer mediaPlayer = mediaPlayerComponent.mediaPlayer();
        mediaPlayer.media().play(videoPath);

        // Tutup frame otomatis setelah video selesai
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                // Saat video selesai, stop dan tutup window
                mediaPlayer.controls().stop();
                videoFrame.dispose();
            }
        });

        // Hentikan video jika window ditutup manual
        videoFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mediaPlayer.controls().stop();
                mediaPlayer.release();
            }
        });
    }

    private void simulateVanishEffect() {
        mainFrame.setVisible(false);
        new Timer(5000, e -> {
            mainFrame.setVisible(true);
            ((Timer)e.getSource()).stop();
        }).start();
    }

    private void showSimpleCalculator() {
        JFrame calculatorFrame = new JFrame("Kalkulator Sederhana");
        calculatorFrame.setSize(300, 350);
        calculatorFrame.setLocationRelativeTo(mainFrame);

        JTextField display = new JTextField("0");
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

    private void simulateFlipEffect() {
        Color originalColor = mainFrame.getContentPane().getBackground();
        mainFrame.getContentPane().setBackground(Color.CYAN);
        new Timer(100, e -> {
            mainFrame.getContentPane().setBackground(Color.MAGENTA);
            new Timer(100, e2 -> {
                mainFrame.getContentPane().setBackground(Color.YELLOW);
                new Timer(100, e3 -> {
                    mainFrame.getContentPane().setBackground(originalColor);
                    JOptionPane.showMessageDialog(mainFrame, "**GIMMICK AKTIF!** Efek 'Flip' (simulasi bolak-balik warna) Selesai!", "Easter Egg: Flip", JOptionPane.WARNING_MESSAGE);
                    ((Timer)e3.getSource()).stop();
                }).start();
                ((Timer)e2.getSource()).stop();
            }).start();
            ((Timer)e.getSource()).stop();
        }).start();
    }

    private void changeAppBackgroundColor(String colorName) {
        Color color;

        if (colorName.equals("RESET")) {
            Color headerColor = DEFAULT_HEADER_COLOR;
            mainFrame.getContentPane().setBackground(headerColor);
            headerPanel.setBackground(headerColor);
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
                color = new Color(255, 255, 100); break;
            case "GREEN":
                color = new Color(100, 255, 100); break;
            case "BLUE":
                color = new Color(100, 100, 255); break;
            default: return;
        }

        mainFrame.getContentPane().setBackground(color);
        headerPanel.setBackground(color);
        scrollPane.getViewport().setBackground(color);
        contentPanel.setBackground(color);

        mainFrame.revalidate();
        mainFrame.repaint();
    }
}