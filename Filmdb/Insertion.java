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

    private void createIntQuery(String query, int[] columnValues) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (columnValues.length < 1)
                throw new IllegalArgumentException("Column amount must be at least 1");
            for (int i = 0; i < columnValues.length; i++) {
                preparedStatement.setInt(i+1, columnValues[i]);
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkExistenceInTable(String[] tables, String identifier, int id) {
        // Tables = tabeller som skal joines
        // Identifier = Navn til ID
        // id = faktisk id-verdi
        StringBuffer queryBuff = new StringBuffer();
        queryBuff.append("SELECT * FROM ");
        queryBuff.append(tables[0]);
        for (int i = 1; i < tables.length; i++) {
            queryBuff.append(" NATURAL JOIN ");
            queryBuff.append(tables[i]);
        }
        queryBuff.append(" WHERE ");
        queryBuff.append(identifier);
        queryBuff.append("=");
        queryBuff.append(id);
        String roleQuery = queryBuff.toString();
        try {
            ResultSet set = connection.createStatement().executeQuery(roleQuery);
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkPersonHasRole(String role, int personNr) {
        String[] roles = {role};
        return checkExistenceInTable(roles, "PersonNr", personNr);
    }

    private boolean checkSeriesExists(int serieID) {
        String[] series = {"serie"};
        return checkExistenceInTable(series, "videoid", serieID );
    }

    private boolean checkEpisodeExists(int epID) {
        String[] episode = {"episode"};
        return checkExistenceInTable(episode, "episodeid", epID);
    }

    private boolean checkUserExists(int userID) {
        String[] user = {"bruker"};
        return checkExistenceInTable(user, "brukerid", userID);
    }

    private int getLatestCompanyID() {
        String idQuery = "SELECT SelskapID FROM Selskap ORDER BY SelskapID DESC LIMIT 1";
        return executeLatestIDQuery(idQuery, "SelskapID");
    }

    private int getLatestVideoID() {
        String idQuery = "SELECT VideoID FROM Video ORDER BY VideoID DESC LIMIT 1";
        return executeLatestIDQuery(idQuery, "VideoID");
    }

    private int getLatestPersonID() {
        String idQuery = "SELECT PersonNr FROM Person ORDER BY PersonNr DESC LIMIT 1";
        return executeLatestIDQuery(idQuery, "PersonNr");
    }

    private int getLatestUserID() {
        String idQuery = "SELECT BrukerID FROM Bruker ORDER BY BrukerID DESC LIMIT 1";
        return executeLatestIDQuery(idQuery, "BrukerID");
    }

    public int insertCompanyIntoDb(String country, String address, String url) {
        String insertQuery = "INSERT INTO selskap (Land, Addresse, URL) " +
                             "VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, country);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, url);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return getLatestCompanyID();
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

    public void insertSeriesIntoDB(int videoID) {
        String query = "INSERT INTO Serie VALUES (?)";
        int[] videoArray = {videoID};
        createIntQuery(query, videoArray);
    }

    public void insertEpisodeIntoDB(int episodeNr, int relYear, int season, String title, String descr, int videoID) {
        if (checkSeriesExists(videoID)) {
            if (title.equals("")) {
                System.out.println("Episodes must have a title");
                return;
            }
            String episodeSQL = "INSERT INTO episode (EpisodeNr, UtgÅr, Sesong, Tittel, Beskrivelse, VideoID)" +
                                "VALUES (?,?,?,?,?,?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(episodeSQL);
                preparedStatement.setInt(1, episodeNr);
                preparedStatement.setInt(2, relYear);
                preparedStatement.setInt(3, season);
                preparedStatement.setString(4, title);
                preparedStatement.setString(5, descr);
                preparedStatement.setInt(6, videoID);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("There is no series corresponding to the videoID");
        }
    }

    public int insertUserIntoDB(String name) {
        String userQuery = "INSERT INTO bruker (Navn) VALUES (?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(userQuery);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Error while inserting user into db. May already exist");
            return -1;
        }
        return getLatestUserID();
    }

    public int insertPersonIntoDB(String name) {
        String nameQuery = "INSERT INTO Person (Navn) " +
                "VALUES (?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(nameQuery);
            preparedStatement.setString(1,name);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Db error when inserting person into db");
            return -1;
        }
        return getLatestPersonID();
    }

    private String determineRoleQuery(String role, boolean isVerb) {
        String roleQuery = "";
        switch (role.toLowerCase()) {
            case "forfatter":
                if (isVerb)
                    roleQuery = "INSERT INTO skrevetmanus VALUES(?,?)";
                else
                    roleQuery = "INSERT INTO forfatter VALUES(?)";
                break;
            case "regissør":
                if (isVerb)
                    roleQuery = "INSERT INTO regisserer VALUES(?,?)";
                else
                    roleQuery = "INSERT INTO regissør VALUES(?)";
                break;
            case "skuespiller":
                if (isVerb)
                    roleQuery = "INSERT INTO spilleri VALUES(?,?)";
                else
                    roleQuery = "INSERT INTO skuespiller VALUES(?)";
                break;
        }
        return roleQuery;
    }

    public void addRoleToPerson(String role, int personNr) {
        if (Arrays.stream(acceptableRoles).noneMatch(
                acceptableRole -> acceptableRole.toLowerCase().equals(role.toLowerCase()))) {
            System.out.println(role + " is not an acceptable role");
        }
        if (! checkPersonHasRole(role, personNr)) {
            String roleQuery = determineRoleQuery(role, false);
            int[] roles = {personNr};
            createIntQuery(roleQuery, roles);
        }
        else {
            System.out.println("Person already has that role");
        }
    }

    public void addRoleToVideo(String role, int personNr, int videoID) {
        if (Arrays.stream(acceptableRoles).noneMatch(
                acceptableRoleVerb -> acceptableRoleVerb.toLowerCase().equals(role.toLowerCase()))) {
            System.out.println(role + " is not an acceptable roleVerb");
        }
        if (! checkPersonHasRole(role, personNr)) {
            addRoleToPerson(role, personNr);
            System.out.println("Added role " + role + " to this person");
        }
        String roleQuery = determineRoleQuery(role, true);
        int[] foreignKeys = {personNr, videoID};
        createIntQuery(roleQuery, foreignKeys);
    }

    public void addRatingToEpisode(int userID, int episodeID, int rating) {
        if (rating < 11 && rating > 0) {
            if (checkUserExists(userID)) {
                if (checkEpisodeExists(episodeID)) {
                    String ratingQuery = "INSERT INTO VurdertEpisode VALUES (?,?,?)";
                    int[] ratingValues = {userID, episodeID, rating};
                    createIntQuery(ratingQuery, ratingValues);
                }
                else {
                    System.out.println("Episode not found");
                }
            }
            else {
                System.out.println("User not found");
            }
        }
        else {
            System.out.println("Rating must be between 1-10");
        }
    }

    public static void main(String[] args) {
        Insertion insrt = new Insertion();
        System.out.println(insrt.getLatestPersonID());
//        insrt.insertVideoIntoDB("The Room", "I did naht hit her", "2004-03-01", 1, "Kino");
//        insrt.insertFilmIntoDB(120, 2004, 2);
//        insrt.insertPersonIntoDB("Hallvard Trætteberg");
//        insrt.addRoleToPerson("Forfatter", 2);
//        insrt.addRoleToVideo("forfatter", 2, 1);
//        insrt.insertCompanyIntoDb("Norge", "Oslo", "www.norskfilm.no");
//        insrt.insertSeriesIntoDB(2);
//        System.out.println(insrt.checkSeriesExists(2));
//        insrt.insertEpisodeIntoDB(1, 2005, 1, "Tommy returns", "Hallo", 2);
//        insrt.insertUserIntoDB("T-H");
        insrt.addRatingToEpisode(4,1,9);
    }

}
