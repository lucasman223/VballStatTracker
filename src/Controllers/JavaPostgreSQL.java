package Controllers;

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
}
