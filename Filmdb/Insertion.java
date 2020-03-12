import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Insertion extends FilmDBDriver {

    private String[] acceptableRoles = {"skuespiller", "regissør", "forfatter"};

    public Insertion() {
        super.connect();
    }

    private int executeLatestIDQuery(String idQuery, String columnName) {
        int id = -1;
        try {
            ResultSet set = connection.createStatement().executeQuery(idQuery);
            if (set.next())
                id = set.getInt(columnName);
        }
        catch (SQLException e){
            System.out.println("DB error when retrieving latest " + columnName);
        }
        return id;
    }

    private int getLatestVideoID() {
        String idQuery = "SELECT VideoID FROM Video ORDER BY VideoID DESC LIMIT 1";
        return executeLatestIDQuery(idQuery, "VideoID");
    }

    private int getLatestPersonID() {
        String idQuery = "SELECT PersonNr FROM Person ORDER BY PersonNr DESC LIMIT 1";
        return executeLatestIDQuery(idQuery, "PersonNr");
    }

    public int insertVideoIntoDB(String title, String description, String date, int companyID, String videoType) {
        // Videotype needs to be one of the three
        String insertQuery = "INSERT INTO Video (Tittel, Beskrivelse, Lansdato, SelskapID, Videotype) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, companyID);
            preparedStatement.setString(5, videoType);
            preparedStatement.execute();
            preparedStatement.close();
        }
        catch (SQLException e) {
            System.out.println("Db error when inserting video into db");
        }
        return getLatestVideoID();
        // Parent method should handle cases where VideoID is -1
    }

    public void insertFilmIntoDB(int length, int releaseYear, int videoID) {
        String insertQuery = "INSERT INTO Film (Lengde, UtgÅr, VideoID) " +
                             "VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, length);
            preparedStatement.setInt(2, releaseYear);
            preparedStatement.setInt(3, videoID);
            preparedStatement.execute();
        }
        catch (SQLException e) {
            System.out.println("Db error when inserting video into db");
        }
    }

    public void insertPersonIntoDB(String name) {
        String nameQuery = "INSERT INTO Person (Navn) " +
                "VALUES (?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(nameQuery);
            preparedStatement.setString(1,name);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Db error when inserting person into db");
        }
    }

    public void addRoleToPerson(String role, int personNr) {
        if (Arrays.stream(acceptableRoles).noneMatch(
                acceptableRole -> acceptableRole.toLowerCase().equals(role.toLowerCase()))) {
            System.out.println(role + " is not an acceptable role");
        }
        String roleQuery = "";
        switch (role.toLowerCase()) {
            case "forfatter":
                roleQuery = "INSERT INTO forfatter VALUES(?)";
                break;
            case "regissør":
                roleQuery = "INSERT INTO regissør VALUES(?)";
                break;
            case "skuespiller":
                roleQuery = "INSERT INTO skuespiller VALUES(?)";
                break;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(roleQuery);
            preparedStatement.setInt(1, personNr);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Db error when adding role to a person. Person may not exist or they" +
                    "may already have that role");;
        }
    }

    public static void main(String[] args) {
        Insertion insrt = new Insertion();
        System.out.println(insrt.getLatestPersonID());
        //insrt.insertFilmIntoDB(120, 2004, 2);
        //insrt.insertVideoIntoDB("The Room", "I did naht hit her", "2004-03-01", 1, "Kino");
        //insrt.insertPersonIntoDB("Hallvard Trætteberg");
        insrt.addRoleToPerson("Forfatter", 2);
    }

}
