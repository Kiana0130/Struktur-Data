import java.util.Arrays;
import java.util.Random;

public class Contoh {
    /**
     * @param size
     * @return
     */
    public static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt();
        }
        return arr;
    }

    public static int[] terurut () {
        return new int[]{23, 17, 4, 90, 56, 32, 8, 77, 11, 1};
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    public static void runAndMeasure(String algoName, int[] dataKecil, int[] dataBesar) {
        try {
            int[] copyKecil = dataKecil.clone();
            long start = System.nanoTime();
            if (algoName.equals("Bubble")) bubbleSort(copyKecil);
            else if (algoName.equals("Selection")) selectionSort(copyKecil);
            else if (algoName.equals("Insertion")) insertionSort(copyKecil);
            long end = System.nanoTime();
            double elapsedMs = (end - start) / 1_000_000.0;

            System.out.println(algoName + " Sort");
            System.out.printf("%s  %.3f ms%n", Arrays.toString(copyKecil), elapsedMs);

            int[] copyBesar = dataBesar.clone();
            start = System.nanoTime();
            if (algoName.equals("Bubble")) bubbleSort(copyBesar);
            else if (algoName.equals("Selection")) selectionSort(copyBesar);
            else if (algoName.equals("Insertion")) insertionSort(copyBesar);
            end = System.nanoTime();
            double elapsedSec = (end - start) / 1_000_000_000.0;

            System.out.printf("100.000 : %.3f detik%n%n", elapsedSec);
            
        } catch (OutOfMemoryError e) {
            System.out.println(algoName + " Sort gagal untuk data besar (memori habis)\n");
        }
    }

    public static void main(String[] args) {
        int[] dataKecil = terurut();
        int[] dataBesar = generateRandomArray(100000);

        runAndMeasure("Bubble", dataKecil, dataBesar);
        runAndMeasure("Selection", dataKecil, dataBesar);
        runAndMeasure("Insertion", dataKecil, dataBesar);
    }
}