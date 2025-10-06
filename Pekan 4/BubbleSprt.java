
import java.util.Arrays;

public class BubbleSprt {
	public static int[] acak() {
		return new int[]{1, 3, 5, 7, 9, 2, 4, 6, 8, 0};
	}

	public static void Bubble (int[] arr) {
		int n = arr.length;
		for (int i = 0; i <  n - 1; i++) {
			for (int j = 0; j< n - i - 1; j++) {
				if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j +1] = temp;
                }
			}
		}
	}

	public static void main(String[] args) {
		int[] data = acak();
        System.out.println("Sebelum sort: " + Arrays.toString(data));

        Bubble(data);

        System.out.println("Sesudah sort: " + Arrays.toString(data));
    }
}