package structures;

public class CustomStack<T> {
    private Object[] data;
    private int top;

    public CustomStack(int capacity) {
        if (capacity <= 0) capacity = 16;
        data = new Object[capacity];
        top = 0;
    }

    public int size() { return top; }
    public boolean isEmpty() { return top == 0; }

    private void ensureCapacity() {
        if (top == data.length) {
            Object[] nd = new Object[data.length * 2];
            System.arraycopy(data, 0, nd, 0, data.length);
            data = nd;
        }
    }

    public void push(T item) {
        ensureCapacity();
        data[top++] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) return null;
        T item = (T) data[--top];
        data[top] = null;
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) return null;
        return (T) data[top - 1];
    }
}