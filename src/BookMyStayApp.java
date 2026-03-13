abstract class Room {

    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Room Size: " + size + " sq.ft");
        System.out.println("Price per Night: ₹" + price);
    }
}


// Single Room
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200, 2500);
    }
}


// Double Room
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350, 4000);
    }
}


// Suite Room
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600, 7500);
    }
}



// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        Book My Stay App");
        System.out.println("           Version 2.1");
        System.out.println("=================================\n");

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display Single Room
        System.out.println("---- Single Room ----");
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);
        System.out.println();

        // Display Double Room
        System.out.println("---- Double Room ----");
        dbl.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);
        System.out.println();

        // Display Suite Room
        System.out.println("---- Suite Room ----");
        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);
    }
}