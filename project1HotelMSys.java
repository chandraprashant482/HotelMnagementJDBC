import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
public class project1HotelMSys {
    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username="root";
    private static final String password="231199";
    public static void main(String[] args){
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
            Connection con=DriverManager.getConnection(url,username,password);
            while (true)
            {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc=new Scanner(System.in);
                System.out.println("1. Reserve Room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println("ENTER YOUR CHOICE");
                int choice =sc.nextInt();
                switch (choice)
                {
                    case 1:
                        reserveRoom(con,sc);
                        break;
                    case 2:
                        viewReservation(con);
                        break;
                    case 3:
                        getRoomNumbaer(con,sc);
                        break;
                    case 4:
                        updateReservation(con,sc);
                        break;
                    case 5:
                        deleteReservation(con,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("INVALID CHOICE.....TRY AGAIN");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

    }
    private static void reserveRoom(Connection con,Scanner sc)
    {
        System.out.println("ENTER GUEST NAME");
        String guestName=sc.next();
        sc.nextLine();
        System.out.println("ENTER ROOM NUMBER");
        int roomNumber=sc.nextInt();
        System.out.println("ENTER CONTACT NUMBER");
        String contactNumber=sc.next();
        sc.nextLine();
        String sql="INSERT INTO reservations (guest_name,room_number,contact_number) " +
                "VALUES('"+guestName +"' , "+ roomNumber +" , '"+ contactNumber +"')";
        try(Statement stmt= con.createStatement()) {
            int rowsAffectedn=stmt.executeUpdate(sql);
            if (rowsAffectedn>0)
            {
                System.out.println("RESERVATION DONE");
            }
            else
            {
                System.out.println("RESERVATION FAIL ..TRY AGAIN");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private  static void viewReservation(Connection con) throws SQLException
    {
        String query="select * from reservations";
        try (Statement stmt =con.createStatement();
        ResultSet rs=stmt.executeQuery(query)){
            System.out.println("CURRENT RESERVATION");

            while (rs.next())
            {
                System.out.println("===================================+======================================================");
                int id=rs.getInt("reservation_id");
                String name =rs.getString("guest_name");
                int room=rs.getInt("room_number");
                String contact=rs.getString("contact_number");
                String rdate=rs.getString("reservation_date").toString();
                System.out.println(id+" " +name+ " " +room+ " " +contact+ " " +rdate);
            }
            System.out.println("================+===================+=============+==================+=====+==================");
        }

    }
    private static void getRoomNumbaer(Connection con,Scanner sc)
    {
        try
        {
            System.out.println("ENTER RESERVATION ID");
            int reservationId =sc.nextInt();
            System.out.println("ENTER GUEST NAME");
            String name=sc.next();
            sc.nextLine();
            String sql="SELECT room_number FROM reservations WHERE reservation_id =  "+reservationId+ " AND guest_name ="+name;
            try (Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(sql))
            {
                if (rs.next())
                {
                    int rowaf=rs.getInt("room_number");
                    System.out.println(reservationId+ " " +name+ " "+ rowaf);
                }
                else
                {
                    System.out.println("RESERVATION NOT FOUND");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection con,Scanner sc)
    {
        try {
            System.out.println("Enter Reservation ID for update ");
            int resid=sc.nextInt();
            sc.nextLine();
            if (!resideExists(con ,resid))
            {
                System.out.println("Reservation not found");
                return;
            }
            System.out.println("Enter new name");
            String newname=sc.next();
            sc.nextLine();
            System.out.println("Enter new room number");
            int newroom=sc.nextInt();
            System.out.println("Enter new phone number");
            String newcon=sc.next();

            String sql="UPDATE reservations SET guest_name="+newname +","+ "room_number=" +newroom +","+ "contact_number=" +newcon + " WHERE reservation_id=" +resid;
           // String sql = "UPDATE reservations SET guest_name = '" + newname + "', room_number = " + newroom + ", contact_number = '" + newcon + "' WHERE reservation_id = " + resid;

            try(Statement sts=con.createStatement())
            {
                int rowaff=sts.executeUpdate(sql);
                if (rowaff>0)
                {
                    System.out.println("Reservation updated");
                }
                else {
                    System.out.println("update fail");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private static void deleteReservation(Connection con,Scanner sc)
    {
        try {
            System.out.println("enter reservation id to delete record");
            int resdel= sc.nextInt();
            sc.nextLine();
            if (!resideExists(con , resdel))
            {
                System.out.println("Reservation not found");
                return;
            }
            String sql="DELETE FROM reservations WHERE reservation_id =" +resdel;
            try (Statement sts=con.createStatement())
            {

                int rowaff=sts.executeUpdate(sql);
                if (rowaff>0)
                {
                    System.out.println("reservation deleted");
                }
                else
                {
                    System.out.println("deletetion failed");
                }

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private static boolean resideExists(Connection con, int reservationId)
    {
        try {
            String sql="SELECT reservation_id FROM reservations WHERE reservation_id="+reservationId;
            try (Statement sts=con.createStatement();
                 ResultSet rs=sts.executeQuery(sql)){
                return rs.next();

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    private static void exit() throws InterruptedException
    {
        System.out.println("EXITING SYSTEM");
        int i=5;
        while (i!=0)
        {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("THANK YOU FOR USING ME");
    }
}
