public interface Deque<T> {
    /* Adds an item to the front of the Deque */
    public void addFirst(T item);

    /* Adds an item to the end of the Deque */
    public void addLast(T item);

    /* Returns if it is an empty Deque or not */
    public boolean isEmpty();

    /* Returns the size of the Deque */
    public int size();

    /* Prints the Deque in the order of its items, separated by a space */
    public void printDeque();

    /* Removes the first item in Deque */
    public T removeFirst();

    /* Removes the last item in Deque */
    public T removeLast();

    /* Gets an item in a Deque */
    public T get(int index);
}
