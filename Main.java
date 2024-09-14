public class Main {
    public static void main(String[] args) {
        int simulationTimeInSeconds = 7200; // 2 hours = 7200 seconds
        int numTellers = 3;
        int numCashiers = 3;
        int maxBankQueueLength = 5;
        int maxGroceryQueueLength = 2;

        QueueSimulator simulator = new QueueSimulator(simulationTimeInSeconds, numTellers, numCashiers, maxBankQueueLength, maxGroceryQueueLength);
        simulator.runSimulation();
    }
}
