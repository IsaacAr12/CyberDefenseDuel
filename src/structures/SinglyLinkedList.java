package structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SinglyLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public SinglyLinkedList() { head = tail = null; size = 0; }

    public boolean isEmpty() { return head == null; }
    public int size() { return size; }

    public void addFirst(T value) {
        Node<T> n = new Node<>(value);
        n.next = head;
        head = n;
        if (tail == null) tail = n;
        size++;
    }

    public void addLast(T value) {
        Node<T> n = new Node<>(value);
        if (isEmpty()) head = tail = n;
        else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Empty");
        T v = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return v;
    }

    public boolean remove(T value) {
        if (isEmpty()) return false;
        if (head.data.equals(value)) { removeFirst(); return true; }
        Node<T> prev = head;
        Node<T> cur = head.next;
        while (cur != null) {
            if (cur.data.equals(value)) {
                prev.next = cur.next;
                if (cur == tail) tail = prev;
                size--; return true;
            }
            prev = cur; cur = cur.next;
        }
        return false;
    }

    public Node<T> find(T value) {
        Node<T> cur = head;
        while (cur != null) {
            if (cur.data.equals(value)) return cur;
            cur = cur.next;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cur = head;
            @Override public boolean hasNext() { return cur != null; }
            @Override public T next() {
                if (cur == null) throw new NoSuchElementException();
                T d = cur.data; cur = cur.next; return d;
            }
        };
    }
}