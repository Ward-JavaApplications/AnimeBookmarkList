import java.sql.*;

public class DataBaseManager {
    public DataBaseManager(){

        Connection conn = null;
        try
        {
            // load the driver class for sqlite
            // in sqlite-jdbc.jar in +libs folder in project folder
            Class.forName("org.sqlite.JDBC");
            // create the connection to the database
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            // create the statement
            Statement stat = conn.createStatement();

            String selectStatement = "SELECT * from Anime"; // put your SQL query here
            // execute the statement on the selected DB and receive the data as a ResultSet
            ResultSet rs = stat.executeQuery(selectStatement);
            // iterate through the resultset row by row
            while (rs.next())
            {
                String title = rs.getString(1);
                System.out.println(title);
            }
            rs.close();
            stat.close();
            conn.close();
        }
        // in case something goes wrong, f.i. incorrect dbname, incorrect select query, driver not found, ...
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}




