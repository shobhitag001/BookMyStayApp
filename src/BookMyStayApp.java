import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String reservationId;
    private final String guestName;
    private final String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Wrapper class to persist entire system state
class SystemState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with clean system.");
            return null;
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        Map<String, Integer> inventory;
        List<Reservation> bookings;

        // Step 1: Load previous state
        SystemState loadedState = PersistenceService.load();

        if (loadedState != null) {
            inventory = loadedState.inventory;
            bookings = loadedState.bookings;
        } else {
            // Initialize fresh system
            inventory = new HashMap<>();
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 1);

            bookings = new ArrayList<>();
        }

        // Step 2: Display current state
        System.out.println("\n=== Current System State ===");
        System.out.println("Inventory: " + inventory);
        System.out.println("Bookings: " + bookings);

        // Step 3: Simulate new booking
        System.out.println("\nProcessing new booking...");

        String roomType = "Standard";

        if (inventory.getOrDefault(roomType, 0) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);

            Reservation res = new Reservation("R" + (bookings.size() + 1),
                    "Guest" + (bookings.size() + 1),
                    roomType);

            bookings.add(res);

            System.out.println("Booking successful: " + res);
        } else {
            System.out.println("No rooms available for booking.");
        }

        // Step 4: Save updated state before shutdown
        SystemState newState = new SystemState(inventory, bookings);
        PersistenceService.save(newState);

        // Step 5: Final state display
        System.out.println("\n=== Final System State ===");
        System.out.println("Inventory: " + inventory);
        System.out.println("Bookings: " + bookings);
    }
}