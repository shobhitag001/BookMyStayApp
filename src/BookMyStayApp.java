import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType +
                " | RoomID: " + roomId +
                " | Status: " + (isCancelled ? "Cancelled" : "Active");
    }
}

// Inventory Manager
class InventoryManager {
    private Map<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    public void allocate(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void release(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void showInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking Store (acts like history + active records)
class BookingStore {
    private Map<String, Reservation> bookings;

    public BookingStore() {
        bookings = new HashMap<>();
    }

    public void addReservation(Reservation res) {
        bookings.put(res.getReservationId(), res);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void showAll() {
        System.out.println("=== Booking Records ===");
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service (core logic)
class CancellationService {

    // Stack for rollback tracking (LIFO)
    private Stack<String> releasedRoomStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingStore store,
                              InventoryManager inventory)
            throws CancellationException {

        // Step 1: Validate existence
        Reservation res = store.getReservation(reservationId);
        if (res == null) {
            throw new CancellationException("Reservation not found: " + reservationId);
        }

        // Step 2: Validate not already cancelled
        if (res.isCancelled()) {
            throw new CancellationException("Reservation already cancelled: " + reservationId);
        }

        // Step 3: Record room ID (for rollback tracking)
        releasedRoomStack.push(res.getRoomId());

        // Step 4: Restore inventory
        inventory.release(res.getRoomType());

        // Step 5: Mark reservation cancelled
        res.cancel();

        System.out.println("Cancellation successful for: " + reservationId);
        System.out.println("Rolled back Room ID: " + releasedRoomStack.peek());
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        BookingStore store = new BookingStore();
        CancellationService cancelService = new CancellationService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("R001", "Arun", "Standard", "S101");
        Reservation r2 = new Reservation("R002", "Meena", "Deluxe", "D201");

        // Allocate inventory (simulate booking confirmation)
        inventory.allocate("Standard");
        inventory.allocate("Deluxe");

        store.addReservation(r1);
        store.addReservation(r2);

        // Initial state
        inventory.showInventory();
        store.showAll();

        // Cancellation scenarios
        String[] cancelRequests = {"R002", "R002", "R003"};

        for (String id : cancelRequests) {
            System.out.println("\nAttempting cancellation: " + id);

            try {
                cancelService.cancelBooking(id, store, inventory);
            } catch (CancellationException e) {
                System.out.println("Cancellation Failed: " + e.getMessage());
            }

            inventory.showInventory();
        }

        // Final state
        System.out.println("\nFinal Booking Status:");
        store.showAll();
    }
}