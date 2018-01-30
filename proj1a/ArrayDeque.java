public class ArrayDeque<T> {
    private T[] values;
    private int size;
    private int nfirst;
    private int nlast;

    /* Constructs an empty ArrayDeque */
    public ArrayDeque() {
        values = (T[]) new Object[8];
        nfirst = 3;
        nlast = 4;
        size = 0;
    }

    /* Constructs an ArrayDeque with one element */
    public ArrayDeque(T item) {
        values = (T[]) new Object[8];
        nfirst = 3;
        values[4] = item;
        nlast = 5;
        size = 1;
    }

    /* Safely changes the provided index by change */
    private int changeIndex(int current, int change) {
        int end = current + change;
        if (end >= values.length)
        {
            end -= values.length;
        }
        else if(end < 0)
        {
            end += values.length;
        }
        return end;
    }

    /* Checks if values is full applies a resize if so.
    * Changes nextFirst and nextLast to values.length-1 and size respectively if resized */
    private void checkAndResize() {
        double usage = size * 1.0 / values.length;
        if (usage <= 0.25 && size >= 16)
        {
            T[] a = (T[]) new Object[size/2];
            if (nfirst < nlast)
            {
                System.arraycopy(values, changeIndex(nfirst, 1), a, 0, size);
            }
            else
            {
                System.arraycopy(values, changeIndex(nfirst, 1), a, 0, values.length-nfirst-1);
                System.arraycopy(values, 0, a, values.length-nfirst-1, nlast);
            }
            nlast = size;
            nfirst = a.length - 1;
            values = a;
        }
        else if (usage == 1.0) {
            T[] b = (T[]) new Object[size*2];
            System.arraycopy(values, nlast, b, 0, size-nlast);
            System.arraycopy(values, 0, b, size-nlast, nlast);
            nlast = size;
            nfirst = b.length - 1;
            values = b;
        }
    }

    /* Adds an item to the front of an ArrayDeque.
     * Checks and resizes if necessary before adding */
    public void addFirst(T item) {
        checkAndResize();
        values[nfirst] = item;
        size += 1;
        nfirst = changeIndex(nfirst, -1);
    }

    /* Adds an item to the back of an ArrayDeque.
     * Checks and resizes if necessary before adding */
    public void addLast(T item) {
        checkAndResize();
        values[nlast] = item;
        size += 1;
        nlast = changeIndex(nlast, 1);
    }

    /* Checks if an ArrayDeque is empty */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Returns the size of the ArrayDeque */
    public int size() {
        return size;
    }

    /* Prints the elements of an ArrayDeque */
    public void printDeque() {
        int change = 1;
        int counter = changeIndex(nfirst, change);
        while (change <= size)
        {
            System.out.print(values[counter]);
            System.out.print(" ");
            change += 1;
            counter = changeIndex(nfirst, change);
        }
    }

    /* Removes the first element of the ArrayDeque if applicable.
     * Checks and resizes if usage is lower than the threshold afterwards */
    public T removeFirst() {
        if (size == 0)
        {
            return null;
        }
        nfirst = changeIndex(nfirst, 1);
        size -= 1;
        T removed = values[nfirst];
        values[nfirst] = null;
        checkAndResize();
        return removed;
    }

    /* Removes the last element of the ArrayDeque if applicable.
     * Checks and resizes if usage is lower than the threshold afterwards */
    public T removeLast() {
        if (size == 0)
        {
            return null;
        }
        nlast = changeIndex(nlast, -1);
        size -= 1;
        T removed = values[nlast];
        values[nlast] = null;
        checkAndResize();
        return removed;
    }

    /* Gets the item at the index if applicable.
     * Runs in constant time */
    public T get(int index) {
        if (index < 0 || index >= size)
        {
            return null;
        }
        return values[changeIndex(nfirst, 1+index)];
    }
}
