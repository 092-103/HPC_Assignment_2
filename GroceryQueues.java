import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroceryQueues {
    private final GroceryQueue[] queues;
    private final int numQueues;
    private final ExecutorService cashierService;
    private final Random random;
    private final QueueSimulator simulator;

    public GroceryQueues(int numQueues, int maxQueueLength, QueueSimulator simulator) {
        this.numQueues = numQueues;
        this.queues = new GroceryQueue[numQueues];
        this.cashierService = Executors.newFixedThreadPool(numQueues);
        this.random = new Random();
        this.simulator = simulator;

        for (int i = 0; i < numQueues; i++) {
            queues[i] = new GroceryQueue(maxQueueLength);
        }
    }

    public void startServing() {
        for (int i = 0; i < numQueues; i++) {
            final int cashierIndex = i;
            cashierService.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    Customer customer = queues[cashierIndex].serveCustomer();
                    if (customer != null) {
                        try {
                            Thread.sleep(customer.getServiceTime() * 1000);
                            customer.setDepartureTime(System.currentTimeMillis());
                            customer.setWasServed(true);
                            simulator.customerServed();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
        }
    }

    public boolean addCustomer(Customer customer) {
        synchronized (this) {
            GroceryQueue shortestQueue = queues[0];
            for (GroceryQueue queue : queues) {
                if (queue.size() < shortestQueue.size()) {
                    shortestQueue = queue;
                } else if (queue.size() == shortestQueue.size() && random.nextBoolean()) {
                    shortestQueue = queue;
                }
            }

            if (!shortestQueue.addCustomer(customer)) {
                simulator.customerLeft();
                return false;
            }
            return true;
        }
    }

    public void shutdown() {
        cashierService.shutdownNow();
    }
}