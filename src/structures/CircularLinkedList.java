package structures;

public class CircularLinkedList<T> {
    private Node<T> tail; // tail.next == head
    private int size;

    public CircularLinkedList() { tail = null; size = 0; }

    public boolean isEmpty() { return tail == null; }
    public int size() { return size; }

    public Node<T> getHead() {
        return (tail == null) ? null : tail.next;
    }

    public void insertAfter(Node<T> node, T value) {
        Node<T> n = new Node<>(value);
        if (isEmpty()) {
            tail = n;
            tail.next = tail;
        } else if (node == null) {
            // insert after tail by default
            n.next = tail.next;
            tail.next = n;
            tail = n;
        } else {
            n.next = node.next;
            node.next = n;
            if (node == tail) tail = n;
        }
        size++;
    }

    public T remove(Node<T> node) {
        if (isEmpty() || node == null) return null;
        Node<T> prev = tail;
        Node<T> cur = tail.next;
        for (int i = 0; i < size; i++) {
            if (cur == node) {
                prev.next = cur.next;
                if (cur == tail) {
                    tail = (cur == prev) ? null : prev;
                }
                size--;
                return cur.data;
            }
            prev = cur;
            cur = cur.next;
        }
        return null;
    }
}