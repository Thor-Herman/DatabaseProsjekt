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

    private void createIntQuery(String query, int[] columnValues, int columnAmounts) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (columnAmounts < 1)
                throw new IllegalArgumentException("Column amount must be at least 1");
            else if (columnAmounts != columnValues.length)
                throw new IllegalArgumentException("Column amount must be equal to length of column values");
            for (int i = 0; i < columnValues.length; i++) {
                preparedStatement.setInt(i+1, columnValues[i]);
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        if (! personHasRole(role, personNr)) {
            String roleQuery = determineRoleQuery(role, false);
            int[] roles = {personNr};
            createIntQuery(roleQuery, roles, 1);
        }
        else {
            System.out.println("Person already has that role");
        }
    }

    private boolean personHasRole(String role, int personNr) {
        StringBuffer roleQueryBuff = new StringBuffer();
        roleQueryBuff.append("SELECT * FROM Person NATURAL JOIN ");
        roleQueryBuff.append(role);
        roleQueryBuff.append(" WHERE PersonNr =");
        roleQueryBuff.append(personNr);
        String roleQuery = roleQueryBuff.toString();
        try {
            ResultSet set = connection.createStatement().executeQuery(roleQuery);
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addRoleToVideo(String role, int personNr, int videoID) {
        if (Arrays.stream(acceptableRoles).noneMatch(
                acceptableRoleVerb -> acceptableRoleVerb.toLowerCase().equals(role.toLowerCase()))) {
            System.out.println(role + " is not an acceptable roleVerb");
        }
        if (! personHasRole(role, personNr)) {
            addRoleToPerson(role, personNr);
            System.out.println("Added role " + role + " to this person");
        }
        String roleQuery = determineRoleQuery(role, true);
        int[] foreignKeys = {personNr, videoID};
        createIntQuery(roleQuery, foreignKeys, 2);
    }

    public static void main(String[] args) {
        Insertion insrt = new Insertion();
        System.out.println(insrt.getLatestPersonID());
        insrt.insertVideoIntoDB("The Room", "I did naht hit her", "2004-03-01", 1, "Kino");
        insrt.insertFilmIntoDB(120, 2004, 2);
        insrt.insertPersonIntoDB("Hallvard Trætteberg");
        insrt.addRoleToPerson("Forfatter", 2);
        insrt.addRoleToVideo("forfatter", 2, 1);
    }

}
