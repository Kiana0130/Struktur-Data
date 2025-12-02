package JavaDictionaryProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DictionaryApp extends JFrame {
    private final DictionaryManager dictionaryManager;
    private JTextField searchField;
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private JButton idButton;
    private JButton engButton;

    private String currentLanguage = "ID";

    public DictionaryApp() {
        super("Kamus Indonesia-Inggris");
        dictionaryManager = new DictionaryManager();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Style & Fonts ---
        Font searchFieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // --- Panel Header (Top) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240)); 
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Search Field Setting
        searchField = new JTextField("Pencarian Teks", 25);
        searchField.setFont(searchFieldFont);
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(300, 35));
        
        // Atur padding kiri saja untuk teks
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); 

        // Logika Focus Listener yang memastikan warna teks terlihat
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
             public void focusGained(java.awt.event.FocusEvent evt) {
                 if (searchField.getText().equals("Pencarian Teks")) {
                     searchField.setText("");
                     searchField.setForeground(Color.BLACK); // Warna teks saat diketik
                 }
             }
             public void focusLost(java.awt.event.FocusEvent evt) {
                 if (searchField.getText().isEmpty()) {
                     searchField.setText("Pencarian Teks");
                     searchField.setForeground(Color.GRAY); // Warna teks placeholder
                 }
             }
         });
        
        // Search Icon Button
        JButton searchIcon = new JButton(new ImageIcon(createSearchIcon(16, 16)));
        searchIcon.setBorderPainted(false);
        searchIcon.setContentAreaFilled(false);
        searchIcon.setFocusPainted(false);
        searchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Search Input Wrapper (BorderLayout untuk menempatkan Field dan Icon)
        JPanel searchInputWrapper = new JPanel(new BorderLayout());
        searchInputWrapper.setBackground(Color.WHITE);
        
        // Menggunakan border standar tebal 1 untuk tampilan yang stabil
        searchInputWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1)); 
        
        // Ikon Wrapper untuk Padding
        JPanel iconWrapper = new JPanel(new BorderLayout());
        iconWrapper.setBackground(Color.WHITE);
        iconWrapper.add(searchIcon, BorderLayout.CENTER);
        iconWrapper.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10)); 
        iconWrapper.setPreferredSize(new Dimension(35, 35));

        searchInputWrapper.add(searchField, BorderLayout.CENTER);
        searchInputWrapper.add(iconWrapper, BorderLayout.EAST);
        searchInputWrapper.setPreferredSize(new Dimension(350, 35));

        // Panel Kontainer untuk Search
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftHeaderPanel.setOpaque(false);
        leftHeaderPanel.add(searchInputWrapper);

        // Language Buttons (ID / Eng)
        JPanel langButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        langButtonPanel.setOpaque(false);
        
        idButton = new JButton("ID");
        engButton = new JButton("Eng");
        idButton.setFont(buttonFont);
        engButton.setFont(buttonFont);
        
        Color selectedBtnBg = new Color(150, 190, 255); 
        Color defaultBtnBg = new Color(220, 220, 220);  
        Color buttonTextColor = Color.BLACK;
        
        // 🚨 Perbaikan: Padding dan Border Tombol (menggunakan EmptyBorder untuk Padding, LineBorder untuk Garis)
        int btnPaddingV = 10;
        int btnPaddingH = 20;

        idButton.setBackground(selectedBtnBg);
        idButton.setForeground(buttonTextColor);
        idButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1), // Border Luar
            BorderFactory.createEmptyBorder(btnPaddingV, btnPaddingH, btnPaddingV, btnPaddingH) // Padding
        ));
        idButton.setFocusPainted(false);

        engButton.setBackground(defaultBtnBg);
        engButton.setForeground(buttonTextColor);
        engButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(btnPaddingV, btnPaddingH, btnPaddingV, btnPaddingH)
        ));
        engButton.setFocusPainted(false);

        langButtonPanel.add(idButton);
        langButtonPanel.add(engButton);

        headerPanel.add(leftHeaderPanel, BorderLayout.CENTER);
        headerPanel.add(langButtonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Content Panel (CENTER) ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        
        styleScrollBar(scrollPane.getVerticalScrollBar()); 

        add(scrollPane, BorderLayout.CENTER);

        // --- Action Listeners ---
        ActionListener generalSearchAction = e -> filterAndDisplayWords(searchField.getText());
        searchField.addActionListener(generalSearchAction);
        searchIcon.addActionListener(generalSearchAction);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }

            private void performSearch() {
                String text = searchField.getText();
                // Cek agar Placeholder "Pencarian Teks" tidak dianggap sebagai kata kunci pencarian
                if (text.equals("Pencarian Teks") && searchField.getForeground() == Color.GRAY) {
                    filterAndDisplayWords("");
                } else {
                    filterAndDisplayWords(text);
                }
            }
        });

        // Listener tombol ID
        idButton.addActionListener(e -> {
            currentLanguage = "ID";
            // Update styling
            idButton.setBackground(selectedBtnBg);
            idButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(selectedBtnBg.darker(), 2), BorderFactory.createEmptyBorder(btnPaddingV, btnPaddingH, btnPaddingV, btnPaddingH)));
            engButton.setBackground(defaultBtnBg);
            engButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1), BorderFactory.createEmptyBorder(btnPaddingV, btnPaddingH, btnPaddingV, btnPaddingH)));
            filterAndDisplayWords(searchField.getText().equals("Pencarian Teks") ? "" : searchField.getText());
        });

        // Listener tombol Eng
        engButton.addActionListener(e -> {
            currentLanguage = "Eng";
            // Update styling
            engButton.setBackground(selectedBtnBg);
            engButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(selectedBtnBg.darker(), 2), BorderFactory.createEmptyBorder(btnPaddingV, btnPaddingH, btnPaddingV, btnPaddingH)));
            idButton.setBackground(defaultBtnBg);
            idButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1), BorderFactory.createEmptyBorder(btnPaddingV, btnPaddingH, btnPaddingV, btnPaddingH)));
            filterAndDisplayWords(searchField.getText().equals("Pencarian Teks") ? "" : searchField.getText());
        });

        filterAndDisplayWords(""); 
        setVisible(true);
    }
    
    // Metode untuk membuat ikon kaca pembesar sederhana
    private Image createSearchIcon(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(100, 100, 100));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        
        g2.drawOval(1, 1, width - 6, height - 6);
        g2.drawLine(width - 5, height - 5, width - 1, height - 1);
        
        g2.dispose();
        return img;
    }
    
    // Metode untuk membuat ScrollBar minimalis dan cepat
    private void styleScrollBar(JScrollBar bar) {
        bar.setPreferredSize(new Dimension(8, bar.getPreferredSize().height));
        bar.setUnitIncrement(50); 

        bar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(150, 150, 150);
                this.trackColor = Color.WHITE;
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroSizeButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroSizeButton();
            }
            
            private JButton createZeroSizeButton() {
                JButton button = new JButton();
                Dimension zeroDim = new Dimension(0, 0);
                button.setPreferredSize(zeroDim);
                button.setMinimumSize(zeroDim);
                button.setMaximumSize(zeroDim);
                return button;
            }
        });
    }

    private void filterAndDisplayWords(String query) {
        contentPanel.removeAll(); 

        List<DictionaryEntry> allEntries = dictionaryManager.getAllSortedEntries();
        
        if (currentLanguage.equals("Eng")) {
            allEntries = allEntries.stream()
                .sorted((e1, e2) -> e1.getEngWord().compareToIgnoreCase(e2.getEngWord()))
                .collect(Collectors.toList());
        }
        
        Map<Character, List<DictionaryEntry>> groupedEntries = new LinkedHashMap<>();

        for (DictionaryEntry entry : allEntries) {
            String targetWord = currentLanguage.equals("ID") ? entry.getIndoWord() : entry.getEngWord();
            
            if (!query.isEmpty() && !targetWord.toLowerCase().contains(query.toLowerCase())) {
                continue;
            }

            if (!targetWord.isEmpty()) {
                char firstChar = Character.toUpperCase(targetWord.charAt(0));
                groupedEntries.computeIfAbsent(firstChar, k -> new ArrayList<>()).add(entry);
            }
        }
        
        for (Map.Entry<Character, List<DictionaryEntry>> entryGroup : groupedEntries.entrySet()) {
            JLabel alphaHeader = new JLabel(String.valueOf(entryGroup.getKey()));
            alphaHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
            alphaHeader.setBorder(new EmptyBorder(15, 0, 5, 0));
            alphaHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(alphaHeader);
            
            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setForeground(new Color(200, 200, 200));
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            separator.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(separator);
            contentPanel.add(Box.createVerticalStrut(10));

            for (DictionaryEntry entry : entryGroup.getValue()) {
                contentPanel.add(createWordCard(entry));
                contentPanel.add(Box.createVerticalStrut(8));
            }
            contentPanel.add(Box.createVerticalStrut(20));
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createWordCard(DictionaryEntry entry) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel primaryWordLabel;
        JLabel secondaryWordLabel;

        if (currentLanguage.equals("ID")) {
            primaryWordLabel = new JLabel(entry.getIndoWord());
            secondaryWordLabel = new JLabel(entry.getEngWord());
            primaryWordLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            secondaryWordLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        } else {
            primaryWordLabel = new JLabel(entry.getEngWord());
            secondaryWordLabel = new JLabel(entry.getIndoWord());
            primaryWordLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            secondaryWordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        
        primaryWordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(primaryWordLabel);

        secondaryWordLabel.setForeground(Color.GRAY);
        secondaryWordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(secondaryWordLabel);

        card.add(Box.createVerticalStrut(5));

        JLabel exampleLabel = new JLabel("contoh : " + entry.getExample());
        exampleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        exampleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(exampleLabel);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(230, 230, 230));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 245));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return card;
    }

    // Kelas kustom untuk membuat border membulat (tetap dipertahankan untuk styling tombol dan search bar)
    static class RoundedBorder implements javax.swing.border.Border {
        private final int radius;
        private final Color color;
        private final int thickness;
        private final Insets insets;

        RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
            this.insets = new Insets(radius + thickness, radius + thickness, radius + thickness, radius + thickness);
        }

        RoundedBorder(Color color, int thickness, int radius, int top, int left, int bottom, int right) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
            this.insets = new Insets(top, left, bottom, right);
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            // Menggunakan Look and Feel Sistem Operasi untuk stabilitas.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback
        }
        
        SwingUtilities.invokeLater(DictionaryApp::new);
    }
}