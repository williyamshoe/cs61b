public class Palindrome {

    /* Converts a string to an ArrayDeque */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> c = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            c.addLast(word.charAt(i));
        }
        return c;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> c = wordToDeque(word);
        while (c.size() > 1) {
            if (c.removeFirst() != c.removeLast()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> c = wordToDeque(word);
        while (c.size() > 1) {
            char first = c.removeFirst();
            char last = c.removeLast();
            if (!cc.equalChars(first, last)) {
                return false;
            }
        }
        return true;
    }
}
