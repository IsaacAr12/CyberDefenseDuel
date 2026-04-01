package structures;

import java.util.NoSuchElementException;

public class CustomQueue<T> {
    private Object[] data;
    private int head;
    private int tail;
    private int count;

    public CustomQueue(int capacity) {
        if (capacity <= 0) capacity = 16;
        data = new Object[capacity];
        head = 0;
        tail = 0;
        count = 0;
    }

    public int size() { return count; }
    public boolean isEmpty() { return count == 0; }

    private void ensureCapacity() {
        if (count == data.length) {
            int newCap = data.length * 2;
            Object[] nd = new Object[newCap];
            for (int i = 0; i < count; i++) {
                nd[i] = data[(head + i) % data.length];
            }
            data = nd;
            head = 0;
            tail = count;
        }
    }

    public void enqueue(T item) {
        ensureCapacity();
        data[tail] = item;
        tail = (tail + 1) % data.length;
        count++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) return null;
        T item = (T) data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        count--;
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) return null;
        return (T) data[head];
    }
}