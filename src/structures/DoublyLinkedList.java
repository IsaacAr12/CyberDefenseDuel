package structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public DoublyLinkedList() { head = tail = null; size = 0; }

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
        if (isEmpty()) throw new NoSuchElementException();
        T v = head.data;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;
        size--;
        return v;
    }

    public T removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        T v = tail.data;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null;
        size--;
        return v;
    }

    public boolean remove(T value) {
        Node<T> cur = head;
        while (cur != null) {
            if (cur.data.equals(value)) {
                if (cur.prev != null) cur.prev.next = cur.next;
                else head = cur.next;
                if (cur.next != null) cur.next.prev = cur.prev;
                else tail = cur.prev;
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
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

    public Iterator<T> backwardIterator() {
        return new Iterator<T>() {
            private Node<T> cur = tail;
            @Override public boolean hasNext() { return cur != null; }
            @Override public T next() {
                if (cur == null) throw new NoSuchElementException();
                T d = cur.data; cur = cur.prev; return d;
            }
        };
    }
}