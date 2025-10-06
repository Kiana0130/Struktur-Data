import java.util.Arrays;

public class Selec {
	public static int[] acak() {
		return new int[] {1, 3, 5, 7, 9, 2, 4, 6, 8, 0};
	}

	public static void Selection (int [] arr) {
		int n = arr.length;
		for (int i = 0; i < n - 1; i++) {
		int minIndex = i;
			for (int j = i + 1; j<n; j++) {
				if (arr[j] < arr[minIndex]) {
					minIndex = j;
				}
			}

			int temp = arr[minIndex];
			arr[minIndex] = arr[i];
			arr[i] = temp;
		}
	}

	public static void main (String [] args) {
		int [] data = acak();
		Selection(data);
		System.out.println("Hasil sorting: " + Arrays.toString(data));
	}
}