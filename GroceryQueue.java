import java.util.LinkedList;
import java.util.Queue;

public class GroceryQueue {
    private final Queue<Customer> queue;
    private final int maxQueueLength;

    public GroceryQueue(int maxQueueLength) {
        this.queue = new LinkedList<>();
        this.maxQueueLength = maxQueueLength;
    }

    public synchronized boolean addCustomer(Customer customer) {
        // Add customer if queue is not full
        if (queue.size() >= maxQueueLength) {
            return false; // Customer leaves if no space
        }
        queue.offer(customer);
        return true;
    }

    public synchronized Customer serveCustomer() {
        // Serve the first customer in line (FIFO)
        return queue.poll();
    }

    public synchronized boolean isFull() {
        return queue.size() >= maxQueueLength;
    }

    public synchronized int size() {
        return queue.size();
    }
}
