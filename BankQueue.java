import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankQueue {
    private final Queue<Customer> queue;
    private final int maxQueueLength;
    private final int tellers;
    private final ExecutorService tellerService;

    public BankQueue(int tellers, int maxQueueLength) {
        this.queue = new LinkedList<>();
        this.maxQueueLength = maxQueueLength;
        this.tellers = tellers;
        // Create a thread pool with the same number of threads as tellers
        this.tellerService = Executors.newFixedThreadPool(tellers);
    }

    public synchronized boolean addCustomer(Customer customer) {
        // Add customer if queue is not full
        if (queue.size() >= maxQueueLength) {
            return false; // Customer leaves immediately if queue is full
        }
        queue.offer(customer);
        return true;
    }

    public void startServing() {
        // Each teller serves customers concurrently in a separate thread
        for (int i = 0; i < tellers; i++) {
            tellerService.submit(() -> {
                while (true) {
                    Customer customer = serveCustomer();
                    if (customer != null) {
                        try {
                            // Simulate service time
                            Thread.sleep(customer.getServiceTime() * 1000);
                            customer.setDepartureTime(System.currentTimeMillis());
                            customer.setWasServed(true);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
        }
    }

    private synchronized Customer serveCustomer() {
        // Serve the first customer in line (FIFO)
        return queue.poll();
    }

    public void shutdown() {
        tellerService.shutdown();
    }
}
