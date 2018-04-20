import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        String[] sorted = new String[asciis.length];
        int pos = 0;
        int maxLength = 0;
        for (String s : asciis) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
            sorted[pos] = s;
            pos += 1;
        }

        for (int i = 0; i < maxLength; i += 1) {
            sortHelperLSD(sorted, i);
        }
        return sorted;
    }

    private static int findAtIndex(String s, int i) {
        if (s.length() - 1 - i >= 0) {
            return (int) s.charAt(s.length() - 1 - i);
        }
        return -1;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        HashMap<Integer, List<String>> keeper = new HashMap<>();
        int[] unsortedIndexes = new int[asciis.length];
        int pos = 0;
        for (String s : asciis) {
            int charaDigit = findAtIndex(s, index);
            unsortedIndexes[pos] = charaDigit;
            pos += 1;
            if (!keeper.containsKey(charaDigit)) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(s);
                keeper.put(charaDigit, temp);
            } else {
                keeper.get(charaDigit).add(s);
            }
        }

        int[] sortedIndexes = CountingSort.betterCountingSort(unsortedIndexes);
        pos = 0;
        int prev = -10;
        for (int i : sortedIndexes) {
            if (prev != i) {
                for (String s : keeper.get(i)) {
                    asciis[pos] = s;
                    pos += 1;
                }
                prev = i;
            }
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    private static void main(String[] args) {
        String[] test = new String[] {"aa", "ab", "ba", "ac", "cb"};
        for (String s : test) {
            System.out.print(s + "   ");
        }
        System.out.println();
        for (String s : sort(test)) {
            System.out.print(s + "   ");
        }
    }
}
