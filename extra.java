package gymmanagement;

import java.sql.*;
import java.util.*;

class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gym2";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() throws Exception {
        Class.forName(DRIVER_NAME);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}

class Member {
    String firstName, lastName, gender, email, phoneNumber, address, membershipType;
    int age;
    float height, weight;
    double membershipFee;

    public Member(String firstName, String lastName, int age, String gender, String phoneNumber, String email,
                  String address, float height, float weight, String membershipType, double membershipFee) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.height = height;
        this.weight = weight;
        this.membershipType = membershipType;
        this.membershipFee = membershipFee;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Member{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", membershipType='" + membershipType + '\'' +
                ", membershipFee=" + membershipFee +
                '}';
    }

    public void m_saveToDatabase(Connection con) throws SQLException {
        String sql = "INSERT INTO member (first_name, last_name, age, gender, phone_number, email, address, height, weight, membership_type, membership_fee) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setInt(3, age);
        pstmt.setString(4, gender);
        pstmt.setString(5, phoneNumber);
        pstmt.setString(6, email);
        pstmt.setString(7, address);
        pstmt.setFloat(8, height);
        pstmt.setFloat(9, weight);
        pstmt.setString(10, membershipType);
        pstmt.setDouble(11, membershipFee);
        pstmt.executeUpdate();
    }

  /*   public static void m_deleteFromDatabase(Connection con, String number) throws SQLException {
        String sql = "DELETE FROM member WHERE  phone_number = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1,number);
        pstmt.executeUpdate(); 
    }  */
}

class Membership {
    private static final Map<String, Double> membershipFees = new HashMap<>();

    static {
        membershipFees.put("general", 13999.0);
        membershipFees.put("master", 19999.0);
        membershipFees.put("expert", 24999.0);
        membershipFees.put("weightloss", 17999.0);
        membershipFees.put("weightgain", 17999.0);
    }

    public static double calculateFee(String membershipType, String paymentMethod) {
        double fee = membershipFees.getOrDefault(membershipType.toLowerCase(), 0.0);
        if (paymentMethod.equalsIgnoreCase("online")) {
            fee *= 0.95;  // 5% discount for online payment
        }
        return fee;
    }

    public static Set<String> getMembershipTypes() {
        return membershipFees.keySet();
    }
}

class Trainer {
    String t_firstName, t_lastName, t_gender, t_phoneNumber, t_email, t_address, t_special;
    int t_age, t_exp;

    public Trainer(String t_firstName, String t_lastName, int t_age, String t_gender, String t_phoneNumber, String t_email,
                   String t_address, int t_exp, String t_special) {
        this.t_firstName = t_firstName;
        this.t_lastName = t_lastName;
        this.t_gender = t_gender;
        this.t_phoneNumber = t_phoneNumber;
        this.t_email = t_email;
        this.t_address = t_address;
        this.t_special = t_special;
        this.t_age = t_age;
        this.t_exp = t_exp;
    }

    @Override
    public String toString() {
        return "Trainer [t_firstName=" + t_firstName + ", t_lastName=" + t_lastName + ", t_age=" + t_age + ", t_gender=" + t_gender
                + ", t_phoneNumber=" + t_phoneNumber + ", t_email=" + t_email + ", t_address=" + t_address
                + ", t_exp=" + t_exp + ", t_special=" + t_special + "]";
    }

    public void t_saveToDatabase(Connection con) throws SQLException {
        String sql = "INSERT INTO trainer (first_name, last_name, age, gender, phone_number, email, address, experience, speciality) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, t_firstName);
        pstmt.setString(2, t_lastName);
        pstmt.setInt(3, t_age);
        pstmt.setString(4, t_gender);
        pstmt.setString(5, t_phoneNumber);
        pstmt.setString(6, t_email);
        pstmt.setString(7, t_address);
        pstmt.setInt(8, t_exp);
        pstmt.setString(9, t_special);
        pstmt.executeUpdate();
    }
}

class UserInterface {
    Scanner scanner;
    Connection con;
    List<Member> members;
    List<Trainer> trainers;
    public UserInterface(Scanner scanner, Connection con) {
        this.scanner = scanner;
        this.con = con;
        this.members = new ArrayList<>();
        this.trainers = new ArrayList<>();
    }

    public void showMenu() {
        System.out.println("--------------------------------------------------------");
        System.out.println("                   Welcome to ABC Gym                    ");
        System.out.println("--------------------------------------------------------");
        System.out.println();
    }
    
    public void registerTrainer() throws SQLException {
        System.out.print("Enter your first name: ");
        String t_firstName = scanner.next();
        System.out.print("Enter your last name: ");
        String t_lastName = scanner.next();
        System.out.print("Enter your age: ");
        int t_age = scanner.nextInt();
        System.out.print("Enter your gender: ");
        String t_gender = validateGender();
        System.out.print("Enter your phone number: ");
        String t_phoneNumber = validatePhoneNumber();
        System.out.print("Enter your email ID: ");
        String t_email = scanner.next();
        System.out.print("Enter your address: ");
        String t_address = scanner.next();
        System.out.print("Enter your experience (in months) : ");
        int t_exp = scanner.nextInt();
        System.out.print("Enter your speciality: ");
        String t_special = scanner.next();
        Trainer trainer = new Trainer(t_firstName, t_lastName, t_age, t_gender, t_phoneNumber, t_email, t_address, t_exp, t_special);
        trainers.add(trainer); // Store member in the list
        trainer.t_saveToDatabase(con); // Persist member to the database

        System.out.println("Trainer registered successfully!");
    }

    public void registerMember() throws SQLException {
        System.out.print("Enter your first name: ");
        String m_firstName = scanner.next();
        System.out.print("Enter your last name: ");
        String m_lastName = scanner.next();
        System.out.print("Enter your age: ");
        int m_age = scanner.nextInt();
        System.out.print("Enter your gender: ");
        String m_gender = validateGender();
        System.out.print("Enter your phone number: ");
        String m_phoneNumber = validatePhoneNumber();
        System.out.print("Enter your email ID: ");
        String m_email = scanner.next();
        System.out.print("Enter your address: ");
        String m_address = scanner.next();
        System.out.print("Enter your height: ");
        float m_height = scanner.nextFloat();
        System.out.print("Enter your weight: ");
        float m_weight = scanner.nextFloat();
        System.out.println("Available Membership Plans: " + Membership.getMembershipTypes());
        System.out.print("Choose your membership plan: ");
        String membershipType = scanner.next();
        while(!membershipType.equalsIgnoreCase("general")&&!membershipType.equalsIgnoreCase("master")&&!membershipType.equalsIgnoreCase("expert")&&!membershipType.equalsIgnoreCase("weightloss")&&!membershipType.equalsIgnoreCase("weightgain"))
        {
            System.out.print("Invalid type. Please enter proper choice: ");
            membershipType = scanner.next();
        }  
        System.out.print("Choose payment method (online/cash): ");
        String paymentMethod = scanner.next();
        while(!paymentMethod.equalsIgnoreCase("online")&&!paymentMethod.equalsIgnoreCase("cash"))
        {
            System.out.print("Invalid type. Please enter proper choice: ");
            paymentMethod= scanner.next();
        }
        double membershipFee = Membership.calculateFee(membershipType, paymentMethod);

        Member member = new Member(m_firstName,m_lastName,m_age,m_gender,m_phoneNumber,m_email,m_address,m_height,m_weight, membershipType, membershipFee);
        members.add(member); // Store member in the list
        member.m_saveToDatabase(con); // Persist member to the database

        System.out.println("Member registered successfully!");
        System.out.println("Your membership fee is: " + membershipFee);
    }

    public void updateInMember(Connection con) throws SQLException {
        System.out.print("Enter the phone number of the member you want to update: ");
        String phoneNumberToUpdate = scanner.next();
    
        // First, check if the member exists in the database
        String query = "SELECT * FROM member WHERE phone_number = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, phoneNumberToUpdate);
        ResultSet rs = pstmt.executeQuery();
    
        if (rs.next()) {
            // Member exists, retrieve current details
            String currentFirstName = rs.getString("first_name");
            String currentLastName = rs.getString("last_name");
            int currentAge = rs.getInt("age");
            String currentGender = rs.getString("gender");
            String currentEmail = rs.getString("email");
            String currentAddress = rs.getString("address");
            float currentHeight = rs.getFloat("height");
            float currentWeight = rs.getFloat("weight");
    
            // Ask for new details
            System.out.print("Enter new first name [" + currentFirstName + "]: ");
            String newFirstName = scanner.next();
            System.out.print("Enter new last name [" + currentLastName + "]: ");
            String newLastName = scanner.next();
            System.out.print("Enter new age [" + currentAge + "]: ");
            int newAge = scanner.nextInt();
            System.out.print("Enter new gender [" + currentGender + "]: ");
            String newGender = validateGender();
            System.out.print("Enter new email [" + currentEmail + "]: ");
            String newEmail = scanner.next();
            System.out.print("Enter new address [" + currentAddress + "]: ");
            String newAddress = scanner.next();
            System.out.print("Enter new height [" + currentHeight + "]: ");
            float newHeight = scanner.nextFloat();
            System.out.print("Enter new weight [" + currentWeight + "]: ");
            float newWeight = scanner.nextFloat();
    
            // Update the database with the new details
            String updateQuery = "UPDATE member SET first_name = ?, last_name = ?, age = ?, gender = ?, email = ?, address = ?, height = ?, weight = ? WHERE phone_number = ?";
            PreparedStatement updatePstmt = con.prepareStatement(updateQuery);
            updatePstmt.setString(1, newFirstName);
            updatePstmt.setString(2, newLastName);
            updatePstmt.setInt(3, newAge);
            updatePstmt.setString(4, newGender);
            updatePstmt.setString(5, newEmail);
            updatePstmt.setString(6, newAddress);
            updatePstmt.setFloat(7, newHeight);
            updatePstmt.setFloat(8, newWeight);
            updatePstmt.setString(9, phoneNumberToUpdate);
    
            int rowsUpdated = updatePstmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                System.out.println("Member details updated successfully.");
            } else {
                System.out.println("Failed to update the member details.");
            }
        } else {
            System.out.println("No member found with phone number: " + phoneNumberToUpdate);
        }
    }

    public void updateInTrainer(Connection con) throws SQLException {
        System.out.print("Enter the phone number of the trainer you want to update: ");
        String phoneNumberToUpdate = scanner.next();
    
        // First, check if the member exists in the database
        String query = "SELECT * FROM trainer WHERE phone_number = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, phoneNumberToUpdate);
        ResultSet rs = pstmt.executeQuery();
    
        if (rs.next()) {
            // Member exists, retrieve current details
            String currentFirstName = rs.getString("first_name");
            String currentLastName = rs.getString("last_name");
            int currentAge = rs.getInt("age");
            String currentGender = rs.getString("gender");
            String currentEmail = rs.getString("email");
            String currentAddress = rs.getString("address");
            int currentexp = rs.getInt("experience");
            String currentspecial = rs.getString("speciality");
    
            // Ask for new details
            System.out.print("Enter new first name [" + currentFirstName + "]: ");
            String newFirstName = scanner.next();
            System.out.print("Enter new last name [" + currentLastName + "]: ");
            String newLastName = scanner.next();
            System.out.print("Enter new age [" + currentAge + "]: ");
            int newAge = scanner.nextInt();
            System.out.print("Enter new gender [" + currentGender + "]: ");
            String newGender = validateGender();
            System.out.print("Enter new email [" + currentEmail + "]: ");
            String newEmail = scanner.next();
            System.out.print("Enter new address [" + currentAddress + "]: ");
            String newAddress = scanner.next();
            System.out.print("Enter new experience [" + currentexp + "]: ");
            int newExp = scanner.nextInt();
            System.out.print("Enter new speciality [" + currentspecial + "]: ");
            String newSpecial = scanner.next();
    
            // Update the database with the new details
            String updateQuery = "UPDATE trainer SET first_name = ?, last_name = ?, age = ?, gender = ?, email = ?, address = ?, experience = ?, speciality = ? WHERE phone_number = ?";
            PreparedStatement updatePstmt = con.prepareStatement(updateQuery);
            updatePstmt.setString(1, newFirstName);
            updatePstmt.setString(2, newLastName);
            updatePstmt.setInt(3, newAge);
            updatePstmt.setString(4, newGender);
            updatePstmt.setString(5, newEmail);
            updatePstmt.setString(6, newAddress);
            updatePstmt.setInt(7, newExp);
            updatePstmt.setString(8, newSpecial);
            updatePstmt.setString(9, phoneNumberToUpdate);
    
            int rowsUpdated = updatePstmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                System.out.println("Member details updated successfully.");
            } else {
                System.out.println("Failed to update the member details.");
            }
        } else {
            System.out.println("No member found with phone number: " + phoneNumberToUpdate);
        }
    }

    public void deleteFromDatabase(Connection con) throws SQLException {
        System.out.print("Choose as a (member/trainer): ");
        String  signAs = scanner.next();
        while(!signAs.equalsIgnoreCase("member")&&!signAs.equalsIgnoreCase("trainer"))
        {
            System.out.print("Invalid type. Please enter proper choice: ");
            signAs= scanner.next();
        }
        if(signAs.equalsIgnoreCase("member")){
            System.out.println("Enter phone number you want to delete = ");
            String phoneNumberToDelete=scanner.next();
            String query = "SELECT * FROM member WHERE phone_number = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, phoneNumberToDelete);
            ResultSet rs = pstmt.executeQuery();
        
            if (rs.next()) {
                // Trainer exists, proceed with deletion
                String deleteQuery = "DELETE FROM member WHERE phone_number = ?";
                PreparedStatement deletePstmt = con.prepareStatement(deleteQuery);
                deletePstmt.setString(1, phoneNumberToDelete);
        
                int rowsDeleted = deletePstmt.executeUpdate();
        
                if (rowsDeleted > 0) {
                    System.out.println("Trainer deleted successfully.");
                } else {
                    System.out.println("Failed to delete the trainer.");
                }
            } else {
                System.out.println("No member found with phone number: " + phoneNumberToDelete);
            }
        }
        else{
            System.out.print("Enter the phone number of the trainer you want to delete: ");
            String phoneNumberToDelete = scanner.next();
        
            // First, check if the trainer exists in the database
            String query = "SELECT * FROM trainer WHERE phone_number = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, phoneNumberToDelete);
            ResultSet rs = pstmt.executeQuery();
        
            if (rs.next()) {
                // Trainer exists, proceed with deletion
                String deleteQuery = "DELETE FROM trainer WHERE phone_number = ?";
                PreparedStatement deletePstmt = con.prepareStatement(deleteQuery);
                deletePstmt.setString(1, phoneNumberToDelete);
        
                int rowsDeleted = deletePstmt.executeUpdate();
        
                if (rowsDeleted > 0) {
                    System.out.println("Trainer deleted successfully.");
                } else {
                    System.out.println("Failed to delete the trainer.");
                }
            } else {
                System.out.println("No trainer found with phone number: " + phoneNumberToDelete);
            }
        }
    }

   
    public void viewAllMembers() throws SQLException {
        String query = "SELECT * FROM member";
        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        System.out.println("---------------------------------------------------");
        System.out.println("                   Member List                     ");
        System.out.println("---------------------------------------------------");
        
        while (rs.next()) {
            System.out.println("First Name: " + rs.getString("first_name"));
            System.out.println("Last Name: " + rs.getString("last_name"));
            System.out.println("Age: " + rs.getInt("age"));
            System.out.println("Gender: " + rs.getString("gender"));
            System.out.println("Phone Number: " + rs.getString("phone_number"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Address: " + rs.getString("address"));
            System.out.println("Height: " + rs.getFloat("height"));
            System.out.println("Weight: " + rs.getFloat("weight"));
            System.out.println("Membership Type: " + rs.getString("membership_type"));
            System.out.println("Membership Fee: " + rs.getDouble("membership_fee"));
            System.out.println("---------------------------------------------------");
        }
    }

    public void viewAllTrainers() throws SQLException {
        String query = "SELECT * FROM trainer";
        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        System.out.println("---------------------------------------------------");
        System.out.println("                   Trainer List                    ");
        System.out.println("---------------------------------------------------");

        while (rs.next()) {
            System.out.println("First Name: " + rs.getString("first_name"));
            System.out.println("Last Name: " + rs.getString("last_name"));
            System.out.println("Age: " + rs.getInt("age"));
            System.out.println("Gender: " + rs.getString("gender"));
            System.out.println("Phone Number: " + rs.getString("phone_number"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Address: " + rs.getString("address"));
            System.out.println("Experience: " + rs.getInt("experience"));
            System.out.println("Speciality: " + rs.getString("speciality"));
            System.out.println("---------------------------------------------------");
        }
    }
    public String validatePhoneNumber() {
        String phoneNumber;
        while (true) {
            phoneNumber = scanner.next();
            if (phoneNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.print("Invalid phone number. Please enter a 10-digit number: ");
            }
        }
        return phoneNumber;
    }

    public String validateGender() {
        String gender;
        gender= scanner.next();
            while(!gender.equalsIgnoreCase("male")&&!gender.equalsIgnoreCase("female"))
            {
                System.out.print("Invalid type. Please enter proper choice: ");
                gender= scanner.next();
            }
        return gender;
    }
    

    public void login() throws SQLException {
        String pw = "1234";
        int count = 0;
        Boolean b = true;
      for(count = 1;count<=3;count++)
      {
        System.out.println("Enter your Password");
        String input =  scanner.next();
        if(input.equals(pw))
        {
            b = true;
          //  Member m;
            System.out.println("Login successfull");
            int ch;
            do {
                System.out.println("Enter your choice: ");
                System.out.println("1.Update  2.Delete  3.View  4.Exit ");
                ch = scanner.nextInt();
                switch (ch) {
                    case 1:
                    System.out.print("Choose as a (member/trainer): ");
                    String  signAs = scanner.next();
                    while(!signAs.equalsIgnoreCase("member")&&!signAs.equalsIgnoreCase("trainer"))
                    {
                        System.out.print("Invalid type. Please enter proper choice: ");
                        signAs= scanner.next();
                    }
                    if(signAs.equalsIgnoreCase("member")){
                        updateInMember(con);
                    }
                    else{
                        updateInTrainer(con);
                    }
                    break;
                    case 2:
                        deleteFromDatabase(con);
                    break;
                    case 3:
                    System.out.print("Choose as a (member/trainer): ");
                    String  signAs1 = scanner.next();
                    while(!signAs1.equalsIgnoreCase("member")&&!signAs1.equalsIgnoreCase("trainer"))
                    {
                        System.out.print("Invalid type. Please enter proper choice: ");
                        signAs= scanner.next();
                    }
                    if(signAs1.equalsIgnoreCase("member")){
                        viewAllMembers();
                    }
                    else{
                        viewAllTrainers();
                    }
                    break;
                    case 4 :
                    System.out.println("Thank You for visiting ABC gym");
                    default:
                        break;
                }
            } while (ch!=3);
            break;

        }
        else
        {
            System.out.println("Invalid password");
        }
      }
      return;
}
}
public class GymManagementSystem {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in); Connection con = DatabaseConnection.getConnection()) {
            UserInterface ui = new UserInterface(scanner, con);
            ui.showMenu();

            int choice;
            do {
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                    System.out.print("Choose as a (member/trainer): ");
                    String  signAs = scanner.next();
                    while(!signAs.equalsIgnoreCase("member")&&!signAs.equalsIgnoreCase("trainer"))
                    {
                        System.out.print("Invalid type. Please enter proper choice: ");
                        signAs= scanner.next();
                    }
                    if(signAs.equalsIgnoreCase("member")){
                        ui.registerMember();
                    }
                    else{
                        ui.registerTrainer();
                    }
                        break;
                    case 2:
                        ui.login();
                        break;
                    case 3:
                        System.out.println("Thank you for visiting ABC Gym!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}