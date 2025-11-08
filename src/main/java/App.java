import java.sql.*;
import java.util.Scanner;

public class App {
    private static final String url = "jdbc:mysql://localhost:3306/hotel";
    private static final String user = "root";
    private static final String pass = "Benzeneking1";

    public static void main(String[] args) throws SQLException,ClassNotFoundException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url,user,pass);
            Statement statement = connection.createStatement();
            while(true){
                Scanner scanner = new Scanner(System.in);
                System.out.println("-----------------------HOTEL RESERVATION----------------------");
                System.out.println("1.reservation");
                System.out.println("2.view reservation");
                System.out.println("3.update reservation");
                System.out.println("4.delete reservation");
                System.out.println("5.exit");
                System.out.print("Enter choice --> ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice){
                    case 1->reservation(scanner,statement);
                    case 2->viewReservation(statement);
                    case 3->update(scanner,statement);
                    case 4->delete(scanner,statement);
                    case 5->{
                        System.out.println("exited.......");
                        return;
                    }
                    default ->{
                        System.out.println("invalid choice");
                    }
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void reservation(Scanner sc,Statement statement){
        System.out.print("Enter guest_name -> ");
        String name = sc.nextLine();
        System.out.print("Enter room_num -> ");
        String room = sc.nextLine();
        System.out.print("Enter contact_num -> ");
        String contact = sc.nextLine();
        String query = "INSERT INTO reservation (guest_name, room_number, contact) VALUES ('"
                + name + "', '" + room + "', '" + contact + "')";
        try{
            int rowsAffected = statement.executeUpdate(query);
            if(rowsAffected >0){
                System.out.println("Reservation secured.");
            }else {
                System.out.println("not successful");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void viewReservation(Statement statement){
        String query = "select * from reservation";
        try{
            ResultSet rs = statement.executeQuery(query);
            System.out.println("-------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-10s %-12s %-25s%n",
                    "ID", "Guest Name", "Room", "Contact", "Reservation Date");
            System.out.println("-------------------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("id");
                String guestName = rs.getString("guest_name");
                String roomNumber = rs.getString("room_number");
                String contact = rs.getString("contact");
                Timestamp date = rs.getTimestamp("reservation_date");
                System.out.printf("%-5d %-20s %-10s %-12s %-25s%n",
                        id, guestName, roomNumber, contact, date);
            }
            System.out.println("-------------------------------------------------------------");
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void update(Scanner sc,Statement statement){
        System.out.print("Enter id -> ");
        int reservation_id = sc.nextInt();
        sc.nextLine();
        if(!reservationExists(statement,reservation_id)){
            return;
        }
        System.out.print("Enter new name-> ");
        String newName = sc.nextLine();
        System.out.print("Enter new room_number-> ");
        String newRoom = sc.nextLine();
        System.out.print("Enter new contact_number-> ");
        String newNumber = sc.nextLine();
        String query = "UPDATE reservation SET guest_name = '" + newName
                + "', room_number = '" + newRoom
                + "', contact = '" + newNumber
                + "' WHERE id = " + reservation_id;
        try{
            int rowsAffected = statement.executeUpdate(query);
            if(rowsAffected>0){
                System.out.println("updated!");
            }else{
                System.out.println("update failed");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void delete(Scanner scanner,Statement statement){
        System.out.print("Enter reservation id-> ");
        int id = scanner.nextInt();
        if(!reservationExists(statement,id)){
            return;
        }
        String query = "DELETE FROM reservation WHERE id = " + id;
        try{
            int rowsAffected = statement.executeUpdate(query);
            if(rowsAffected>0){
                System.out.println("Deleted!");
            }else{
                System.out.println("Deletion failed.");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static boolean reservationExists(Statement statement,int id){
        String query = "select id from reservation where id="+id;
        try{
            ResultSet isReserved = statement.executeQuery(query);
            if(!isReserved.next()){
                System.out.println("Doesn't exist.");
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }
}
