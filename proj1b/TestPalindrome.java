import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();
    CharacterComparator cc = new OffByOne();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindromeBase() {
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("w"));
    }

    @Test
    public void testIsPalindromeIncorrect() {
        assertFalse(palindrome.isPalindrome("cool"));
        assertFalse(palindrome.isPalindrome("streets"));
    }

    @Test
    public void testIsPalindromeCorrect() {
        assertTrue(palindrome.isPalindrome("noon"));
        assertTrue(palindrome.isPalindrome("racecar"));
    }

    @Test
    public void testIsPalindromeWithCCBase() {
        assertTrue(palindrome.isPalindrome("", cc));
        assertTrue(palindrome.isPalindrome("w", cc));
    }

    @Test
    public void testIsPalindromeWithCCCorrect() {
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertTrue(palindrome.isPalindrome("wwiijijijjvx", cc));
    }

    @Test
    public void testIsPalindromeWithCCIncorrect() {
        assertFalse(palindrome.isPalindrome("nope", cc));
        assertFalse(palindrome.isPalindrome("thank", cc));
    }
}
