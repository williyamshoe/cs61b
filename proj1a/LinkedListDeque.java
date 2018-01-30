public class LinkedListDeque<T> {
    /* Link class that comprises the Deque */
    private class Link {
        private Link prev;
        private T item;
        private Link next;

        /* Creates a typical Link */
        private Link(Link p, T i, Link n) {
            prev = p;
            item = i;
            next = n;
        }

        /* Creates a typical Link with the prev the same as next */
        private Link(T i, Link n) {
            prev = n;
            item = i;
            next = n;
        }

        /* Creates a empty Link with sentinel value */
        private Link(T i) {
            prev = this;
            item = i;
            next = this;
        }
    }
    private Link sentinel;
    private int size;
    /* Creates an empty Deque */
    public LinkedListDeque() {
        sentinel = new Link(null);
        size = 0;
    }

    /* Adds an item to the front of the Deque */
    public void addFirst(T x) {
        Link prevFirst = sentinel.next;
        Link added = new Link(sentinel, x, prevFirst);
        prevFirst.prev = added;
        sentinel.next = added;
        size += 1;
    }

    /* Adds an item to the end of the Deque */
    public void addLast(T x) {
        Link prevLast = sentinel.prev;
        Link added = new Link(prevLast, x, sentinel);
        prevLast.next = added;
        sentinel.prev = added;
        size += 1;
    }

    /* Returns if it is an empty Deque or not */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Returns the size of the Deque */
    public int size() {
        return size;
    }

    /* Prints the Deque in the order of its items, separated by a space */
    public void printDeque() {
        Link current = sentinel.next;
        while (current != sentinel) {
            System.out.print(current.item);
            System.out.print(" ");
            current = current.next;
        }
    }

    /* Removes the first item in Deque */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T removedValue = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return removedValue;
    }

    /* Removes the last item in Deque */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T removedValue = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return removedValue;
    }

    /* Gets an item in a Deque iteratively */
    public T get(int index) {
        int counter = index;
        Link current = sentinel.next;
        while (counter > 0) {
            if (sentinel == current) {
                return null;
            }
            current = current.next;
            counter -= 1;
        }
        if (sentinel == current) {
            return null;
        }
        return current.item;
    }

    /* Gets an item in a Deque recursively. Is a helper function to getRecursive */
    private T helper(int index, Link current) {
        if (sentinel == current) {
            return null;
        }
        else if (index == 0) {
            return current.item;
        }
        return helper(index - 1, current.next);
    }

    /* Gets an item in a Deque by using helper function helper */
    public T getRecursive(int index) {
        return helper(index, sentinel.next);
    }

}
