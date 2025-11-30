package JavaDictionaryProject;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class DictionaryManager {
    // HashMap untuk pencarian cepat O(1)
    private final Map<String, DictionaryEntry> wordMap;
    // TreeMap (menggunakan RBT) untuk menjaga kata-kata terurut O(log N)
    private final TreeMap<String, DictionaryEntry> sortedWords;

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
        // Data Normal (diurutkan berdasarkan Indo: A, B, C, D, E, I, J, W)
        addEntry("Air", "Water", "Air minum adalah kebutuhan pokok.");
        addEntry("Anjing", "Dog", "Anjing dikenal sebagai sahabat manusia.");
        addEntry("Api", "Fire", "Hati-hati dengan api, sangat panas.");
        addEntry("Batu", "Stone", "Batu sangat keras dan padat.");
        addEntry("Buku", "Book", "Saya suka membaca buku fiksi.");
        addEntry("Burung", "Bird", "Burung pipit terbang ke sarangnya.");
        addEntry("Cepat", "Fast", "Mobil itu melaju sangat cepat di jalan tol.");
        addEntry("Dunia", "World", "Dunia ini luas, penuh misteri.");
        addEntry("Emas", "Gold", "Emas adalah logam mulia yang berharga.");
        addEntry("Ikan", "Fish", "Ikan berenang di dalam akuarium.");
        addEntry("Jalan", "Road", "Jalan menuju rumahku menanjak.");
        addEntry("Warna", "Color", "Warna hijau menenangkan mata.");
    }
}