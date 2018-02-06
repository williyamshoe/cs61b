import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testEqualCharsCorrect() {
        assertTrue(offByOne.equalChars('e', 'f'));
        assertTrue(offByOne.equalChars('j', 'i'));
    }

    @Test
    public void testEqualCharsIncorrect() {
        assertFalse(offByOne.equalChars('e', 'e'));
        assertFalse(offByOne.equalChars('z', 'a'));
        assertFalse(offByOne.equalChars('t', 'v'));
    }
}
