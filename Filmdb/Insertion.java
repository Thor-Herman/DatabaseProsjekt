import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Insertion extends FilmDBDriver {

    public Insertion() {
        super.connect();
    }

    private int getLatestVideoID() {
        int videoID = -1;
        String idQuery = "SELECT VideoID FROM Video ORDER BY VideoID DESC LIMIT 1";
        try {
            ResultSet set = connection.createStatement().executeQuery(idQuery);
            videoID = set.getInt("VideoID");
        }
        catch (SQLException e){
            System.out.println("DB error when retrieving latest videoID");
        }
        return videoID;
    }

    public int insertVideoIntoDB(String title, String description, Date date, int companyID, String videoType) {
        // Videotype needs to be one of the three
        String insertQuery = "INSERT INTO Video (Tittel, Beskrivelse, Lansdato, SelskapID, Videotype) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setDate(3, date);
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

    public static void main(String[] args) {
        java.sql.Date date = new java.sql.Date(new java.util.Date(1999, 10, 30).getTime());
        Insertion insrt = new Insertion();
        insrt.insertVideoIntoDB("The Room", "I did naht hit her", date, 1, "Mafia-thriller");
    }

}
