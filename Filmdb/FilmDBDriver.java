import java.sql.*;
import java.util.ArrayList;

public class FilmDBDriver {

    // Driver
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/testschema";

    // Credentials
    private static final String USER = "root";
    private static final String PASSWORD = "6wC9L8#ujrW9LeQTwJ*X"; // Må endres til personlig passord

    // Connection saved
    protected Connection connection;

    public void connect() {
        try {
            // Register driver
            Class.forName(JDBC_DRIVER);

            // Start connection
            System.out.println("Establishing connection..");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }
    }
    protected ArrayList<String> SQLToStringList(String columnName, String query) {
        ArrayList<String> resultingList = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                resultingList.add(resultSet.getString(columnName));
            }
        }
        catch (Exception e ) {
            System.out.println("db error when retrieving " + columnName);
        }
        return resultingList;
    }
}