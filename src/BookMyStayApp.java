import java.util.*;

// Custom Exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Inventory Manager (controls room availability)
class InventoryManager {
    private Map<String, Integer> roomInventory;

    public InventoryManager() {
        roomInventory = new HashMap<>();

        // Initial room availability
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 1);
        roomInventory.put("Suite", 0);
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Check availability
    public void validateAvailability(String roomType) throws InvalidBookingException {
        int available = roomInventory.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }

    // Allocate room (only after validation)
    public void allocateRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    // Display inventory
    public void showInventory() {
        System.out.println("Current Inventory: " + roomInventory);
    }
}

// Validator class (Fail-Fast validation)
class InvalidBookingValidator {

    public static void validate(String guestName, String roomType, InventoryManager inventory)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        inventory.validateRoomType(roomType);
        inventory.validateAvailability(roomType);
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();

        // Test scenarios
        String[][] testBookings = {
                {"R001", "Arun", "Deluxe"},     // Valid
                {"R002", "", "Standard"},      // Invalid guest name
                {"R003", "Meena", "Premium"},  // Invalid room type
                {"R004", "Karthik", "Suite"}   // No availability
        };

        for (String[] booking : testBookings) {
            String id = booking[0];
            String guest = booking[1];
            String room = booking[2];

            System.out.println("\nProcessing Booking: " + id);

            try {
                // Step 1: Validate input (Fail-Fast)
                InvalidBookingValidator.validate(guest, room, inventory);

                // Step 2: Allocate room only if valid
                inventory.allocateRoom(room);

                // Step 3: Create reservation
                Reservation reservation = new Reservation(id, guest, room);

                System.out.println("Booking Successful: " + reservation);

            } catch (InvalidBookingException e) {
                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            }

            // System continues safely
            inventory.showInventory();
        }
    }
}