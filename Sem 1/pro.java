
import java.sql.*;
import java.util.*;


class DatabaseUtil {
    static final String URL = "jdbc:mysql://localhost:3306/stadium";
    static final String USER = "root";
    static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

class Event {
    int id;
    String name;
    Timestamp dateTime;
    String type;

    public Event(int id, String name, Timestamp dateTime, String type) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", type='" + type + '\'' +
                '}';
    }
}

class Ticket {
    int id;
    int eventId;
    String seatNumber;
    int userId;
    String status;

    public Ticket(int id, int eventId, String seatNumber, int userId, String status) {
        this.id = id;
        this.eventId = eventId;
        this.seatNumber = seatNumber;
        this.userId = userId;
        this.status = status;
        
    }

    public int getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", seatNumber='" + seatNumber + '\'' +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }
}


class Facility {
     int id;
     String name;
     String description;

    public Facility(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

class EventManager {
    void addEvent(String name, Timestamp dateTime, String type) throws SQLException {
        String sql = "INSERT INTO Events (name, date, type) VALUES (?, ?, ?)";
        Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setTimestamp(2, dateTime); // Use Timestamp for a DATETIME column
            stmt.setString(3, type);
            stmt.executeUpdate();
       
    }

    void updateEvent(int id, String name, Timestamp dateTime, String type) throws SQLException {
        String sql = "UPDATE Events SET name = ?, date = ?, type = ? WHERE id = ?";
        Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setTimestamp(2, dateTime); // Use Timestamp for a DATETIME column
            stmt.setString(3, type);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        
    }

    void deleteEvent(int id) throws SQLException {
        String sql = "DELETE FROM Events WHERE id = ?";
      
            Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        
    }

    ArrayList<Event> getEvents() throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Events";
        
            Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Event event = new Event(rs.getInt("id"), rs.getString("name"),
                        rs.getTimestamp("date"), rs.getString("type"));
                events.add(event);
            }
        
        return events;
    }
}

class TicketManager {
    static int top;
    Ticket ticekt1[] =  new Ticket[10];
    public TicketManager() throws Exception{
        top=-1;
        Ticket ticket = null;
        Connection conn=DatabaseUtil.getConnection();
        Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tickets ORDER BY id desc");
            while (rs.next()) {
                int id = rs.getInt("id");
                int event_id = rs.getInt("event_id");
                String seat_number = rs.getString("seat_number");
                int user_id = rs.getInt("user_id");
                String status=rs.getString("status");
                ticket = new Ticket(id, event_id, seat_number, user_id,status);
                top++;
                ticekt1[top] = ticket; 
                
            }
    }

    public void push(Ticket ticket) throws Exception {
       if (top>=ticekt1.length-1) {

       } else {
            top++;
            ticekt1[top]=ticket;
            bookTicket();
            
       }
            
    }


    public void bookTicket() throws SQLException {
        String sql = "INSERT INTO Tickets (event_id, seat_number, user_id, status) VALUES (?, ?, ?, ?)";
        
            Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, ticekt1[top].eventId);
            stmt.setString(2, ticekt1[top].seatNumber);
            stmt.setInt(3, ticekt1[top].userId);
            stmt.setString(4, ticekt1[top].status);
            stmt.executeUpdate();
       
    }

    void cancelTicket(int ticketId)throws SQLException {
        String sql = "UPDATE Tickets SET status = 'cancelled' WHERE id = ?";
        Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ticketId);
            stmt.executeUpdate();
       
        
    }

    ArrayList<Ticket> getTicketsForEvent(int eventId) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM Tickets WHERE event_id = ?";
        
            Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, eventId);
            try {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Ticket ticket = new Ticket(rs.getInt("id"), rs.getInt("event_id"),
                            rs.getString("seat_number"), rs.getInt("user_id"), rs.getString("status"));
                    tickets.add(ticket);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        
        return tickets;
    }
}

class UserManager {
    void registerUser(String username, String password, String email, String membershipLevel) throws SQLException {
        String sql = "INSERT INTO Users (username, password, email, membership_level) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // In a real application, ensure password is hashed
            stmt.setString(3, email);
            stmt.setString(4, membershipLevel);
            stmt.executeUpdate();
      
    }

    boolean authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // Ensure password hashing in real apps
            
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            
    }

    public void updateUser(int id, String email, String membershipLevel) throws SQLException {
        String sql = "UPDATE Users SET email = ?, membership_level = ? WHERE id = ?";
        Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, membershipLevel);
            stmt.setInt(3, id);
            stmt.executeUpdate();
       
    }
}

class FacilityManager {
    void addFacility(String name, String description) throws SQLException {
        String sql = "INSERT INTO Facilities (name, description) VALUES (?, ?)";
       Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.executeUpdate();
        
    }

    void updateFacility(int id, String name, String description) throws SQLException {
        String sql = "UPDATE Facilities SET name = ?, description = ? WHERE id = ?";
        Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        
    }

    void deleteFacility(int id) throws SQLException {
        String sql = "DELETE FROM Facilities WHERE id = ?";
        Connection conn = DatabaseUtil.getConnection();

        
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
       
    }

    ArrayList<Facility> getFacilities() throws SQLException {
        ArrayList<Facility> facilities = new ArrayList<>();
        String sql = "SELECT * FROM Facilities";
        Connection conn = DatabaseUtil.getConnection();

        
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Facility facility = new Facility(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
                facilities.add(facility);
            }
       
        return facilities;
    }
}

class NotificationManager {
    void addNotification(String recipient, String subject, String body) throws SQLException {
        String sql = "INSERT INTO notifications (recipient, subject, body) VALUES (?, ?, ?)";
        Connection conn = DatabaseUtil.getConnection();
       
            
             PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, recipient);
            stmt.setString(2, subject);
            stmt.setString(3, body);
            stmt.executeUpdate();
            
            System.out.println("Notification added successfully for recipient: " + recipient);
       
    }
}

 class StadiumManagementSystem {
    public static void main(String[] args)throws Exception {
        
            // Initialize managers
            EventManager eventManager = new EventManager();
            TicketManager ticketManager = new TicketManager();
            UserManager userManager = new UserManager();
            FacilityManager facilityManager = new FacilityManager();
            NotificationManager notificationManager = new NotificationManager();

            // Add an event
            Timestamp eventDateTime = Timestamp.valueOf("2024-09-15 19:00:00");
            eventManager.addEvent("Football Match", eventDateTime, "match");

            // List all events
            ArrayList<Event> events = eventManager.getEvents();
            for (Event event : events) {
                System.out.println(event);
            }

            // Register a user
            userManager.registerUser("john_doe", "password123", "johndoe@example.com", "Gold");

            // Authenticate a user
            boolean isAuthenticated = userManager.authenticateUser("john_doe", "password123");
            if (isAuthenticated) {
                System.out.println("User authenticated successfully.");
            } else {
                System.out.println("Authentication failed.");
            }

            // Add a facility
            facilityManager.addFacility("Main Hall", "The main event hall with 5000 seats.");

            // List all facilities
            ArrayList<Facility> facilities = facilityManager.getFacilities();
            for (Facility facility : facilities) {
                System.out.println(facility);
            }

            // Book a ticket for the event
            int eventId = events.get(0).getId(); // Assume we book for the first event
            ticketManager.push(new Ticket(1, eventId, "A1", 1, "booked"));

            // Get tickets for an event
            ArrayList<Ticket> tickets = ticketManager.getTicketsForEvent(eventId);
            for (Ticket ticket : tickets) {
                System.out.println(ticket);
            }

            // Send notification
            notificationManager.addNotification("john.doe@example.com", "Event Reminder", "Reminder: Your event is scheduled for " + eventDateTime);
    }
}