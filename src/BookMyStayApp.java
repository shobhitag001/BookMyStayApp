import java.util.*;

// Represents an Add-On Service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages mapping between Reservation and Services
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add services to a reservation
    public void addServices(String reservationId, List<AddOnService> services) {
        reservationServicesMap.putIfAbsent(reservationId, new ArrayList<>());
        reservationServicesMap.get(reservationId).addAll(services);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of add-ons
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = reservationServicesMap.get(reservationId);
        if (services == null) return 0;

        double total = 0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Selected Add-On Services:");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Sample Reservation ID
        String reservationId = "RES123";

        // Create services
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1000);

        // Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        List<AddOnService> selectedServices = new ArrayList<>();
        selectedServices.add(wifi);
        selectedServices.add(breakfast);
        selectedServices.add(airportPickup);

        // Add services to reservation
        manager.addServices(reservationId, selectedServices);

        // Display result
        System.out.println("Reservation ID: " + reservationId);
        manager.displayServices(reservationId);
    }
}