import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

public class GroceryQueues {
    private final GroceryQueue[] queues;
    private final int numQueues;
    private final ExecutorService cashierService;
    private final Random random;

    public GroceryQueues(int numQueues, int maxQueueLength) {
        this.numQueues = numQueues;
        this.queues = new GroceryQueue[numQueues];
        this.cashierService = Executors.newFixedThreadPool(numQueues);
        this.random = new Random();

        // Initialize each queue
        for (int i = 0; i < numQueues; i++) {
            queues[i] = new GroceryQueue(maxQueueLength);
        }
    }

    public void startServing() {
        // Each cashier serves customers concurrently in separate threads
        for (int i = 0; i < numQueues; i++) {
            final int cashierIndex = i;
            cashierService.submit(() -> {
                while (true) {
                    Customer customer = queues[cashierIndex].serveCustomer();
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

    public boolean addCustomer(Customer customer) {
        // Find the queue with the fewest people waiting
        synchronized (this) {
            GroceryQueue shortestQueue = queues[0];
            for (GroceryQueue queue : queues) {
                if (queue.size() < shortestQueue.size()) {
                    shortestQueue = queue;
                } else if (queue.size() == shortestQueue.size() && random.nextBoolean()) {
                    shortestQueue = queue;
                }
            }

            // Try to add customer to the chosen queue
            return shortestQueue.addCustomer(customer);
        }
    }

    public void shutdown() {
        cashierService.shutdown();
    }
}
