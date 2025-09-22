import java.util.Random;

public class ContohSorting {
    public static void main (String[] args) {
        Random rand = new Random();
        int[] deret = new int[100000];
        for (int i = 0; i < deret.length; i++) {
            deret [i] = rand.nextInt(100000);
        }

        long start = System.currentTimeMillis();

        for (int i = 0; i < deret.length - 1; i++) {
            for (int j = 0; j < deret.length - i - 1; j++) {
                if (deret[j] > deret[j + 1]) {
                    int temp = deret[j];
                    deret[j] = deret [j + 1];
                    deret[j + 1] = temp;   
                }
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("Waktunya adalah:" + (end - start) + "ms");
    }
}