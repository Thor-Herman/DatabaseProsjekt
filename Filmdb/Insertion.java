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

    public int insertVideoIntoDB(String title, int companyID, String videoType, int length, int releaseYear) {
        // Videotype needs to be one of the three
        String insertQuery = "INSERT INTO Video (Tittel, Beskrivelse, Lansdato, SelskapID, Videotype) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, companyID);
            preparedStatement.setString(3, videoType);
            preparedStatement.setInt(4, length);
            preparedStatement.setInt(5, releaseYear);
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
            preparedStatement.close();
        }
        catch (SQLException e) {
            System.out.println("Db error when inserting video into db");
        }
    }

    public static void main(String[] args) {
        java.sql.Date date = new java.sql.Date(new java.util.Date(1999, 10, 30).getTime());
    }

}
