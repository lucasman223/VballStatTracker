package Controllers;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JavaPostgreSQL {
    public static void writeToDatabase(String team) {
        String url = "jdbc:postgresql://localhost:5432/vballStats";
        String user = "postgres";
        String pass  = "lucas";

        String query = "INSERT INTO teams (team_name) VALUES (?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, team);

            pst.executeUpdate();
            System.out.println("Successfully updated");

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

    }
}
