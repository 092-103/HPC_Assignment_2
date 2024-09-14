public class Customer {
    private final long arrivalTime;
    private final int serviceTime;
    private boolean wasServed;
    private long departureTime;
    private boolean leftWithoutService;

    public Customer(long arrivalTime, int serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.wasServed = false;
        this.leftWithoutService = false;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public boolean wasServed() {
        return wasServed;
    }

    public void setWasServed(boolean wasServed) {
        this.wasServed = wasServed;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public boolean leftWithoutService() {
        return leftWithoutService;
    }

    public void setLeftWithoutService(boolean leftWithoutService) {
        this.leftWithoutService = leftWithoutService;
    }
}
