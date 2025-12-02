package JavaDictionaryProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DictionaryGimmick {
    private final Map<String, Consumer<JPanel>> gimmickActions;

    public DictionaryGimmick() {
        gimmickActions = new HashMap<>();
        registerGimmicks();
    }

    private void registerGimmicks() {
        gimmickActions.put("hantu", this::showGhostGimmick);
    }

    public boolean tryExecuteGimmick(String keyword, JPanel contentPanel) {
        String cleanKey = keyword.trim().toLowerCase();
        
        if (gimmickActions.containsKey(cleanKey)) {
            gimmickActions.get(cleanKey).accept(contentPanel);
            return true;
        }
        return false;
    }

    // --- Logika Tampilan Gimmick ---

    private void showGhostGimmick(JPanel panel) {
        panel.removeAll();
        JPanel gimmickPanel = new JPanel();
        gimmickPanel.setLayout(new BorderLayout());
        gimmickPanel.setBackground(Color.BLACK);
        gimmickPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        gimmickPanel.setPreferredSize(new Dimension(800, 500));

        String imagePath = "Hantu.jpeg"; 
        File imgFile = new File(imagePath);
        JLabel contentLabel;

        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            contentLabel = new JLabel(new ImageIcon(img));
        } else {
            contentLabel = new JLabel("XIXIXI (Gambar hantu.png tidak ditemukan)");
            contentLabel.setFont(new Font("Chiller", Font.BOLD, 40));
            contentLabel.setForeground(Color.RED);
        }
        
        contentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel textLabel = new JLabel("HIHIHIHIHIHIHI....");
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        textLabel.setForeground(Color.RED);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        gimmickPanel.add(contentLabel, BorderLayout.CENTER);
        gimmickPanel.add(textLabel, BorderLayout.SOUTH);

        panel.add(gimmickPanel);
    
        panel.revalidate();
        panel.repaint();
    }
}