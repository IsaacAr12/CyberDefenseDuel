package structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Entiendo que debe ser algo asi
 * Métodos que tiene la mica: addLast, addFirst, removeFirst, remove, find, size, isEmpty, iterator
 */
public class CustomLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public CustomLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (isEmpty()) {
            head = tail = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (isEmpty()) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        Node<T> removed = head;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return removed.data;
    }

    public boolean remove(T value) {
        if (isEmpty()) return false;
        if (head.data.equals(value)) {
            removeFirst();
            return true;
        }
        Node<T> prev = head;
        Node<T> cur = head.next;
        while (cur != null) {
            if (cur.data.equals(value)) {
                prev.next = cur.next;
                if (cur == tail) tail = prev;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
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
            private Node<T> current = head;
            @Override
            public boolean hasNext() { return current != null; }
            @Override
            public T next() {
                if (current == null) throw new NoSuchElementException();
                T d = current.data;
                current = current.next;
                return d;
            }
        };
    }
}
