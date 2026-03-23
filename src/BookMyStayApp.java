import java.util.*;

// Reservation class (core booking entity)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double cost;

    public Reservation(String reservationId, String guestName, String roomType, double cost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.cost = cost;
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

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | ₹" + cost;
    }
}

// Booking History (stores confirmed bookings)
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addBooking(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getBookings() {
        return history;
    }
}

// Reporting Service (separate from storage)
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> bookings) {
        System.out.println("=== Booking History ===");

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> bookings) {
        int totalBookings = bookings.size();
        double totalRevenue = 0;

        for (Reservation r : bookings) {
            totalRevenue += r.getCost();
        }

        System.out.println("\n=== Summary Report ===");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Initialize booking history
        BookingHistory bookingHistory = new BookingHistory();

        // Step 2: Simulate confirmed bookings
        Reservation b1 = new Reservation("R001", "Arun", "Deluxe", 3000);
        Reservation b2 = new Reservation("R002", "Meena", "Suite", 5000);
        Reservation b3 = new Reservation("R003", "Karthik", "Standard", 2000);

        // Step 3: Add bookings to history (in order)
        bookingHistory.addBooking(b1);
        bookingHistory.addBooking(b2);
        bookingHistory.addBooking(b3);

        // Step 4: Admin requests reports
        BookingReportService reportService = new BookingReportService();

        // Display all bookings
        reportService.showAllBookings(bookingHistory.getBookings());

        // Generate summary
        reportService.generateSummary(bookingHistory.getBookings());
    }
}