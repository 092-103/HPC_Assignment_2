import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankQueue {
    private final Queue<Customer> queue;
    private final int maxQueueLength;
    private final int tellers;
    private final ExecutorService tellerService;
    private final QueueSimulator simulator;

    public BankQueue(int tellers, int maxQueueLength, QueueSimulator simulator) {
        this.queue = new LinkedList<>();
        this.maxQueueLength = maxQueueLength;
        this.tellers = tellers;
        this.tellerService = Executors.newFixedThreadPool(tellers);
        this.simulator = simulator;
    }

    public synchronized boolean addCustomer(Customer customer) {
        if (queue.size() >= maxQueueLength) {
            simulator.customerLeft();
            return false;
        }
        queue.offer(customer);
        return true;
    }

    public void startServing() {
        for (int i = 0; i < tellers; i++) {
            tellerService.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    Customer customer = serveCustomer();
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

    private synchronized Customer serveCustomer() {
        return queue.poll();
    }

    public void shutdown() {
        tellerService.shutdownNow();
    }
}