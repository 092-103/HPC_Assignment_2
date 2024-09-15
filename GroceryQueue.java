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
        if (queue.size() >= maxQueueLength) {
            return false;
        }
        queue.offer(customer);
        return true;
    }

    public synchronized Customer serveCustomer() {
        return queue.poll();
    }

    public synchronized int size() {
        return queue.size();
    }
}