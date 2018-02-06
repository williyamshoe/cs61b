import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void test1 () {
        StudentArrayDeque a = new StudentArrayDeque();
        ArrayDequeSolution b = new ArrayDequeSolution();
        String message = "";
        while (true) {
            double random = StdRandom.uniform();
            if (random < 0.25 || (b.isEmpty() && random < 0.5)) {
                a.addFirst((int) (random * 200));
                b.addFirst((int) (random * 200));
                message = message + "addFront("+ ((int) (random * 200)) +")\n";
            } else if (random < 0.5 || b.isEmpty()) {
                a.addLast((int) (random * 100));
                b.addLast((int) (random * 100));
                message = message + "addLast("+ ((int) (random * 100)) +")\n";
            } else if (random < 0.75) {
                message = message + "removeFirst()\n";
                assertEquals(message, b.removeFirst(), a.removeFirst());
            } else {
                message = message + "removeFirst()\n";
                assertEquals(message, b.removeLast(), a.removeLast());
            }
        }
    }
}
