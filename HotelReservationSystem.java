package hotelReservationSystem;

import java.io.*;
import java.util.*;

class Room {
    String roomType;
    int roomNumber;
    boolean isBooked;

    Room(String roomType, int roomNumber) {
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.isBooked = false;
    }
}

class Booking {
    String guestName;
    int roomNumber;
    String roomType;
    String paymentStatus;

    Booking(String guestName, int roomNumber, String roomType, String paymentStatus) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return guestName + "," + roomNumber + "," + roomType + "," + paymentStatus;
    }
}

public class HotelReservationSystem {
    static List<Room> rooms = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeRooms();
        loadBookingsFromFile();

        while (true) {
            System.out.println("\nHotel Reservation System");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewAvailableRooms();
                    break;
                case 2:
                    bookRoom(scanner);
                    break;
                case 3:
                    cancelBooking(scanner);
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    saveBookingsToFile();
                    System.out.println("Exiting system...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    static void initializeRooms() {
        for (int i = 1; i <= 3; i++) {
            rooms.add(new Room("Standard", i));
            rooms.add(new Room("Deluxe", i + 3));
            rooms.add(new Room("Suite", i + 6));
        }
    }

    static void viewAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room room : rooms) {
            if (!room.isBooked) {
                System.out.println("Room Number: " + room.roomNumber + " | Type: " + room.roomType);
            }
        }
    }

    static void bookRoom(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter room type (Standard/Deluxe/Suite): ");
        String type = scanner.nextLine();

        for (Room room : rooms) {
            if (room.roomType.equalsIgnoreCase(type) && !room.isBooked) {
                room.isBooked = true;
                Booking booking = new Booking(name, room.roomNumber, type, "Paid");
                bookings.add(booking);
                System.out.println("Room booked successfully! Room Number: " + room.roomNumber);
                return;
            }
        }

        System.out.println("No available rooms for selected type.");
    }

    static void cancelBooking(Scanner scanner) {
        System.out.print("Enter your name to cancel booking: ");
        String name = scanner.nextLine();

        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            if (booking.guestName.equalsIgnoreCase(name)) {
                iterator.remove();

                for (Room room : rooms) {
                    if (room.roomNumber == booking.roomNumber && room.roomType.equals(booking.roomType)) {
                        room.isBooked = false;
                        break;
                    }
                }

                System.out.println("Booking cancelled for " + name);
                return;
            }
        }

        System.out.println("Booking not found.");
    }

    static void viewBookings() {
        System.out.println("\nAll Bookings:");
        for (Booking booking : bookings) {
            System.out.println("Name: " + booking.guestName + ", Room No: " + booking.roomNumber +
                    ", Type: " + booking.roomType + ", Payment: " + booking.paymentStatus);
        }
    }

    static void saveBookingsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Booking booking : bookings) {
                writer.println(booking);
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings.");
        }
    }

    static void loadBookingsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length == 4) {
                    Booking booking = new Booking(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]);
                    bookings.add(booking);
                    // Mark room as booked
                    for (Room room : rooms) {
                        if (room.roomNumber == booking.roomNumber && room.roomType.equals(booking.roomType)) {
                            room.isBooked = true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings.");
        }
    }
}