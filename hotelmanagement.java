import java.util.Scanner;
import java.sql.*;

public class Hotelregistration {

    private static final String url = "jdbc:mysql://localhost:3306/hotel";
    private static final String uname = "root";
    private static final String password = "Chintu@123";

    // reserve room
    private static void reservationRoom(Connection con, Scanner sc) {
        try {
            sc.nextLine(); // clear buffer after previous nextInt()
            System.out.println("Enter guest name:");
            String guest_name = sc.nextLine();

            System.out.println("Enter room number:");
            int room_num = sc.nextInt();

            System.out.println("Enter contact number:");
            long contact = sc.nextLong();

            String sql = "INSERT INTO reservation(guest_name, room_num, contact) VALUES (?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, guest_name);
                ps.setInt(2, room_num);
                ps.setLong(3, contact);

                int row = ps.executeUpdate();
                if (row > 0) {
                    System.out.println("Reserved successfully.");
                } else {
                    System.out.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error in reservation: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // view all rooms / reservations
    private static void view_room(Connection con) {
        String sql = "SELECT * FROM reservation";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String guest_name = rs.getString("guest_name");
                int room_num = rs.getInt("room_num");
                long contact = rs.getLong("contact");
                String date = rs.getTimestamp("date").toString();

                System.out.println("ID: " + id +
                        " | Name: " + guest_name +
                        " | Room No: " + room_num +
                        " | Contact: " + contact +
                        " | Date: " + date);

            }

            if (!found) {
                System.out.println("No reservations found.");
            }

            System.out.println("---------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error while viewing reservations: " + e.getMessage());
        }
    }

    // get room details by room number
    private static void get_room(Connection con, Scanner sc) {
        String sql = "SELECT * FROM reservation WHERE room_num = ?";
        try {
            System.out.println("Enter room number:");
            int room = sc.nextInt();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, room);
                try (ResultSet rs = ps.executeQuery()) {
                    boolean found = false;

                    while (rs.next()) {
                        found = true;
                        int id = rs.getInt("id");
                        String name = rs.getString("guest_name");
                        long contact = rs.getLong("contact");

                        System.out.println("ID: " + id);
                        System.out.println("Guest: " + name);
                        System.out.println("Contact: " + contact);
                        System.out.println("--------------------------");
                    }

                    if (!found) {
                        System.out.println("No reservation found for room: " + room);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while getting room details: " + e.getMessage());
        }
    }

    // update reservation by id
    private static void update(Connection con, Scanner sc) {
        String sql = "UPDATE reservation SET guest_name = ?, room_num = ?, contact = ? WHERE id = ?";
        try {
            System.out.println("Enter reservation ID to update:");
            int id = sc.nextInt();
            sc.nextLine(); // clear buffer

            // Optional: show old details
            System.out.println("Enter new guest name:");
            String newName = sc.nextLine();

            System.out.println("Enter new room number:");
            int newRoom = sc.nextInt();

            System.out.println("Enter new contact number:");
            long newContact = sc.nextLong();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, newName);
                ps.setInt(2, newRoom);
                ps.setLong(3, newContact);
                ps.setInt(4, id);

                int row = ps.executeUpdate();
                if (row > 0) {
                    System.out.println("Reservation updated successfully.");
                } else {
                    System.out.println("No reservation found with ID: " + id);
                }
                System.out.println("view update reservation");
                view_room(con);
            }
        } catch (SQLException e) {
            System.out.println("Error while updating reservation: " + e.getMessage());
        }
    }

    // delete reservation by id
    private static void delete(Connection con, Scanner sc) {
        System.out.println("Enter reservation ID to delete:");
        int rid = sc.nextInt();
        String sql = "DELETE FROM reservation WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rid);
            int row = ps.executeUpdate();
            if (row > 0) {
                System.out.println("Deleted successfully.");
            } else {
                System.out.println("No record found with ID: " + rid);
            }

            // show all records after delete
            System.out.println("Current reservations:");
            view_room(con);

        } catch (SQLException e) {
            System.out.println("Error while deleting reservation: " + e.getMessage());
        }
    }

    private static void exit() {
        System.out.println("Exiting... Thank you for using Hotel Management System.");
    }

    public static void main(String args[]) {
        try {
            // correct driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
        } catch (Exception e) {
            System.out.println("Driver loading error: " + e.getMessage());
        }

        try (Connection con = DriverManager.getConnection(url, uname, password);
             Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n===== HOTEL MANAGEMENT SYSTEM =====");
                System.out.println("1. Reserve a room");
                System.out.println("2. View reservations");
                System.out.println("3. Get room details");
                System.out.println("4. Update reservation");
                System.out.println("5. Delete reservation");
                System.out.println("0. Exit");
                System.out.print("Select a number: ");

                int choose = sc.nextInt();

                switch (choose) {
                    case 1:
                        reservationRoom(con, sc);
                        break;
                    case 2:
                        view_room(con);
                        break;
                    case 3:
                        get_room(con, sc);
                        break;
                    case 4:
                        update(con, sc);
                        break;
                    case 5:
                        delete(con, sc);
                        break; // important break
                    case 0:
                        exit();
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Connection / SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
