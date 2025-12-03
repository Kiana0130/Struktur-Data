package JavaDictionaryProject;

public class DictionaryEntry {
    private String indoWord;
    private String engWord;
    private String example;

    public DictionaryEntry(String indoWord, String engWord, String example) {
        this.indoWord = indoWord;
        this.engWord = engWord;
        this.example = example;
    }

    public String getIndoWord() { return indoWord; }
    public String getEngWord() { return engWord; }
    public String getExample() { return example; }

    @Override
    public String toString() {
        return indoWord; 
    }
}