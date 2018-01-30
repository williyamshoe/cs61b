public class LinkedListDeque<Generic>
{
    /* Link class that comprises the Deque */
    private class Link
    {
        public Link prev;
        public Generic item;
        public Link next;

        /* Creates a typical Link */
        public Link(Link p, Generic i, Link n)
        {
            prev = p;
            item = i;
            next = n;
        }

        /* Creates a typical Link with the prev the same as next */
        public Link(Generic i, Link n)
        {
            prev = n;
            item = i;
            next = n;
        }

        /* Creates a empty Link with sentinel value */
        public Link(Generic i)
        {
            prev = this;
            item = i;
            next = this;
        }
    }
    private Link sentinel;
    private int size;
    /* Creates an empty Deque */
    public LinkedListDeque()
    {
        sentinel = new Link(null);
        size = 0;
    }

    /* Creates a Deque with one item*/
    public LinkedListDeque(Generic x)
    {
        sentinel = new Link(null);
        Link following = new Link(x, sentinel);
        sentinel.prev = following;
        sentinel.next = following;
        size = 1;
    }

    /* Adds an item to the front of the Deque */
    public void addFirst(Generic x)
    {
        Link prevFirst = sentinel.next;
        Link added = new Link(sentinel, x, prevFirst);
        prevFirst.prev = added;
        sentinel.next = added;
        size += 1;
    }

    /* Adds an item to the end of the Deque */
    public void addLast(Generic x)
    {
        Link prevLast = sentinel.prev;
        Link added = new Link(prevLast, x, sentinel);
        prevLast.next = added;
        sentinel.prev = added;
        size += 1;
    }

    /* Returns if it is an empty Deque or not */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /* Returns the size of the Deque */
    public int size()
    {
        return size;
    }

    /* Prints the Deque in the order of its items, separated by a space */
    public void printDeque()
    {
        Link current = sentinel.next;
        while (current != sentinel)
        {
            System.out.print(current.item);
            System.out.print(" ");
            current = current.next;
        }
    }

    /* Removes the first item in Deque */
    public Generic removeFirst()
    {
        if (isEmpty())
        {
            return null;
        }
        Generic removed_value = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return removed_value;
    }

    /* Removes the last item in Deque */
    public Generic removeLast()
    {
        if (isEmpty())
        {
            return null;
        }
        Generic removed_value = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return removed_value;
    }

    /* Gets an item in a Deque iteratively */
    public Generic get(int index)
    {
        int counter = index;
        Link current = sentinel.next;
        while (counter > 0)
        {
            if (sentinel == current)
            {
                return null;
            }
            current = current.next;
            counter -= 1;
        }
        if (sentinel == current)
        {
            return null;
        }
        return current.item;
    }

    /* Gets an item in a Deque recursively. Is a helper function to getRecursive */
    private Generic helper(int index, Link current)
    {
        if (sentinel == current)
        {
            return null;
        }
        else if (index == 0)
        {
            return current.item;
        }
        return helper(index - 1, current.next);
    }

    /* Gets an item in a Deque by using helper function helper */
    public Generic getRecursive(int index)
    {
        return helper(index, sentinel.next);
    }
}