import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QueueSimulator {
    private final BankQueue bankQueue;
    private final GroceryQueues groceryQueues;
    private final ScheduledExecutorService clock;
    private final int simulationTime;

    public QueueSimulator(int simulationTime, int numTellers, int numCashiers, int maxBankQueueLength, int maxGroceryQueueLength) {
        this.bankQueue = new BankQueue(numTellers, maxBankQueueLength);
        this.groceryQueues = new GroceryQueues(numCashiers, maxGroceryQueueLength);
        this.clock = Executors.newScheduledThreadPool(1); // A single clock thread
        this.simulationTime = simulationTime;
    }

    public void runSimulation() {
        // Start serving customers concurrently
        bankQueue.startServing();
        groceryQueues.startServing();

        // Schedule customer arrival every second
        clock.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            int serviceTime = (int) (60 + Math.random() * 240); // Random service time between 60-300s
            Customer customer = new Customer(currentTime, serviceTime);

            if (!bankQueue.addCustomer(customer)) {
                customer.setLeftWithoutService(true);
            }

            if (!groceryQueues.addCustomer(customer)) {
                customer.setLeftWithoutService(true);
            }
        }, 0, 1, TimeUnit.SECONDS); // Every second

        // Stop the simulation after a set time
        clock.schedule(() -> {
            clock.shutdown();
            bankQueue.shutdown();
            groceryQueues.shutdown();
            // Output statistics
        }, simulationTime, TimeUnit.SECONDS);
    }
}
