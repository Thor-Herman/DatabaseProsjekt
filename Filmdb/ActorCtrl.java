import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ActorCtrl extends FilmDBDriver {

    public ActorCtrl() {
        super.connect();
    }

    private int getPersonNr(String actorName) {
        String personNrSQL = "SELECT PersonNr FROM Skuespiller NATURAL JOIN Person WHERE Person.Navn='"+actorName+"'";
        try {
            ResultSet set = connection.createStatement().executeQuery(personNrSQL);
            if (set.next()) {
                return set.getInt("PersonNr");
            }
            else {
                System.out.println("No such actor found");
                return -1;
            }
        }
        catch (Exception e ) {
            System.out.println("Db error when retrieving personNr");
            return -1;
        }
    }

    private ArrayList<String> SQLToStringList(String columnName, String query) {
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

    public ArrayList<String> getActorRoles(String actorName) {
        int personNr = getPersonNr(actorName);
        String rollerSQL = "SELECT Rolle FROM SpillerI WHERE PersonNr='"+personNr+"'";
        return SQLToStringList("Rolle", rollerSQL);
    }

    public ArrayList<String> getActorFilms(String actorName) {
        int personNr = getPersonNr(actorName);
        String filmSQL = "SELECT Video.Tittel " +
                         "FROM Film NATURAL JOIN Video NATURAL JOIN SpillerI " +
                         "WHERE PersonNr ='"+personNr+"'";
        return SQLToStringList("Tittel", filmSQL);
    }

    public static void main(String[] args) {
        ActorCtrl actorCtrl = new ActorCtrl();
        int nr = actorCtrl.getPersonNr("Al Pacino");
        System.out.println(nr);
        ArrayList<String> roles = actorCtrl.getActorRoles("Al Pacino");
        System.out.println(roles);
        ArrayList<String> films = actorCtrl.getActorFilms("Al Pacino");
        System.out.println(films);
    }
}
