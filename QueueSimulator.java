import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueSimulator {
    private final BankQueue bankQueue;
    private final GroceryQueues groceryQueues;
    private final ScheduledExecutorService clock;
    private final int simulationTime;
    private final AtomicInteger totalCustomers = new AtomicInteger(0);
    private final AtomicInteger servedCustomers = new AtomicInteger(0);
    private final AtomicInteger leftCustomers = new AtomicInteger(0);

    public QueueSimulator(int simulationTime, int numTellers, int numCashiers, int maxBankQueueLength, int maxGroceryQueueLength) {
        this.bankQueue = new BankQueue(numTellers, maxBankQueueLength, this);
        this.groceryQueues = new GroceryQueues(numCashiers, maxGroceryQueueLength, this);
        this.clock = Executors.newScheduledThreadPool(1);
        this.simulationTime = simulationTime;
    }

    public void runSimulation() {
        System.out.println("Starting simulation for " + simulationTime + " seconds.");
        
        bankQueue.startServing();
        groceryQueues.startServing();

        clock.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            int serviceTime = (int) (60 + Math.random() * 240);
            Customer customer = new Customer(currentTime, serviceTime);
            totalCustomers.incrementAndGet();

            if (!bankQueue.addCustomer(customer)) {
                leftCustomers.incrementAndGet();
            }

            if (!groceryQueues.addCustomer(new Customer(currentTime, serviceTime))) {
                leftCustomers.incrementAndGet();
            }
        }, 0, 1, TimeUnit.SECONDS);

        clock.schedule(this::endSimulation, simulationTime, TimeUnit.SECONDS);

        try {
            clock.awaitTermination(simulationTime + 1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void endSimulation() {
        System.out.println("Ending simulation...");
        clock.shutdown();
        bankQueue.shutdown();
        groceryQueues.shutdown();
        
        try {
            Thread.sleep(1000); // Give time for final processing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printStatistics();
    }

    private void printStatistics() {
        System.out.println("\nSimulation Results:");
        System.out.println("Total customers: " + totalCustomers.get());
        System.out.println("Customers served: " + servedCustomers.get());
        System.out.println("Customers who left: " + leftCustomers.get());
    }

    public void customerServed() {
        servedCustomers.incrementAndGet();
    }

    public void customerLeft() {
        leftCustomers.incrementAndGet();
    }
}