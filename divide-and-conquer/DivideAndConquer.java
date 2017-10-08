import java.util.Arrays;

/**
 * Created by Gary on 10/11/2016.
 */
public class DivideAndConquer {

    /**
     * MAXIMUM SUBARRAY PROBLEM
     */
    private static int bruteForce(int[] a) {             //COMPLEXITY O(n^2)
        int max = a[0];
        for (int i = 1; i < a.length; i++) {
            int current = 0;
            for (int j = i + 1; j < a.length; j++) {
                current += a[j];
                if (current > max) {
                    max = current;
                }
            }
        }

        return max;
    }

    private static int divideAndConquer(int[] a, int start, int end) {
        if (start == end) {
            return a[start];
        }
        else {

            //split
            int middle = (start + end) / 2;

            //left
            int maxLeftSum = divideAndConquer(a, start, middle);

            //right
            int maxRightSum = divideAndConquer(a, middle + 1, end);

            //crossover
            //left side
            int leftBorderSum = 0;
            int maxLeftBorderSum = 0;
            for (int i = middle; i > start; i--) {
                leftBorderSum += a[i];
                if (leftBorderSum > maxLeftBorderSum) {
                    maxLeftBorderSum = leftBorderSum;
                }
            }

            //right side
            int rightBorderSum = 0;
            int maxRightBorderSum = 0;
            for (int i = middle + 1; i <= end; i++) {
                rightBorderSum += a[i];
                if (rightBorderSum > maxRightBorderSum) {
                    maxRightBorderSum = rightBorderSum;
                }
            }

            return Math.max(maxLeftSum, Math.max(maxRightSum, maxLeftBorderSum + maxRightBorderSum));
        }
    }

    public static void main(String[] args) {
        int[] a = {1, -3, 5, -7, 4};
        System.out.println(Arrays.toString(a));     // Print original string
        System.out.println("_BF " + bruteForce(a) + " O(n^2)");          // Brute Force
        System.out.println("DnC " + divideAndConquer(a, 0, 4) + " O(n*log n)");

    }
}
