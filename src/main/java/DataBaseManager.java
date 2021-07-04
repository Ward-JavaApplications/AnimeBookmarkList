import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

public class DataBaseManager {
    public DataBaseManager(){
        populateDBFromExcel();
    }
    public void populateDBFromExcel(){
        try {


            File file = new File("C:\\Users\\wards\\OneDrive\\4docs\\Coding\\Java\\AnimeBookmarkList\\Anime BookmarkList.xlsx");
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            for (int i = 0;i<rowCount+1;i++){
                Row row = sheet.getRow(i);
                if(row != null) {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        if (row.getCell(j) != null)
                            System.out.println(row.getCell(j));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readDB(){
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




