package JavaDictionaryProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;
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
        setSize(980, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Fonts & Style ---
        Font searchFieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240)); 
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Search Field
        searchField = new JTextField("Pencarian Teks", 25);
        searchField.setFont(searchFieldFont);
        searchField.setForeground(Color.GRAY);
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

        JPanel searchInputWrapper = new JPanel(new BorderLayout());
        searchInputWrapper.setBackground(Color.WHITE);
        searchInputWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1)); 
        
        JPanel iconWrapper = new JPanel(new BorderLayout());
        iconWrapper.setBackground(Color.WHITE);
        iconWrapper.add(searchIcon, BorderLayout.CENTER);
        iconWrapper.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10)); 
        iconWrapper.setPreferredSize(new Dimension(35, 35));

        searchInputWrapper.add(searchField, BorderLayout.CENTER);
        searchInputWrapper.add(iconWrapper, BorderLayout.EAST);
        searchInputWrapper.setPreferredSize(new Dimension(350, 40));

        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftHeaderPanel.setOpaque(false);
        leftHeaderPanel.add(searchInputWrapper);

        // Language Buttons
        JPanel langButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        langButtonPanel.setOpaque(false);
        
        idButton = new JButton("ID");
        engButton = new JButton("Eng");
        idButton.setFont(buttonFont);
        engButton.setFont(buttonFont);
        
        Color selectedBtnBg = new Color(50, 120, 255); 
        Color selectedBtnText = Color.WHITE;
        Color defaultBtnBg = Color.WHITE;  
        Color defaultBtnText = Color.BLACK;
        
        int btnPaddingV = 8;
        int btnPaddingH = 20;

        styleButton(idButton, true, selectedBtnBg, selectedBtnText, defaultBtnBg, defaultBtnText, btnPaddingV, btnPaddingH);
        styleButton(engButton, false, selectedBtnBg, selectedBtnText, defaultBtnBg, defaultBtnText, btnPaddingV, btnPaddingH);

        langButtonPanel.add(idButton);
        langButtonPanel.add(engButton);

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
            styleButton(idButton, true, selectedBtnBg, selectedBtnText, defaultBtnBg, defaultBtnText, btnPaddingV, btnPaddingH);
            styleButton(engButton, false, selectedBtnBg, selectedBtnText, defaultBtnBg, defaultBtnText, btnPaddingV, btnPaddingH);
            performGlobalSearch();
        });

        engButton.addActionListener(e -> {
            currentLanguage = "Eng";
            styleButton(engButton, true, selectedBtnBg, selectedBtnText, defaultBtnBg, defaultBtnText, btnPaddingV, btnPaddingH);
            styleButton(idButton, false, selectedBtnBg, selectedBtnText, defaultBtnBg, defaultBtnText, btnPaddingV, btnPaddingH);
            performGlobalSearch();
        });

        filterAndDisplayWords(""); 
        setVisible(true);
    }
    
    private void performGlobalSearch() {
        String text = searchField.getText();
        if (text.equals("Pencarian Teks") && searchField.getForeground() == Color.GRAY) {
            filterAndDisplayWords(""); 
        } else {
            filterAndDisplayWords(text);
        }
    }

    private void filterAndDisplayWords(String query) {
        contentPanel.removeAll(); 
        String cleanQuery = query.trim().toLowerCase();
        
        List<DictionaryEntry> allEntries = dictionaryManager.getAllSortedEntries();

        if (currentLanguage.equals("ID")) {
            // --- MODE ID (GROUPING) ---
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
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(250, 250, 250));
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1000)); 

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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(240, 245, 255)); 
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 149, 237), 1, true),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(250, 250, 250)); 
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
        });

        return card;
    }

    private void styleButton(JButton btn, boolean isSelected, Color bgSel, Color fgSel, Color bgDef, Color fgDef, int pv, int ph) {
        if (isSelected) {
            btn.setBackground(bgSel);
            btn.setForeground(fgSel);
            btn.setBorder(BorderFactory.createEmptyBorder(pv, ph, pv, ph));
        } else {
            btn.setBackground(bgDef);
            btn.setForeground(fgDef);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(pv-1, ph-1, pv-1, ph-1)
            ));
        }
        btn.setFocusPainted(false);
    }

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

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(DictionaryApp::new);
    }
}