package JavaDictionaryProject;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.geom.RoundRectangle2D;

public class DictionaryApp extends JFrame {
    private final DictionaryManager dictionaryManager;
    private JTextField searchField;
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private GradientButton idButton;
    private GradientButton engButton;
    private JPanel headerPanel;
    private DictionaryGimmick dictionaryGimmick;
    private String currentLanguage = "ID";

    public DictionaryApp() {
        super("Kamus Indonesia-Inggris");
        dictionaryManager = new DictionaryManager();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Fonts & Style ---
        Font searchFieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // --- Header Panel ---
        headerPanel = new JPanel(new BorderLayout()); 
        Color themeBlue = new Color(0x2b5288);
        headerPanel.setBackground(themeBlue);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Search Field
        searchField = new JTextField("Pencarian Teks", 25);
        searchField.setFont(searchFieldFont);
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); 

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Pencarian Teks")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Pencarian Teks");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        JButton searchIcon = new JButton(new ImageIcon(createSearchIcon(16, 16)));
        searchIcon.setBorderPainted(false);
        searchIcon.setContentAreaFilled(false);
        searchIcon.setFocusPainted(false);
        searchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel searchInputWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                // Menggambar kotak putih dengan sudut melengkung (radius 30)
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        searchInputWrapper.setOpaque(false); // Agar sudut transparan terlihat background biru header
        searchInputWrapper.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 10)); // Padding dalam
        searchInputWrapper.add(searchField, BorderLayout.CENTER);
        searchInputWrapper.add(searchIcon, BorderLayout.EAST);
        searchInputWrapper.setPreferredSize(new Dimension(400, 45));

        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftHeaderPanel.setOpaque(false);
        leftHeaderPanel.add(searchInputWrapper);

        // Language Buttons
        JPanel langButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Jarak antar tombol 10
        langButtonPanel.setOpaque(false);
        
        idButton = new GradientButton("ID", themeBlue);
        engButton = new GradientButton("Eng", themeBlue);
        idButton.setFont(buttonFont);
        engButton.setFont(buttonFont);

        langButtonPanel.add(idButton);
        langButtonPanel.add(engButton);

        idButton.setActive(true);
        engButton.setActive(false);

        headerPanel.add(leftHeaderPanel, BorderLayout.CENTER);
        headerPanel.add(langButtonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Content Panel ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        styleScrollBar(scrollPane.getVerticalScrollBar()); 

        add(scrollPane, BorderLayout.CENTER);

        // --- Listeners ---
        ActionListener generalSearchAction = e -> performGlobalSearch();
        
        searchField.addActionListener(generalSearchAction);
        searchIcon.addActionListener(generalSearchAction);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performGlobalSearch(); }
            public void removeUpdate(DocumentEvent e) { performGlobalSearch(); }
            public void changedUpdate(DocumentEvent e) { performGlobalSearch(); }
        });

        idButton.addActionListener(e -> {
            currentLanguage = "ID";
            idButton.setActive(true);
            engButton.setActive(false);
            performGlobalSearch();
        });

        engButton.addActionListener(e -> {
            currentLanguage = "Eng";
            engButton.setActive(true);
            idButton.setActive(false);
            performGlobalSearch();
        });

        filterAndDisplayWords(""); 
        dictionaryGimmick = new DictionaryGimmick(this, headerPanel, contentPanel, scrollPane);
        setVisible(true);
    }
    
    private void performGlobalSearch() {
        String text = searchField.getText();
        if (text.equals("Pencarian Teks") || text.trim().isEmpty()) {
            filterAndDisplayWords(""); 
        } else {
            filterAndDisplayWords(text);
        }
    }

    private void filterAndDisplayWords(String query) {
        String cleanQuery = query.trim().toLowerCase();
        
        // --- LOGIKA GIMMICK YANG DIREVISI ---
        String gimmickAction = null;
        if (!cleanQuery.isEmpty()) {
            gimmickAction = dictionaryManager.getGimmickAction(cleanQuery);
        }

        if (gimmickAction != null) {
            // 1. Trigger the gimmick if found (e.g., "merah")
            triggerGimmick(gimmickAction, query.trim());
        } else {
            // 2. Reset the color/gimmick if not found, or if query is empty
            if (dictionaryGimmick != null) {
                dictionaryGimmick.execute("COLOR_RESET", "");
            }
        }

        contentPanel.removeAll(); 
        
        
        List<DictionaryEntry> allEntries = dictionaryManager.getAllSortedEntries();

        if (currentLanguage.equals("ID")) {
            
            allEntries.sort((e1, e2) -> e1.getIndoWord().compareToIgnoreCase(e2.getIndoWord()));

            Map<String, List<DictionaryEntry>> groupedData = new LinkedHashMap<>();

            for (DictionaryEntry entry : allEntries) {
                if (!query.isEmpty() && !entry.getIndoWord().toLowerCase().contains(cleanQuery)) {
                    continue;
                }
                groupedData.computeIfAbsent(entry.getIndoWord(), k -> new ArrayList<>()).add(entry);
            }

            char lastChar = '\0';
            for (Map.Entry<String, List<DictionaryEntry>> group : groupedData.entrySet()) {
                String indoWord = group.getKey();
                List<DictionaryEntry> meanings = group.getValue();
                
                char currentChar = Character.toUpperCase(indoWord.charAt(0));
                if (currentChar != lastChar) {
                    addHeaderLabel(currentChar);
                    lastChar = currentChar;
                }

                contentPanel.add(createMergedCard(meanings));
                contentPanel.add(Box.createVerticalStrut(10));
            }

        } else {
            // --- MODE ENG (SINGLE) ---
            allEntries.sort((e1, e2) -> e1.getEngWord().compareToIgnoreCase(e2.getEngWord()));
            
            char lastChar = '\0';
            for (DictionaryEntry entry : allEntries) {
                if (!query.isEmpty() && !entry.getEngWord().toLowerCase().contains(cleanQuery)) {
                    continue;
                }

                char currentChar = Character.toUpperCase(entry.getEngWord().charAt(0));
                if (currentChar != lastChar) {
                    addHeaderLabel(currentChar);
                    lastChar = currentChar;
                }

                contentPanel.add(createSingleCard(entry));
                contentPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void addHeaderLabel(char letter) {
        JLabel alphaHeader = new JLabel(String.valueOf(letter));
        alphaHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        alphaHeader.setForeground(new Color(50, 50, 50));
        alphaHeader.setBorder(new EmptyBorder(15, 0, 5, 0));
        alphaHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(alphaHeader);
        
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(220, 220, 220));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(10));
    }

    private JPanel createSingleCard(DictionaryEntry entry) {
        return createBaseCard(entry.getEngWord(), entry.getIndoWord(), "Contoh: " + entry.getExample());
    }

    // --- REVISI DI SINI ---
    // Kartu Gabungan Mode ID: Contoh kalimat hanya diambil SATU saja
    private JPanel createMergedCard(List<DictionaryEntry> entries) {
        if (entries.isEmpty()) return new JPanel();

        // 1. Judul Utama (Kata Indo)
        String mainWord = entries.get(0).getIndoWord(); 

        // 2. Sub Judul (Kumpulan arti Inggris)
        String subWord = entries.stream()
                .map(DictionaryEntry::getEngWord)
                .collect(Collectors.joining(", "));

        // 3. Contoh Kalimat (Hanya ambil yang pertama!)
        // Tidak perlu loop, langsung ambil index ke-0
        String exampleText = "Contoh: " + entries.get(0).getExample();

        return createBaseCard(mainWord, subWord, exampleText);
    }

    private JPanel createBaseCard(String title, String subtitle, String footer) {
        RoundedCardPanel card = new RoundedCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel primaryLabel = new JLabel(title);
        primaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        primaryLabel.setForeground(new Color(30, 30, 30));
        primaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel secondaryLabel = new JLabel(subtitle);
        secondaryLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        secondaryLabel.setForeground(new Color(100, 149, 237)); 
        secondaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footerLabel = new JLabel(footer);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        footerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        footerLabel.setBorder(new EmptyBorder(8, 0, 0, 0)); 

        card.add(primaryLabel);
        card.add(secondaryLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(footerLabel);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 248, 255)); // Background jadi agak biru muda
                card.setHovered(true);
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(250, 250, 250)); 
                card.setHovered(false);
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)
                );
            }
        });

        return card;
    }

    // private void styleButton(JButton btn, boolean isSelected, Color bgSel, Color fgSel, Color bgDef, Color fgDef, int pv, int ph) {
    //     if (isSelected) {
    //         btn.setBackground(bgSel);
    //         btn.setForeground(fgSel);
    //         btn.setBorder(BorderFactory.createEmptyBorder(pv, ph, pv, ph));
    //     } else {
    //         btn.setBackground(bgDef);
    //         btn.setForeground(fgDef);
    //         btn.setBorder(BorderFactory.createCompoundBorder(
    //             BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
    //             BorderFactory.createEmptyBorder(pv-1, ph-1, pv-1, ph-1)
    //         ));
    //     }
    //     btn.setFocusPainted(false);
    // }

    private Image createSearchIcon(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(150, 150, 150));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, width - 6, height - 6);
        g2.drawLine(width - 5, height - 5, width - 1, height - 1);
        g2.dispose();
        return img;
    }

    private void styleScrollBar(JScrollBar bar) {
        bar.setPreferredSize(new Dimension(8, bar.getPreferredSize().height));
        bar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200);
                this.trackColor = Color.WHITE;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
        });
    }

    private void triggerGimmick(String actionCode, String word) {
        if (dictionaryGimmick != null) {
            dictionaryGimmick.execute(actionCode, word);
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(DictionaryApp::new);
    }

    private class GradientButton extends JButton {
        private Color themeColor;
        private boolean isActive = false;

        public GradientButton(String text, Color themeColor) {
            super(text);
            this.themeColor = themeColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(80, 40));
        }

        public void setActive(boolean active) {
            this.isActive = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. Gambar Shadow (Bayangan) - sedikit lebih tipis
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 20, 20);

            // 2. Tentukan Warna Berdasarkan Status Active
            Color startColor, endColor, textColor;

            if (isActive) {
                // MODE AKTIF: Background Biru (Tema), Teks Putih
                startColor = themeColor.brighter(); // Sedikit lebih terang dari header biar kelihatan
                endColor = themeColor;
                textColor = Color.WHITE;
            } else {
                // MODE INAKTIF: Background Putih, Teks Biru
                // Efek tekan (pressed) hanya berlaku jika tombol inaktif
                boolean isPressed = getModel().isPressed();
                startColor = isPressed ? new Color(220, 220, 220) : Color.WHITE;
                endColor = isPressed ? Color.WHITE : new Color(230, 230, 230);
                textColor = themeColor;
            }

            // 3. Gambar Background Button
            GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);

            // Opsional: Tambah border tipis saat aktif biar lebih tegas (karena background header juga biru)
            if (isActive) {
                g2.setColor(new Color(255, 255, 255, 100)); // Putih transparan
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);
            }

            // 4. Gambar Text
            g2.setColor(textColor);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - 3 - fm.stringWidth(getText())) / 2;
            int y = ((getHeight() - 3 - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    // --- Custom Panel untuk Kartu (Rounded & Shadow) ---
    private class RoundedCardPanel extends JPanel {
        private Color borderColor = new Color(230, 230, 230); // Warna border default
        private boolean isHovered = false;
        private int arc = 25; // Kelengkungan sudut
        private int shadowSize = 3; // Ketebalan bayangan

        public RoundedCardPanel() {
            setOpaque(false); // Transparan agar sudut luar terlihat bersih
            setBackground(new Color(250, 250, 250)); // Warna dasar kartu
            // Padding (Jarak teks ke pinggir kartu)
            super.setBorder(new EmptyBorder(15, 20, 15, 20)); 
        }

        public void setHovered(boolean hovered) {
            this.isHovered = hovered;
            repaint(); // Gambar ulang saat status hover berubah
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth() - shadowSize;
            int h = getHeight() - shadowSize;

            // 1. Gambar Shadow (Bayangan)
            g2.setColor(new Color(200, 200, 200, 80)); // Abu-abu transparan
            g2.fillRoundRect(shadowSize, shadowSize, w, h, arc, arc);

            // 2. Gambar Background Putih/Warna Dasar
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            // 3. Gambar Border (Garis Tepi)
            if (isHovered) {
                g2.setColor(new Color(100, 149, 237)); // Biru saat disorot
                g2.setStroke(new BasicStroke(2)); // Garis lebih tebal
            } else {
                g2.setColor(borderColor); // Abu-abu halus saat biasa
                g2.setStroke(new BasicStroke(1));
            }
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

            g2.dispose();
            super.paintComponent(g); // Lanjut gambar teks/label di atasnya
        }
        
        // Mencegah border diganti oleh method lain yang tidak sengaja
        @Override
        public void setBorder(javax.swing.border.Border border) {
            if (border instanceof EmptyBorder) {
                 super.setBorder(border);
            }
        }
    }
}