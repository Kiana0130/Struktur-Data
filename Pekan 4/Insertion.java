import java.util.Arrays;

public class Insertion {
	public static int[] acak() {
		return new int[]{1, 3, 5, 7, 9, 2, 4, 6, 8, 0};
	}

	public static void Insert (int[] arr) {
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

	public static void main(String[] args) {
		int[] data = acak();
        System.out.println("Sebelum sort: " + Arrays.toString(data));

        Insert(data);

        System.out.println("Sesudah sort: " + Arrays.toString(data));
    }
}