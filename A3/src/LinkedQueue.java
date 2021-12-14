public class LinkedQueue<T>  implements Queue<T>{
    private class Node<T> {
        T data;
        Node<T> next;
        public Node(T item) {
            data = item;
            next = null;
        }
    }
    private Node<T> head;
    private Node<T> tail;
    public LinkedQueue() {
        head = tail = null;
    }
    @Override
    public T dequeue() throws Exception {
        if (empty())
            throw new Exception("Queue underflow");
        Node<T> spare = head;
        head = head.next;
        if (head == null)
            tail = null;
        return spare.data;
    }
    @Override
    public void enqueue(T item) {
        Node<T> node = new Node<T>(item);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }
    @Override
    public boolean empty() {
        return head == null;
    }
}
