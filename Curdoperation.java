import java.sql.*;
import java.util.Scanner;

public class Curdoperation {

    private static final String URL = "jdbc:mysql://localhost:3306/kumar";
    private static final String USER = "root";
    private static final String PASS = "*******";

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } 
        catch (Exception e) { e.printStackTrace(); }
    }

    private static Connection getCon() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    private static void insert(Scanner sc) throws Exception {
        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Branch: ");
        String branch = sc.nextLine();

        String q = "INSERT INTO std VALUES(?, ?, ?, ?)";
        try (Connection con = getCon(); PreparedStatement pst = con.prepareStatement(q)) {
            pst.setInt(1, id);
            pst.setString(2, name);
            pst.setInt(3, age);
            pst.setString(4, branch);
            pst.executeUpdate();
            System.out.println("Inserted.");
        }
    }

    private static void read() throws Exception {
        String q = "SELECT * FROM std";
        try (Connection con = getCon(); PreparedStatement pst = con.prepareStatement(q); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getInt(3) + " | " + rs.getString(4));
            }
        }
    }

    private static void update(Scanner sc) throws Exception {
        System.out.print("ID to update: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("New Name: ");
        String name = sc.nextLine();
        System.out.print("New Age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("New Branch: ");
        String branch = sc.nextLine();

        String q = "UPDATE std SET name=?, age=?, branch=? WHERE id=?";
        try (Connection con = getCon(); PreparedStatement pst = con.prepareStatement(q)) {
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, branch);
            pst.setInt(4, id);
            pst.executeUpdate();
            System.out.println("Updated.");
        }
    }

    private static void delete(Scanner sc) throws Exception {
        System.out.print("ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        String q = "DELETE FROM std WHERE id=?";
        try (Connection con = getCon(); PreparedStatement pst = con.prepareStatement(q)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Deleted.");
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Insert  2.Read  3.Update  4.Delete  5.Exit");
            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> insert(sc);
                case 2 -> read();
                case 3 -> update(sc);
                case 4 -> delete(sc);
                case 5 -> { System.out.println("Bye"); return; }
                default -> System.out.println("Invalid");
            }
        }
       
    }
}
