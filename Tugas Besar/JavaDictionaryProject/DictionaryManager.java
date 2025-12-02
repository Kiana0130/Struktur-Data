package JavaDictionaryProject;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryManager {
    // HashMap untuk pencarian cepat O(1)
    private final Map<String, DictionaryEntry> wordMap;
    // TreeMap (menggunakan RBT) untuk menjaga kata-kata terurut O(log N)
    private final TreeMap<String, DictionaryEntry> sortedWords;
    private static final String CSV_FILE = "Struktur-Data/Tugas Besar/JavaDictionaryProject/indo-english-complete.csv";

    public DictionaryManager() {
        this.wordMap = new HashMap<>();
        this.sortedWords = new TreeMap<>();
        loadInitialData();
    }

    public void addEntry(String indo, String eng, String example) {
        DictionaryEntry entry = new DictionaryEntry(indo, eng, example);
        String key = indo.toLowerCase();
        wordMap.put(key, entry);
        sortedWords.put(key, entry);
    }

    // Metode pencarian utama menggunakan HashMap
    public DictionaryEntry search(String word) {
        if (word == null) return null;
        // Search hanya berdasarkan kata Indonesia (kunci di TreeMap/HashMap)
        return wordMap.get(word.toLowerCase()); 
    }

    // Metode untuk mendapatkan semua entri terurut (dari RBT/TreeMap)
    public List<DictionaryEntry> getAllSortedEntries() {
        return new ArrayList<>(sortedWords.values());
    }

    private void loadInitialData() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", 4);

                if (data.length >= 4) {
                    String indo = data[1].trim();
                    String eng = data[2].trim();
                    String example = data[3].trim();
                    example = example.replace("\"", "");
                    addEntry(indo, eng, example);
                }
            }
            System.out.println("Berhasil memuat data dari" + CSV_FILE);
        } catch (IOException e) {
            System.err.println("Gagal memuat data: " + e.getMessage());
            System.err.println("Pastikan file ada di: " + System.getProperty("user.dir"));
        }
        // // Data Normal (diurutkan berdasarkan Indo: A, B, C, D, E, I, J, W)
        // addEntry("Air", "Water", "Air minum adalah kebutuhan pokok.");
        // addEntry("Anjing", "Dog", "Anjing dikenal sebagai sahabat manusia.");
        // addEntry("Api", "Fire", "Hati-hati dengan api, sangat panas.");
        // addEntry("Batu", "Stone", "Batu sangat keras dan padat.");
        // addEntry("Buku", "Book", "Saya suka membaca buku fiksi.");
        // addEntry("Burung", "Bird", "Burung pipit terbang ke sarangnya.");
        // addEntry("Cepat", "Fast", "Mobil itu melaju sangat cepat di jalan tol.");
        // addEntry("Dunia", "World", "Dunia ini luas, penuh misteri.");
        // addEntry("Emas", "Gold", "Emas adalah logam mulia yang berharga.");
        // addEntry("Ikan", "Fish", "Ikan berenang di dalam akuarium.");
        // addEntry("Jalan", "Road", "Jalan menuju rumahku menanjak.");
        // addEntry("Warna", "Color", "Warna hijau menenangkan mata.");
    }
}