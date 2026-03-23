import java.util.*;

// Booking Request
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Shared Inventory (Thread-safe)
class InventoryManager {
    private Map<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Critical Section
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " allocating " + roomType + " room...");
            inventory.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    public void showInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.offer(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryManager inventory;

    public BookingProcessor(String name, BookingQueue queue, InventoryManager inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // Fetch request safely
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) break;

            // Allocate room (critical section inside method)
            boolean success = inventory.allocateRoom(request.getRoomType());

            if (success) {
                System.out.println(getName() + " SUCCESS: " +
                        request.getGuestName() + " booked " + request.getRoomType());
            } else {
                System.out.println(getName() + " FAILED: No " +
                        request.getRoomType() + " rooms for " + request.getGuestName());
            }

            try {
                Thread.sleep(100); // simulate processing delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple guest requests
        queue.addRequest(new BookingRequest("Arun", "Standard"));
        queue.addRequest(new BookingRequest("Meena", "Standard"));
        queue.addRequest(new BookingRequest("Karthik", "Standard")); // Should fail
        queue.addRequest(new BookingRequest("Divya", "Deluxe"));
        queue.addRequest(new BookingRequest("Ravi", "Deluxe")); // Should fail

        // Create multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);
        BookingProcessor t3 = new BookingProcessor("Thread-3", queue, inventory);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to complete
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory state
        System.out.println("\n=== Final System State ===");
        inventory.showInventory();
    }
}