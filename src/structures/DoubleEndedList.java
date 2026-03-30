package structures;

public class DoubleEndedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public DoubleEndedList() { head = tail = null; size = 0; }

    public boolean isEmpty() { return head == null; }
    public int size() { return size; }

    public void addFirst(T value) {
        Node<T> n = new Node<>(value);
        n.next = head;
        if (head != null) head.prev = n;
        head = n;
        if (tail == null) tail = n;
        size++;
    }

    public void addLast(T value) {
        Node<T> n = new Node<>(value);
        if (tail != null) {
            tail.next = n;
            n.prev = tail;
            tail = n;
        } else {
            head = tail = n;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) return null;
        T v = head.data;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;
        size--;
        return v;
    }

    public T removeLast() {
        if (isEmpty()) return null;
        T v = tail.data;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null;
        size--;
        return v;
    }
}