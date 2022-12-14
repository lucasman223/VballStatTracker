package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private static String curPlayerString;

    public static int getCurTeamID() {
        return curTeamID;
    }
    public static String getCurTeamName() {
        return curTeamName;
    }
    public static String getCurEventName() {
        return curEventName;
    }
    public static String getCurPlayerString() {
        return curPlayerString;
    }

    public static void setCurEventIDs(int eventID, String eventName) {
        System.out.println("SETTING CUR EVENT ID: " + eventID);
        System.out.println("SETTING CUR EVENT NAME: " + eventName);
        curEventID = eventID;
        curEventName = eventName;
    }

    public static void setCurPlayerIDs(int playerID) throws SQLException {
        System.out.println("SETTING CUR PLAYER ID: " + playerID);
        curPlayerID = playerID;
        curPlayerString = queryPlayer();
        System.out.println("SETTING CUR PLAYER NAME: " + curPlayerString);

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

    public static String queryPlayer() throws SQLException {
        createConn();
        String query = "SELECT player_name, number FROM players WHERE player_id = ?::int";
        String playerString = "";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, curPlayerID);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                playerString = rs.getString(1) + " " + rs.getInt(2);
            }

            rs.close();

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        closeConn();

        return playerString;
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
        //TODO DELETE EVENTS associated with team
        try {
            createConn();

            String query = "SELECT event_id FROM events WHERE team_id = ?::int";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, curTeamID);
            ResultSet rs = pst.executeQuery();
            closeConn();

            while (rs.next()) {
                deleteEvent(rs.getInt(1));
            }

            createConn();
            query = "DELETE FROM players WHERE team_id = ?::int";
            pst = con.prepareStatement(query);
            pst.setInt(1, curTeamID);

            pst.executeUpdate();

            query = "DELETE FROM teams WHERE team_id = ?::int";
            pst = con.prepareStatement(query);
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

    public static ObservableList<Player> queryTeamPlayers() throws SQLException {
        //TODO update to have an observable list
        //TODO update function to return observable list
        createConn();

        ObservableList<Player> data = FXCollections.observableArrayList();

        String query = "SELECT number, player_name, player_id FROM players WHERE team_id = ?::int";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, curTeamID);
            ResultSet rs = pst.executeQuery();

            System.out.println("QUERY TEAM PLAYERS SUCCESS");
            while(rs.next()) {
                int num = rs.getInt(1);
                String name = rs.getString(2);
                int pID = rs.getInt(3);

                Player p = new Player(num, name, pID, false);
                data.add(p);
            }
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        closeConn();

        return data;
    }

    public static ObservableList<Player> queryEventPlayerList() throws SQLException {
        createConn();

        ObservableList<Player> data = FXCollections.observableArrayList();

        String query = "SELECT p.number, p.player_name, p.player_id, coalesce(pl.event_id, -1) AS selected FROM players p LEFT JOIN (SELECT * FROM player_list WHERE event_id = ?::int) pl ON p.player_id = pl.player_id WHERE p.team_id = ?::int";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, curEventID);
            pst.setInt(2, curTeamID);
            ResultSet rs = pst.executeQuery();

            System.out.println("QUERY EVENT PLAYERS SUCCESS");
            while(rs.next()) {
                int num = rs.getInt(1);
                String name = rs.getString(2);
                int pID = rs.getInt(3);

                int selected = rs.getInt(4);
                boolean b;
                if (selected != -1) {
                    b = true;
                }
                else {
                    b = false;
                }

                Player p = new Player(num, name, pID, b);
                data.add(p);
            }
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        closeConn();

        return data;
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
            con.setAutoCommit(false);

            String query = "DELETE FROM statistics WHERE player_id = ?::int";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, player_id);

            pst.executeUpdate();

            query = "DELETE FROM player_list WHERE player_id = ?::int";
            pst = con.prepareStatement(query);
            pst.setInt(1, player_id);

            pst.executeUpdate();

            query = "DELETE FROM players WHERE player_id = ?::int";
            pst = con.prepareStatement(query);
            pst.setInt(1, player_id);

            pst.executeUpdate();

            con.commit();

            System.out.println("DELETE PLAYER SUCCESS");
        }  catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            con.setAutoCommit(true);
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
        String query = "SELECT event_id, event_name FROM events WHERE team_id = ?::int";
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

    public static void deleteEvent(int eventID) throws SQLException {
        if (eventID == -1) {
            eventID = curEventID; //if -1 arg given then use curEventID else use eventID
        }
        try {
            createConn();
            con.setAutoCommit(false);

            String query = "DELETE FROM statistics WHERE event_id = ?::int";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, eventID);

            pst.executeUpdate();

            query = "DELETE FROM action_list WHERE event_id = ?::int";
            pst = con.prepareStatement(query);
            pst.setInt(1, eventID);

            pst.executeUpdate();

            query = "DELETE FROM player_list WHERE event_id = ?::int";
            pst = con.prepareStatement(query);
            pst.setInt(1, eventID);

            pst.executeUpdate();

            query = "DELETE FROM events WHERE event_id = ?::int";
            pst = con.prepareStatement(query);
            pst.setInt(1, eventID);

            pst.executeUpdate();
            con.commit();

            System.out.println("DELETE EVENT SUCCESS");

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            con.setAutoCommit(true);
            closeConn();

        }
    }

    public static ObservableList<Action> queryActions() throws SQLException {
        createConn();

        ObservableList<Action> data = FXCollections.observableArrayList();

        String query = "SELECT atype.action_type_id, atype.action_name, coalesce(al.event_id, -1) AS selected\n" +
                "FROM action_type atype\n" +
                "LEFT JOIN (SELECT * FROM action_list WHERE event_id = ?::int) al\n" +
                "ON atype.action_type_id = al.action_type_id;";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, curEventID);
            ResultSet rs = pst.executeQuery();

            System.out.println("QUERY ACTIONS SUCCESS");
            while(rs.next()) {
                int action_id = rs.getInt(1);
                String name = rs.getString(2);
                int selected = rs.getInt(3);
                boolean b;
                if (selected != -1) {
                    b = true;
                }
                else {
                    b = false;
                }

                Action a = new Action(action_id, name, b);
                data.add(a);
            }
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        closeConn();

        return data;
    }

    public static void alterEventPlayerList(ObservableList<Player> data) throws SQLException {
        //delete all players with certain event id
        //given a list of selected ids add those to the player_list

        try {
            createConn();
            String query1 = "DELETE FROM player_list WHERE event_id = ?::int";
            PreparedStatement pst = con.prepareStatement(query1);
            pst.setInt(1, curEventID);

            pst.executeUpdate();

            for (Player p: data) {
                if (p.getRemark()) {
                    String insertQuery = "INSERT INTO player_list (event_id, player_id) VALUES (?::int, ?::int)";
                    pst = con.prepareStatement(insertQuery);
                    pst.setInt(1, curEventID);
                    pst.setInt(2, p.getPlayer_id());

                    pst.executeUpdate();
                }
            }
            System.out.println("ALTER PLAYER LIST SUCCESS");
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }

    public static Map<Integer, String> queryPlayersList() throws SQLException {

        createConn();
        Map<Integer, String> players_map = new HashMap<Integer, String>();

        String query = "SELECT pl.player_id, p.player_name \n" +
                "FROM player_list pl \n" +
                "JOIN players p ON pl.player_id = p.player_id \n" +
                "WHERE pl.event_id = ?::int;";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, curEventID);

            ResultSet rs = pst.executeQuery();



            while (rs.next()) {
                players_map.put(rs.getInt(1), rs.getString(2));
            }

            rs.close();
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        closeConn();

        return players_map;
    }

    public static void alterEventActionList(ObservableList<Action> data) throws SQLException {
        //delete all actions with certain event id
        //given a list of selected actions add those to the action_list

        try {
            createConn();
            String query1 = "DELETE FROM action_list WHERE event_id = ?::int";
            PreparedStatement pst = con.prepareStatement(query1);
            pst.setInt(1, curEventID);

            pst.executeUpdate();

            for (Action a: data) {
                if (a.getRemark()) {
                    String insertQuery = "INSERT INTO action_list (event_id, action_type_id) VALUES (?::int, ?::int)";
                    pst = con.prepareStatement(insertQuery);
                    pst.setInt(1, curEventID);
                    pst.setInt(2, a.getActionId());

                    pst.executeUpdate();
                }
            }
            System.out.println("ALTER ACTION LIST SUCCESS");
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }

    public static boolean isEventInitialized() throws SQLException {
        createConn();

        try {
            String query1 = "SELECT CASE WHEN EXISTS (SELECT * FROM player_list WHERE event_id = ?::int LIMIT 1) THEN 1 ELSE 0 END";
            PreparedStatement pst = con.prepareStatement(query1);
            pst.setInt(1, curEventID);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                if (rs.getInt(1) == 0) {
                    return false;
                }
            }

            String query2 = "SELECT CASE WHEN EXISTS (SELECT * FROM action_list WHERE event_id = ?::int LIMIT 1) THEN 1 ELSE 0 END";
            pst = con.prepareStatement(query2);
            pst.setInt(1, curEventID);
            rs = pst.executeQuery();

            System.out.println("QUERY IS EVENT INITALIZED SUCCESS");
            while(rs.next()) {
                if (rs.getInt(1) == 0) {
                    return false;
                }
            }
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        closeConn();

        return true;
    }

    public static ObservableList<Stat> queryStatsTimeline() throws SQLException {
        createConn();

        ObservableList<Stat> data = FXCollections.observableArrayList();

        String query = "SELECT s.player_id, p.player_name, p.number, s.stat_id, s.action_type_id, atype.action_name \n" +
                "FROM statistics s\n" +
                "JOIN players p ON s.player_id = p.player_id\n" +
                "JOIN action_type atype ON s.action_type_id = atype.action_type_id\n" +
                "WHERE s.event_id = ?::int\n" +
                "ORDER BY s.stat_id DESC";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, curEventID);
            ResultSet rs = pst.executeQuery();
            System.out.println( "!!!Cur event ID !!! " + curEventID);

            System.out.println("QUERY STATS TIMELINE SUCCESS");
            while(rs.next()) {
                int player_id = rs.getInt(1);
                String playerName = rs.getString(2);
                int playerNum = rs.getInt(3);
                Player p = new Player(playerNum, playerName, player_id, false);

                int statID = rs.getInt(4);

                int actionTypeID = rs.getInt(5);
                String actionName = rs.getString(6);

                Action a = new Action(actionTypeID, actionName, false);

                Stat s = new Stat(p, a, statID);

                data.add(s);
            }
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        closeConn();

        return data;
    }

    public static Map<Integer, String> queryActionList() throws SQLException {

        createConn();
        Map<Integer, String> action_map = new HashMap<Integer, String>();

        String query = "SELECT al.action_type_id, atype.action_name \n" +
                "FROM action_list al \n" +
                "JOIN action_type atype ON al.action_type_id = atype.action_type_id \n" +
                "WHERE al.event_id = ?;";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, curEventID);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                action_map.put(rs.getInt(1), rs.getString(2));
            }

            rs.close();
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        closeConn();

        return action_map;
    }
    public static void writeStatToDB(int actionID) throws SQLException {
        try {
            createConn();
            String query = "INSERT INTO statistics (event_id, player_id, action_type_id) VALUES (?::int, ?::int, ?::int);";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, curEventID);
            pst.setInt(2, curPlayerID);
            pst.setInt(3, actionID);

            pst.executeUpdate();

            System.out.println("INSERT STAT SUCCESS");
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }

    public static List<Stat> queryStatsList() throws SQLException {
        createConn();

        List<Stat> data = new ArrayList<Stat>();

        String query = "SELECT s.player_id, p.player_name, p.number, s.stat_id, s.action_type_id, atype.action_name \n" +
                "FROM statistics s\n" +
                "JOIN players p ON s.player_id = p.player_id\n" +
                "JOIN action_type atype ON s.action_type_id = atype.action_type_id\n" +
                "WHERE s.event_id = ?::int";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, curEventID);
            ResultSet rs = pst.executeQuery();
            System.out.println( "!!!Cur event ID !!! " + curEventID);

            System.out.println("QUERY STATS TIMELINE SUCCESS");
            while(rs.next()) {
                int player_id = rs.getInt(1);
                String playerName = rs.getString(2);
                int playerNum = rs.getInt(3);
                Player p = new Player(playerNum, playerName, player_id, false);

                int statID = rs.getInt(4);

                int actionTypeID = rs.getInt(5);
                String actionName = rs.getString(6);

                Action a = new Action(actionTypeID, actionName, false);

                Stat s = new Stat(p, a, statID);

                data.add(s);
            }
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        closeConn();

        return data;
    }

    public static List<Player> queryPlayersObjects() throws SQLException {

        createConn();
        List<Player> data = new ArrayList<Player>();

        String query = "SELECT p.number, p.player_name, pl.player_id\n" +
                        "FROM player_list pl\n" +
                        "JOIN players p ON p.player_id = pl.player_id\n" +
                        "WHERE pl.event_id = ?::int";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, curEventID);

            ResultSet rs = pst.executeQuery();



            while (rs.next()) {
                Player p = new Player(rs.getInt(1), rs.getString(2), rs.getInt(3), false);

                data.add(p);
            }

            rs.close();
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        closeConn();

        return data;
    }

    public static void undoStat() throws SQLException {
        try {
            createConn();

            String testquery = "SELECT CASE WHEN EXISTS (SELECT * FROM statistics WHERE event_id = ?::int LIMIT 1) THEN 1 ELSE 0 END";

            PreparedStatement pst = con.prepareStatement(testquery);
            pst.setInt(1, curEventID);
            ResultSet rs = pst.executeQuery();

            System.out.println("QUERY IS EVENT INITALIZED SUCCESS");
            boolean isStatExist = true;
            while(rs.next()) {
                if (rs.getInt(1) == 0) {
                    isStatExist = false;
                    System.out.println("STAT DOES NOT EXIST");
                }
            }

            if (isStatExist) {
                String query = "DELETE FROM statistics\n" +
                        "WHERE ctid IN (\n" +
                        "    SELECT ctid\n" +
                        "    FROM statistics\n" +
                        "    WHERE event_id = ?::int\n" +
                        "    ORDER BY stat_id DESC\n" +
                        "    LIMIT 1\n" +
                        ")";
                pst = con.prepareStatement(query);
                pst.setInt(1, curEventID);

                pst.executeUpdate();

                System.out.println("DELETE STAT SUCCESS");
            }
        }  catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            closeConn();
        }
    }



}
