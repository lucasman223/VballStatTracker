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
    private static String curEventName;

    public static int getCurTeamID() {
        return curTeamID;
    }
    public static String getCurTeamName() {
        return curTeamName;
    }
    public static String getCurEventName() {
        return curEventName;
    }

    public static void setCurEventIDs(int eventID, String eventName) {
        System.out.println("SETTING CUR EVENT ID: " + eventID);
        System.out.println("SETTING CUR EVENT NAME: " + eventName);
        curEventID = eventID;
        curEventName = eventName;
    }

    public static void setCurPlayerID(int playerID) {
        curPlayerID = playerID;
    }

    public static void setCurTeamName(String name) {
        curTeamName = name;
    }

    public static void setCurTeamIDs(int teamID, String teamName) {
        System.out.println("SETTING CUR TEAM ID: " + teamID);
        System.out.println("SETTING CUR TEAM NAME: " + teamName);
        curTeamID = teamID;
        curTeamName = teamName;
    }

    private static void createConn() throws SQLException {
        con = DriverManager.getConnection(url, user, pass);
    }

    private  static void closeConn() throws SQLException {
        con.close();
    }
    public static void writeTeamToDB(String team) throws SQLException {
        try {
            createConn();
            String query = "INSERT INTO teams (team_name) VALUES (?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, team);

            pst.executeUpdate();

            System.out.println("INSERT TEAM SUCCESS");
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }

    public static void writeEventToDB(String event) throws SQLException{
        try {
            createConn();
            String query = "INSERT INTO events (team_id, event_name) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, curTeamID);
            pst.setString(2, event);

            pst.executeUpdate();

            System.out.println("INSERT EVENT SUCCESS");
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
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
        }

        rs.close();
        st.close();
        closeConn();

        return teams_map;
    }

    public static void deleteTeam() throws SQLException{
        //TODO may need to delete other things from other tables before deleting the team itself ex events and stats
        try {
            createConn();
            String query1 = "DELETE FROM players WHERE team_id = ?::int";
            PreparedStatement pst = con.prepareStatement(query1);
            pst.setInt(1, curTeamID);

            pst.executeUpdate();

            String query2 = "DELETE FROM teams WHERE team_id = ?::int";
            pst = con.prepareStatement(query2);
            pst.setInt(1, curTeamID);

            pst.executeUpdate();

            System.out.println("DELETE TEAM SUCCESS");
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }

    public static void queryTeamPlayers(TableView playerList) throws SQLException {
        createConn();
        String query = "SELECT number, player_name, player_id FROM players WHERE team_id = ?::int";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, curTeamID);
            ResultSet rs = pst.executeQuery();

            System.out.println("QUERY TEAM PLAYERS SUCCESS");
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
        closeConn();
    }

    public static void writePlayerToDB(int num, String name) throws SQLException {
        try {
            createConn();
            String query = "INSERT INTO players (team_id, player_name, number) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, curTeamID);
            pst.setString(2, name);
            pst.setInt(3, num);

            pst.executeUpdate();

            System.out.println("INSERT PLAYER SUCCESS");
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }

    public static void deletePlayer(int player_id) throws SQLException {
        try {
            createConn();
            String query = "DELETE FROM players WHERE player_id = ?::int";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, player_id);

            pst.executeUpdate();

            System.out.println("DELETE PLAYER SUCCESS");
        }  catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }

    public static void alterTeamNameDB(String newTeamName) throws SQLException {
        try {
            createConn();
            String query = "UPDATE teams SET team_name = ? WHERE team_id = ?::int";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, newTeamName);
            pst.setInt(2, curTeamID);

            pst.executeUpdate();

            System.out.println("ALTER TEAM NAME COMPLETE");
            setCurTeamName(newTeamName);
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }

    public static Map<Integer, String> queryEvents() throws SQLException {
        createConn();
        String query = "SELECT team_id, event_name FROM events WHERE team_id = ?::int";
        Map<Integer, String> events_map = new HashMap<Integer, String>();

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, curTeamID);
            ResultSet rs = pst.executeQuery();

            System.out.println("QUERY EVENTS SUCCESS");
            while(rs.next()) {
                events_map.put(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        closeConn();

        return events_map;
    }


}
