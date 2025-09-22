import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Sorting {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Loop utama untuk menu awal
        while (true) {
            System.out.println("\n========= MENU UTAMA =========");
            System.out.println("1. Urutkan 10 Angka (Pre-defined)");
            System.out.println("2. Urutkan 100.000 Angka (Random)");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Memanggil menu untuk 10 angka
                    handleSortingMenu(scanner, 10);
                    break;
                case 2:
                    // Memanggil menu untuk 100.000 angka
                    handleSortingMenu(scanner, 100000);
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan program ini!");
                    return; // Keluar dari program
                default:
                    System.out.println("Pilihan tidak valid, silakan coba lagi.");
            }
        }
    }

    /**
     * Menangani menu pilihan algoritma sorting.
     * @param scanner Objek Scanner untuk input pengguna.
     * @param size Ukuran array (10 atau 100000).
     */
    public static void handleSortingMenu(Scanner scanner, int size) {
        // Buat array data asli sekali saja, agar semua sort membandingkan data acak yang sama
        int[] originalArray;
        if (size == 10) {
            // Data yang sudah ditentukan untuk 10 angka
            originalArray = new int[]{23, 17, 4, 90, 56, 32, 8, 77, 11, 1};
        } else {
            // Generate angka random untuk 100.000 angka
            originalArray = generateRandomArray(size);  
            System.out.println("Berhasil men-generate 100.000 angka acak.");
        }

        // Loop untuk menu pilihan sort
        while (true) {
            System.out.println("\n--- Pilihan Algoritma Sort untuk " + size + " Angka ---");
            System.out.println("1. Bubble Sort");
            System.out.println("2. Selection Sort");
            System.out.println("3. Insertion Sort");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilih algoritma: ");

            int sortChoice = scanner.nextInt();
            
            if (sortChoice == 4) {
                break; // Keluar dari loop ini untuk kembali ke menu utama
            }

            // PENTING: Salin array asli agar setiap algoritma mengurutkan data yang sama
            // dan tidak mengurutkan data yang sudah terurut oleh algoritma sebelumnya.
            int[] arrayToSort = originalArray.clone();

            long startTime = 0, endTime = 0;
            String algorithmName = "";

            switch (sortChoice) {
                case 1:
                    algorithmName = "Bubble Sort";
                    startTime = System.nanoTime();
                    bubbleSort(arrayToSort);
                    endTime = System.nanoTime();
                    break;
                case 2:
                    algorithmName = "Selection Sort";
                    startTime = System.nanoTime();
                    selectionSort(arrayToSort);
                    endTime = System.nanoTime();
                    break;
                case 3:
                    algorithmName = "Insertion Sort";
                    startTime = System.nanoTime();
                    insertionSort(arrayToSort);
                    endTime = System.nanoTime();
                    break;
                default:
                    System.out.println("Pilihan algoritma tidak valid.");
                    continue; // Lanjut ke iterasi loop berikutnya
            }

            long durationInNano = endTime - startTime;
            double durationInMillis = durationInNano / 1_000_000.0;

            System.out.println("\n--- HASIL " + algorithmName.toUpperCase() + " ---");
            
            // Tampilkan output sesuai permintaan
            if (size == 10) {
                System.out.println("Data Terurut: " + Arrays.toString(arrayToSort));
            }
            
            System.out.printf("Waktu Eksekusi: %.4f milidetik (ms)\n", durationInMillis);
        }
    }

    // --- METODE ALGORITMA SORTING ---

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Tukar arr[j] dengan arr[j+1]
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
            // Tukar elemen terkecil dengan elemen pertama di bagian yang belum terurut
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
            // Pindahkan elemen dari arr[0..i-1] yang lebih besar dari key
            // ke satu posisi di depan posisi mereka saat ini
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * Membuat array dengan angka acak.
     * @param size Ukuran array yang akan dibuat.
     * @return Array integer yang berisi angka acak.
     */
    public static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(); // Mengisi dengan angka integer acak
        }
        return arr;
    }
}