/**
 * Created by Gary on 11/7/2016.
 * This program demonstrates the Activity Selection Problem. It is implemented using a greedy
 * algorithm which works by making the locally optimal choice at each stage with the hope of
 * finding a global optimum. The average run time for this implementation is O(n).
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ActivitySelectorProblem {

    // sort array pairs based on smallest finish time
    private static void sortArrayPairsBasedOnFinishTimes(int[] start, int[] finish) {
        for (int i = 0; i < finish.length; i++) {

            for (int j = i; j < finish.length; j++) {

                int F_Overwritten = finish[i];
                int S_Overwritten = start[i];
                if (finish[i] > finish[j]) {
                    finish[i] = finish[j];
                    finish[j] = F_Overwritten;
                    // matching up the other array
                    start[i] = start[j];
                    start[j] = S_Overwritten;
                }
            }
        }
    }

    //greedy algorithm
    private static List<Integer> ActivitySelector(int[] start, int[] finish) {
        List<Integer> a = new ArrayList<>();
        a.add(start[0]);    // start with first element in this implementation since smallest finish is first
        int length = start.length;
        int i = 0;
        for (int m = 1; m < length; m++) {
            if (start[m] >= finish[i]) {
                a.add(start[m]);
                i = m;
            }
        }
        return a;
    }

    public static void main(String[] args) {
        System.out.println("Enter number of activities ");
        Scanner in = new Scanner(System.in);
        int numActivity = in.nextInt();

        System.out.println("Enter each activity's starting time: ");
        int[] start = new int[numActivity];
        for (int i = 0; i < start.length; i++) {
            start[i] = in.nextInt();
        }

        System.out.println("Enter each activity's finish time: ");
        int[] finish = new int[numActivity];
        for (int i = 0; i < finish.length; i++) {
            finish[i] = in.nextInt();
        }

        sortArrayPairsBasedOnFinishTimes(start, finish);

        List<Integer> list = ActivitySelector(start, finish);
        System.out.println("To maximize the number of activities, you should attend " +
                "activities who's start times are: \n" + list);

    }

}
