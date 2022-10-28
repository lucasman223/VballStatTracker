package Controllers;

import javafx.scene.control.TableView;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JavaPostgreSQL {

    private static String url = "jdbc:postgresql://localhost:5432/vballStats";
    private static String user = "postgres";
    private static String pass  = "lucas";
    private static Connection con;
    private static int curTeamID;
    private static int curEventID;
    private static int curPlayerID;

    private static String curTeamName;

    public static int getCurTeamID() {
        return curTeamID;
    }
    public static String getCurTeamName() {
        return curTeamName;
    }

    public static void setCurTeamIDs(int teamID, String teamName) {
        System.out.println("SETTING CUR TEAM ID TO: ");
        System.out.println(teamID);
        curTeamID = teamID;
        curTeamName = teamName;
    }

    public static void setCurEventID(int eventID) {
        curEventID = eventID;
    }

    public static void setCurPlayerID(int playerID) {
        curPlayerID = playerID;
    }

    public static void setCurTeamName(String name) {
        curTeamName = name;
    }

    private static void createConn() throws SQLException {
        con = DriverManager.getConnection(url, user, pass);
    }
    public static void writeTeamToDB(String team) throws SQLException {
        createConn();

        String query = "INSERT INTO teams (team_name) VALUES (?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, team);

            pst.executeUpdate();
            System.out.println("Successfully updated");

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    public static Map<Integer, String> queryTeams() throws SQLException {
        createConn();

        String query = "SELECT * FROM teams";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        Map<Integer, String> teams_map = new HashMap<Integer, String>();

        while (rs.next()) {
            teams_map.put(rs.getInt(1), rs.getString(2));
//            System.out.println("col 1 returned: ");
//            System.out.println(rs.getString(1));
        }

        rs.close();
        st.close();

        return teams_map;
    }

    public static void deleteTeam() throws SQLException{
        //TODO may need to delete other things from other tables before deleting the team itself ex events and stats
        //^^single query ok for now
        System.out.println("ID OF TEAM GETTING DELETED");
        System.out.println(String.valueOf(curTeamID));
        createConn();

        String query1 = "DELETE FROM players WHERE team_id = ?::int";
        try (PreparedStatement pst = con.prepareStatement(query1)) {

            pst.setInt(1, curTeamID);

            pst.executeUpdate();
            System.out.println("DELETE PLAYERS ON TEAM");

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }


        String query2 = "DELETE FROM teams WHERE team_id = ?::int";

        try (PreparedStatement pst = con.prepareStatement(query2)) {

            pst.setInt(1, curTeamID);

            pst.executeUpdate();
            System.out.println("DELETE TEAM");

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void queryTeamPlayers(TableView playerList) throws SQLException {
        System.out.println("QUERY TEAM PLAYERS");

        createConn();
        String query = "SELECT number, player_name, player_id FROM players WHERE team_id = ?::int";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, curTeamID);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                int num = rs.getInt(1);
                String name = rs.getString(2);
                int pID = rs.getInt(3);

                Player p = new Player(num, name, pID);
                playerList.getItems().add(p);
            }
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    public static void writePlayerToDB(int num, String name) throws SQLException {
        createConn();

        String query = "INSERT INTO players (team_id, player_name, number) VALUES (?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, curTeamID);
            pst.setString(2, name);
            pst.setInt(3, num);

            pst.executeUpdate();
            System.out.println("Successfully updated players");

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void deletePlayer(int player_id) throws SQLException {
        System.out.println("PLAYER GETTING DELETED");
        System.out.println(player_id);
        createConn();

        String query = "DELETE FROM players WHERE player_id = ?::int";
        try (PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, player_id);

            pst.executeUpdate();
            System.out.println("DELETE PLAYER");

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void alterTeamNameDB(String newTeamName) {
        System.out.println("ALTER TEAM NAME");

        String query = "UPDATE teams SET team_name = ? WHERE team_id = ?::int";
        try (PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, newTeamName);
            pst.setInt(2, curTeamID);

            pst.executeUpdate();
            System.out.println("ALTER TEAM NAME COMPLETE");

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        setCurTeamName(newTeamName);

    }


}
