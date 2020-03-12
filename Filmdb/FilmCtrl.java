import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilmCtrl extends FilmDBDriver {

    public FilmCtrl() {
        super.connect();
    }

    private ArrayList<String> getCompaniesMostFilmsPerGenre() {
        ArrayList<String> mostFilmsPerGenreList = new ArrayList<>();
        String templateSQL =
                "SELECT Kategori.KategoriNavn, SelskapID, count(VideoID)" +
                "FROM kategorisert NATURAL JOIN kategori NATURAL JOIN Video NATURAL JOIN FILM NATURAL JOIN SELSKAP " +
                "GROUP BY KategoriNavn " +
                "ORDER BY count(VideoID) DESC " +
                "LIMIT 1";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(templateSQL);
            while (resultSet.next()) {
                String resultRow = resultSet.getString("KategoriNavn") + ","
                        + resultSet.getInt("SelskapID") + ","
                        + resultSet.getInt("count(VideoID)");
                mostFilmsPerGenreList.add(resultRow);
            }
            return mostFilmsPerGenreList;
        }
        catch (SQLException e) {
            System.out.println("Db error when retrieving film genres per company");
            return null;
        }
    }

    public void printCompaniesMostFilmsPerGenre() {
        ArrayList<String> genreList = getCompaniesMostFilmsPerGenre();
        if (genreList == null) {
            System.out.println("There are no complete entries in the database");
            return;
        }
        for (String row : genreList) {
            String[] rowArr = row.split(",");
            System.out.print("Genre: " + rowArr[0]);
            System.out.print(" | CompanyID: " + rowArr[1]);
            System.out.println(" | Amount of films in genre: " + rowArr[2]);
        }
    }

    public static void main(String[] args) {
        FilmCtrl filmCtrl = new FilmCtrl();
        filmCtrl.printCompaniesMostFilmsPerGenre();
    }

}
