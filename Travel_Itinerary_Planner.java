import java.util.*;
import java.io.*;
import java.sql.*;

class Travel_Itinerary_Planner {
    static String dburl;
    static String dbuser;
    static String dbpass;
    static String driver;
    static Connection con;
    static Statement st;
    static PreparedStatement ps;
    static Thread t;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        t = Thread.currentThread();
        // Connection with database
        dburl = "jdbc:mysql://localhost:3306/Travellers";
        dbuser = "root";
        dbpass = "";
        driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        con = DriverManager.getConnection(dburl, dbuser, dbpass);
        st = con.createStatement();
        if (con != null) {
            System.out.println("Connection Successfull!");
        } else {
            System.out.println("connection FAILED!!");
        }

        // creation of table of contents of travel package
        String sql = "create table if not exists TravelItinerary(Sr_no int primary key auto_increment, Destination Varchar(30), Date date,Duration varchar(30), Tour_Price varchar(30),Mode_of_Transport varchar(30),Schedule longtext,Attractions Blob)";
        ps = con.prepareStatement(sql);
        // st.execute(sql);

        // creation of table of applicant details
        String sql4 = "create table if not exists Bookings (Sr_no int Primary key auto_increment,Applicant_Name varchar(30),ContactNo varchar(10) Unique key,EmailId varchar(50) Unique key,SelectedPackage int,AadharCardPic blob)";
        ps = con.prepareStatement(sql4);
        // st.execute(sql4);

        // insertion of data
        String sql1 = "insert into TravelItinerary(Destination,Date,Duration,Tour_Price,Mode_of_Transport,Schedule,Attractions) values(?,?,?,?,?,?,?)";
        ps = con.prepareStatement(sql1);

        /*
         * // 1. shimla
         * ps.setString(1, "Shimla");
         * ps.setDate(2,java.sql.Date.valueOf("2023-11-06"));
         * ps.setString(3, "8 Days");
         * ps.setString(4, "Rs. 23,500/-");
         * ps.setString(5, "3AC Train");
         * File f = new File("D:\\java programming\\practice\\SHIMLA.txt");
         * FileReader fr = new FileReader(f);
         * ps.setClob(6, fr);
         * File f1 = new File("D:\\java programming\\practice\\Shimla_Image.jpeg");
         * FileInputStream fis = new FileInputStream(f1);
         * ps.setBlob(7, fis);
         * int r = ps.executeUpdate();
         * if (r > 0) {
         * System.out.println("text and image inserted");
         * } else {
         * System.out.println("failed");
         * }
         */

        do {
            System.out.println("#####################  Welcome to Travelers Guide  #####################");
            System.out.println("1. See Plans \n2. Create Your own Plan \n3. Book Plan \n4. Cancel plan \n5. Exit");
            int ch = sc.nextInt();
            sc.nextLine();
            switch (ch) {
                case 1:
                    SeePlans();
                    break;
                case 2:
                    CreateOwnPlan();
                    break;
                case 3:
                    BookPlan();
                    break;
                case 4:
                    CancelPlan();
                    break;
                case 5:
                    System.out.println("Thank You :) ");
                    System.exit(0);
                default:
                    System.out.println("enter valid option!");
                    break;
            }
            t.sleep(1000);
        } while (true);
    }

    static void SeePlans() throws Exception {
        String sql2 = "select * from TravelItinerary";
        ResultSet r = st.executeQuery(sql2);
        while (r.next()) {
            System.out.println("sr no.:      " + r.getInt(1));
            System.out.println("Destination: " + r.getString(2));
            System.out.println("Date:        " + r.getDate(3));
            System.out.println("Duration:    " + r.getString(4));
            System.out.println("Total cost:  " + r.getString(5));
            System.out.println("Transport:   " + r.getString(6));
            System.out.println("--------------------------------------------------");
        }
        System.out.println("Press sr no. of destination whoes Schedule and Image you wish to see else press 0");
        int srn = sc.nextInt();
        if (srn == 0) {
            return;
        } else {
            // for schedule(clob)
            // writing results to a file
            File f1 = new File("D:\\java programming\\practice\\User's Folder\\ScheduleDownloaded.txt");
            FileWriter fw = new FileWriter(f1);

            String sql1 = "select Schedule from TravelItinerary where Sr_no= " + srn;
            ps = con.prepareStatement(sql1);
            ResultSet r1 = ps.executeQuery();
            while (r1.next()) {
                Reader c = r1.getCharacterStream(1);
                int i = c.read();
                while (i != -1) {
                    fw.write((char) i);
                    i = c.read();
                }
            }
            fw.flush();
            fw.close();

            // for Image(blob)
            // writing results to a file
            File f2 = new File("D:\\java programming\\practice\\User's Folder\\AttractionDownloaded.jpg");
            FileOutputStream fos = new FileOutputStream(f2);
            String sql3 = "select Attractions from TravelItinerary where Sr_no= " + srn;
            ps = con.prepareStatement(sql3);
            ResultSet r2 = ps.executeQuery();
            while (r2.next()) {
                Reader c = r2.getCharacterStream(1);
                if (c == null) {
                    return;
                }
                int i = c.read();
                while (i != -1) {
                    fos.write((char) i);
                    i = c.read();
                }
            }
            fos.flush();
            fos.close();

            System.out.println("Schedule and image downloaded!");
        }
    }

    static void CreateOwnPlan() throws Exception {
        String sql3 = "insert into TravelItinerary(Destination,Date,Duration,Mode_of_Transport,Schedule,Attractions) values(?,?,?,?,?,?)";
        ps = con.prepareStatement(sql3);

        String location[] = new String[] { "Udaipur", "Jaipur", "Goa", "Coorg" };
        System.out.println("We currently have the following locations available for tour : ");
        for (int i = 0; i < location.length; i++) {
            System.out.println((i + 1) + " " + location[i]);
        }
        System.out.println("enter Destination no.: ");
        int cdes = sc.nextInt();
        if (cdes == 1) {
            ps.setString(1, "Udaipur");
            File f1 = new File("D:\\java programming\\practice\\udaipur_Image.jpg");
            FileInputStream fis = new FileInputStream(f1);
            ps.setBlob(6, fis);
        } else if (cdes == 2) {
            ps.setString(1, "Jaipur");
            File f1 = new File("D:\\java programming\\practice\\Jaipur_Image.jpeg");
            FileInputStream fis = new FileInputStream(f1);
            ps.setBlob(6, fis);
        } else if (cdes == 3) {
            ps.setString(1, "Goa");
            File f1 = new File("D:\\java programming\\practice\\Goa_Image.jpeg");
            FileInputStream fis = new FileInputStream(f1);
            ps.setBlob(6, fis);
        } else if (cdes == 4) {
            ps.setString(1, "Coorg");
            File f1 = new File("D:\\java programming\\practice\\Coorg_Image.jpg");
            FileInputStream fis = new FileInputStream(f1);
            ps.setBlob(6, fis);
        } else {
            System.out.println("enter valid location no.!");
            return;
        }
        System.out.println("enter date(YYYY-MM-DD): ");
        sc.nextLine();
        java.util.Date date = new java.util.Date();
        ps.setDate(2, new java.sql.Date(date.getTime()));
        sc.nextLine();
        System.out.println("enter duration: ");
        String dur = sc.nextLine();
        ps.setString(3, dur);

        System.out.println("enter Mode of Transport: ");
        String trans = sc.nextLine();
        ps.setString(4, trans);

        System.out.println("rough schedule is inserted ");
        File f = new File("D:\\java programming\\practice\\COMMON_SCHEDULE.txt");
        FileReader fr = new FileReader(f);
        ps.setClob(5, fr);
        int r = ps.executeUpdate();
        if (r > 0) {
            System.out.println("Plan Created");
        } else {
            System.out.println("creation failed!!");
        }
    }

    static void BookPlan() throws Exception {
        String sql5 = "insert into Bookings(Applicant_Name,ContactNo,EmailId,SelectedPackage,AadharCardPic) values (?,?,?,?,?)";
        ps = con.prepareStatement(sql5);

        System.out.println("enter name: ");
        String aname = sc.nextLine();
        ps.setString(1, aname);
        // sc.nextLine();

        while (true) {
            try {
                System.out.print("Enter a 10-digit phone number: ");
                String phoneNumber = sc.nextLine();

                if (isValidPhoneNumber(phoneNumber)) {
                    System.out.println("Valid phone number: " + phoneNumber);
                    ps.setString(2, phoneNumber);
                    break; // Exit the loop when a valid number is entered
                } else {
                    System.out.println("Invalid phone number. Please enter a 10-digit number.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }

        System.out.println("enter EmailId: ");
        String aeid = sc.nextLine();
        ps.setString(3, aeid);

        System.out.println("enter plan sr_no.: ");
        int aplan = sc.nextInt();
        ps.setInt(4, aplan);
        sc.nextLine();

        System.out.println("insert aadharcard photo address: ");
        // String Aadhar = sc.nextLine();
        // "D:\\java programming\\practice\\AadharSample1.jpg"
        File f2 = new File("D:\\java programming\\practice\\AadharSample1.jpg");
        FileInputStream fis1 = new FileInputStream(f2);
        ps.setBlob(5, fis1);

        int r = ps.executeUpdate();
        if (r > 0) {
            System.out.println("Booking confirmed");
        } else {
            System.out.println("Booking failed!!");
        }
    }

    static void CancelPlan() throws Exception {
        System.out.println("To cancel booking please enter ur contact no.: ");
        String acon;

        String sql6 = "Delete from bookings where ContactNo = ? ";
        ps = con.prepareStatement(sql6);

        while (true) {
            try {
                System.out.print("Enter a 10-digit phone number: ");
                acon = sc.nextLine();

                if (isValidPhoneNumber(acon)) {
                    System.out.println("Valid phone number: " + acon);
                    ps.setString(1, acon);
                    break; // Exit the loop when a valid number is entered
                } else {
                    System.out.println("Invalid phone number. Please enter a 10-digit number.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        int r3 = ps.executeUpdate();
        if (r3 > 0) {
            System.out.println("booking cancelled successfully.");
        } else {
            System.out.println("Errorr in cancelling the booking");
        }
    }

    //method to check correct 10 digit contact number
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Remove spaces and hyphens (if any) from the input
        phoneNumber = phoneNumber.replaceAll("[\\s-]+", "");

        // Check if the cleaned input consists only of digits and has a length of 10
        if (phoneNumber.length() == 10 && phoneNumber.chars().allMatch(Character::isDigit)) {
            return true;
        } else {
            return false;
        }
    }
}