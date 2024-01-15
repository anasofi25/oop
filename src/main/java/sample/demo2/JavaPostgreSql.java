
package sample.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JavaPostgreSql {
    public static int writeToDatabase(String email, String password, String name, String gender, Date dateofbirth ){
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        String checkQuery = "SELECT user_id FROM users WHERE email = ?";
        String query = "INSERT INTO users(email, password, name, gender, dateofbirth) VALUES(?, ?, ?, ?, ?)";
        int userID=-1;

        try (Connection con = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement check=con.prepareStatement(checkQuery);
             PreparedStatement pst = con.prepareStatement(query)) {

            check.setString(1, email);
            ResultSet resultSet=check.executeQuery();
            if(resultSet.next()){
                userID=resultSet.getInt("user_id");
                System.out.println("User exists. User id: "+ userID);
            }else{
                pst.setString(1, email);
                pst.setString(2, password);
                pst.setString(3,name);
                pst.setString(4,gender);
                pst.setDate(5, (java.sql.Date) dateofbirth);
                pst.executeUpdate();

                ResultSet generatedKeys = pst.getGeneratedKeys();
                if(generatedKeys.next()) {
                    userID = generatedKeys.getInt(1);
                    System.out.println("Successfully created.User id: "+ userID);
                }

            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return userID;
    }

    public static int retrieveUserID(String email, String password) {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String password_1 = "password";

        String query = "SELECT user_id FROM users WHERE email = ? AND password = ?";
        int userID = -1;

        try (Connection con = DriverManager.getConnection(url, user, password_1);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getInt("user_id");
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return userID;
    }

    public static ObservableList<Destinations> getDestinationsFromDatabase()  {
        ObservableList<Destinations> data = FXCollections.observableArrayList();
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT destination_id, country, city"+ " FROM destinations";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Integer destinationId = resultSet.getInt("destination_id");
                    String country = resultSet.getString("country");
                    String city = resultSet.getString("city");


                    System.out.println("Destination: " + country + " City:" + city);

                    // Create a Destination object for each row
                    Destinations destination = new Destinations(destinationId, country, city);
                    data.add(destination);
                }
            }catch(SQLException e) {
            e.printStackTrace();

        }

        return data;
    }


    public static ObservableList<Destinations> getFilteredDestinationsFromDatabase(String searchQuery) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        // Use a prepared statement with a LIKE clause to filter data
        String query = "SELECT destination_id, country, city FROM destinations " +
                "WHERE country ILIKE ? OR city ILIKE ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters for the LIKE clause
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<Destinations> filteredData = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    // Extract data and create Destinations objects
                    int destinationId = resultSet.getInt("destination_id");
                    String country = resultSet.getString("country");
                    String city = resultSet.getString("city");


                    Destinations destination = new Destinations(destinationId, country, city);
                    filteredData.add(destination);
                }

                return filteredData;
            }
        }
    }

    public static ObservableList<Trip> getTripsFromDatabase() throws SQLException {
        ObservableList<Trip> data = FXCollections.observableArrayList();
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT available_dates_id, starting_date, end_date, nr_nights FROM available_dates";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Integer tripId = resultSet.getInt("available_dates_id");
                    Date startingDate = resultSet.getDate("starting_date");
                    Date endDate = resultSet.getDate("end_date");
                  Integer nrNights=resultSet.getInt("nr_nights");

                    System.out.println("Trip ID: " + tripId + " Starting Date: " + startingDate + " End Date: " + endDate);

                    // Create a Trip object for each row
                    Trip trip = new Trip(tripId, startingDate, endDate, nrNights);
                    data.add(trip);
                }
            }
        }
        return data;
    }
   /* public static ObservableList<Flights> getFlightsFromDatabase(int bookingId) throws SQLException {
        ObservableList<Flights> data = FXCollections.observableArrayList();
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT f.flight_id, f.airline, f.departureairport, f.arrivalairport, f.departuredatetime, f.arrivaldatetime, f.price, f.availableseats FROM flights f " +
                    "JOIN destinations_available_dates dad ON f.flight_id = dad.flight_id " +
                    "WHERE dad.destinations_available_dates_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                preparedStatement.setInt(1, bookingId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        Integer flightId = resultSet.getInt("flight_id");
                        String airline = resultSet.getString("airline");
                        String departureAirport = resultSet.getString("departureairport");
                        String arrivalAirport = resultSet.getString("arrivalairport");
                        Timestamp departureTime = resultSet.getTimestamp("departuredatetime");
                        Timestamp arrivalTime = resultSet.getTimestamp("arrivaldatetime");
                        Integer price = resultSet.getInt("price");
                        String seat = resultSet.getString("availableseats");

                        // Create a Flight object for each row
                        Flights flight = new Flights(flightId, airline, departureAirport, arrivalAirport, departureTime.toLocalDateTime(), arrivalTime.toLocalDateTime(), price, seat);
                        data.add(flight);
                    }
                }
            }

            return data;
        }
    }*/

    public static ObservableList<Trip> getTripsForDestinationFromDatabase(int destinationId) throws SQLException {
        ObservableList<Trip> data = FXCollections.observableArrayList();
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT a.available_dates_id, a.starting_date, a.end_date, a.nr_nights " +
                    "FROM available_dates a " +
                    "JOIN destinations_available_dates dad ON a.available_dates_id = dad.available_dates_id " +
                    "WHERE dad.destination_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, destinationId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer tripId = resultSet.getInt("available_dates_id");
                        Date startingDate = resultSet.getDate("starting_date");
                        Date endDate = resultSet.getDate("end_date");
                        Integer nrNights=resultSet.getInt("nr_nights");


                        System.out.println("Trip ID: " + tripId + " Starting Date: " + startingDate + " End Date: " + endDate);

                        // Create a Trip object for each row
                        Trip trip = new Trip(tripId, startingDate, endDate, nrNights);
                        data.add(trip);
                    }
                }
            }
        }
        return data;
    }
    public static ObservableList<Hotel> getHotelsFromDatabase() throws SQLException {
        ObservableList<Hotel> data = FXCollections.observableArrayList();
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT hotelid, name, location, description, pricepernight, availablerooms FROM hotels";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Integer hotelId = resultSet.getInt("hotelid");
                   String hotelName=resultSet.getString("name");
                   String hotelLocation=resultSet.getString("location");
                   String hotelDescription=resultSet.getString("description");
                   Integer hotelPrice=resultSet.getInt("pricepernight");
                   Integer hotelAvb=resultSet.getInt("availablerooms");


                    // Create a Trip object for each row
                    Hotel hotel = new Hotel(hotelId,hotelName,hotelLocation,hotelDescription,hotelPrice, hotelAvb);
                    data.add(hotel);
                }
            }
        }
        return data;
    }
    public static ObservableList<Hotel> getHotelsForDateFromDatabase( int availableDatesId) throws SQLException {
        ObservableList<Hotel> data = FXCollections.observableArrayList();
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT h.hotelid, h.name, h.location, h.description, h.pricepernight, h.availablerooms " +
                    "FROM hotels h " +
                    "JOIN hotel_available_dates had ON h.hotelid = had.hotelid " +
                    "JOIN destinations d ON h.destination_id = d.destination_id " +
                    "WHERE had.available_dates_id = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, availableDatesId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("hotelid");
                        String name = resultSet.getString("name");
                        String location = resultSet.getString("location");
                        String description = resultSet.getString("description");
                        int pricePerNight = resultSet.getInt("pricepernight");
                        int availableRooms = resultSet.getInt("availablerooms");

                        // Create a Hotel object for each row
                        Hotel hotel = new Hotel(id, name, location, description, pricePerNight, availableRooms);
                        data.add(hotel);
                    }
                }
            }
        }
        return data;
    }


    public static ObservableList<String> getCitiesForCountry(String country) throws SQLException {
        ObservableList<String> cities = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT DISTINCT city FROM destinations WHERE country = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, country);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String city = resultSet.getString("city");
                        cities.add(city);
                    }
                }
            }
        }

        return cities;
    }
    public static ObservableList<String> getCountriesFromDatabase() throws SQLException {
        ObservableList<String> countries = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT DISTINCT country FROM destinations";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    String country = resultSet.getString("country");
                    countries.add(country);
                }
            }
        }

        return countries;
    }
    public static ObservableList<Destinations> getDestinationsByCountry(String country) throws SQLException {
        ObservableList<Destinations> data = FXCollections.observableArrayList();
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT destination_id, country, city FROM destinations WHERE country = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, country);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer destinationId = resultSet.getInt("destination_id");
                        String countryResult = resultSet.getString("country");
                        String city = resultSet.getString("city");

                        Destinations destination = new Destinations(destinationId, countryResult, city);
                        data.add(destination);
                    }
                }
            }
        }

        return data;
    }

    public static ObservableList<Users> getUsers(int userId) {
        ObservableList<Users> data = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String password1 = "password";

        try (Connection con = DriverManager.getConnection(url, user, password1)) {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int retrievedUserId = rs.getInt("user_id");
                    String username = rs.getString("email");
                    String password = rs.getString("password");
                    String fullName = rs.getString("name");
                    String gender = rs.getString("gender");
                    String birthDate = rs.getString("dateofbirth");

                    data.add(new Users(retrievedUserId, username, password, fullName, gender, birthDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static boolean updatePassword(int userId, String newPassword) {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String password1 = "password";

        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password1);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static int createBookingForUser(int userId) {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        String insertBookingQuery = "INSERT INTO bookings (user_id) VALUES (?) RETURNING booking_id";
        int bookingId = -1;

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement insertBookingStatement = connection.prepareStatement(insertBookingQuery)) {

            // Insert a new booking for the user and retrieve the generated booking_id
            insertBookingStatement.setInt(1, userId);
            try (ResultSet generatedKeys = insertBookingStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    bookingId = generatedKeys.getInt(1);
                    System.out.println("Successfully created booking. Generated Booking id: " + bookingId);
                }
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return bookingId;
    }


    public static void storeDatesId(int bookingId, int availableDatesId) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            // Update the available_dates_id for the specified booking_id
            String updateQuery = "UPDATE bookings SET available_dates_id = ? WHERE booking_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, availableDatesId);
                updateStatement.setInt(2, bookingId);
                updateStatement.executeUpdate();
            }
        }
    }
    public static void storeHotelId(int bookingId, int availableHotelId) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            // Update the available_dates_id for the specified booking_id
            String updateQuery = "UPDATE bookings SET hotelid = ? WHERE booking_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, availableHotelId);
                updateStatement.setInt(2, bookingId);
                updateStatement.executeUpdate();
            }
        }
    }




    public static void storeDestinationId(int bookingId, int destinationId) throws SQLException{
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            // Insert a new booking record for the user
            String insertQuery = "UPDATE bookings SET destination_id=? where booking_id=?";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, destinationId);
                insertStatement.setInt(2,bookingId);
                insertStatement.executeUpdate();
            }
        }

    }

    public static int getAvailableDatesId(int bookingId) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String query = "SELECT available_dates_id FROM bookings WHERE booking_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, bookingId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("available_dates_id");
                    }
                }
            }
        }

        // Return a default value or throw an exception based on your use case
        throw new SQLException("Unable to fetch availableDatesId for bookingId: " + bookingId);
    }


        public static ObservableList<Booking> getBookingDetails(int userId) throws SQLException {
            ObservableList<Booking> bookingDetailsList = FXCollections.observableArrayList();
            String url = "jdbc:postgresql://localhost:5432/proiect";
            String user = "postgres";
            String dbPassword = "password";

            try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
                String query = "SELECT b.booking_id, d.country, d.city, h.name,  h.pricepernight, ad.nr_nights, ad.starting_date, ad.end_date  "+
                        " FROM bookings b "+
                        " JOIN destinations d ON b.destination_id = d.destination_id "+
                        " JOIN hotels h ON b.hotelid = h.hotelid "+
                        " JOIN available_dates ad ON b.available_dates_id = ad.available_dates_id "+
                        " WHERE b.user_id = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, userId);
                     ResultSet resultSet = preparedStatement.executeQuery(); {

                    while (resultSet.next()) {
                        int booking_id=resultSet.getInt("booking_id");
                        String country = resultSet.getString("country");
                        String city = resultSet.getString("city");
                        String hotelName = resultSet.getString("name");
                        int hotelPrice = resultSet.getInt("pricepernight");
                        int nrNights=resultSet.getInt("nr_nights");
                        Date startDate=resultSet.getDate("starting_date");
                        Date endDate=resultSet.getDate("end_date");

                        // Create a BookingDetails object for each row
                        Booking bookingDetails = new Booking(booking_id, country, city, hotelName, hotelPrice, nrNights, startDate, endDate);
                        bookingDetailsList.add(bookingDetails);
                    }
                }
            }
            return bookingDetailsList;
        }
    public static void saveReviewToDatabase(int bookingId, int userId, String reviewText) {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String password = "password"; // Replace with your actual password

        String insertQuery = "INSERT INTO reviews (booking_id, user_id, review_text) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {

            insertStatement.setInt(1, bookingId);
            insertStatement.setInt(2, userId);
            insertStatement.setString(3, reviewText);

            // Execute the insert query
            insertStatement.executeUpdate();

            System.out.println("Review saved to the database!"); // Optional: Print confirmation message
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Booking.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    public static ObservableList<Review> getReviewsByBookingId(int bookingId) {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String password = "password";

        String query = "SELECT * FROM reviews WHERE booking_id = ?";
        ObservableList<Review> reviewsData = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int reviewId = resultSet.getInt("review_id");
                int userId = resultSet.getInt("user_id");
                String reviewText = resultSet.getString("review_text");

                // Assuming you have a Review class with appropriate constructor
                Review review = new Review(reviewId, userId, reviewText);
                reviewsData.add(review);
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return reviewsData;
    }

    public static void cancelBooking(int userId, int bookingId) {
        String url = "jdbc:postgresql://localhost:5432/proiect";
        String user = "postgres";
        String password = "password";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM bookings WHERE user_id = ? AND booking_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
            System.out.println("Booking with ID " + bookingId + " canceled and removed for user ID " + userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


