public class Main {
    public static void main(String[] args) {
        int simulationTimeInSeconds = 360; // 2 minutes
        int numTellers = 3;
        int numCashiers = 3;
        int maxBankQueueLength = 5;
        int maxGroceryQueueLength = 2;

        QueueSimulator simulator = new QueueSimulator(simulationTimeInSeconds, numTellers, numCashiers, maxBankQueueLength, maxGroceryQueueLength);
        simulator.runSimulation();
    }
}