import java.util.Arrays;
import java.util.Random;

public class Pekan5 {

    // Generate array acak
    public static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(100000);
        }
        return arr;
    }

    // Array kecil untuk contoh output
    public static int[] terurut() {
        return new int[]{9, 2, 8, 3, 7, 4, 6, 5, 1, 0};
    }

    // ------------------- MERGE SORT -------------------
    public static void mergeSort(int[] arr) {
        if (arr.length <= 1) return;
        int mid = arr.length / 2;

        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);

        mergeSort(left);
        mergeSort(right);
        merge(arr, left, right);
    }

    private static void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) arr[k++] = left[i++];
            else arr[k++] = right[j++];
        }
        while (i < left.length) arr[k++] = left[i++];
        while (j < right.length) arr[k++] = right[j++];
    }

    // ------------------- QUICK SORT -------------------
    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int p = partition(arr, low, high);
            quickSort(arr, low, p - 1);
            quickSort(arr, p + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    // ------------------- SHELL SORT -------------------
    public static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }
    }

    // ------------------- MEASURE FUNCTION -------------------
    public static void runAndMeasure(String algoName, int[] dataKecil, int[] dataBesar) {
        try {
            int[] copyKecil = dataKecil.clone();
            long start = System.nanoTime();
            if (algoName.equals("Merge")) mergeSort(copyKecil);
            else if (algoName.equals("Quick")) quickSort(copyKecil);
            else if (algoName.equals("Shell")) shellSort(copyKecil);
            long end = System.nanoTime();
            double elapsedMs = (end - start) / 1_000_000_000.0;

            System.out.println(algoName + " Sort");
            System.out.printf("%s  %.6f Detik%n", Arrays.toString(copyKecil), elapsedMs);

            int[] copyBesar = dataBesar.clone();
            start = System.nanoTime();
            if (algoName.equals("Merge")) mergeSort(copyBesar);
            else if (algoName.equals("Quick")) quickSort(copyBesar);
            else if (algoName.equals("Shell")) shellSort(copyBesar);
            end = System.nanoTime();
            double elapsedSec = (end - start) / 1_000_000_000.0;

            System.out.printf("100.000 : %.6f detik%n%n", elapsedSec);

        } catch (OutOfMemoryError e) {
            System.out.println(algoName + " Sort gagal untuk data besar (memori habis)\n");
        }
    }

    // ------------------- MAIN PROGRAM -------------------
    public static void main(String[] args) {
        int[] dataKecil = terurut();
        int[] dataBesar = generateRandomArray(100000);

        runAndMeasure("Merge", dataKecil, dataBesar);
        runAndMeasure("Quick", dataKecil, dataBesar);
        runAndMeasure("Shell", dataKecil, dataBesar);
    }
}
