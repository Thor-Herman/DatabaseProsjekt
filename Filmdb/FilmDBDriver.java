import java.sql.*;

public class FilmDBDriver {

    // Driver
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/testschema";

    // Credentials
    private static final String USER = "root";
    private static final String PASSWORD = "6wC9L8#ujrW9LeQTwJ*X";

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

    public static void main(String[] args) {
        Statement statement = null;
        FilmDBDriver driver = new FilmDBDriver();
        driver.connect();
        try {
            statement = driver.connection.createStatement();
            String sql = "SELECT * FROM bruker";
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                int brukerID = result.getInt("BrukerID");
                String brukerNavn = result.getString("Navn");

                System.out.println(brukerID + brukerNavn);
            }
        }
        catch (Exception e) {

        }
    }
}