import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;

public class DataBaseManager {
    public DataBaseManager(){

    }
    public void populateDBFromExcel(){
        ArrayList<AnimeTitle> animes = populateFromExcel();
        populateDB(animes);

    }
    private void populateDB(ArrayList<AnimeTitle> animes)
    {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            for (AnimeTitle a: animes) {
                String querry = "insert into Anime (Title,Status,priority)" + " values (?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(querry);
                preparedStatement.setString(1,a.getTitle());
                preparedStatement.setString(2,a.getStatus());
                preparedStatement.setInt(3,0);
                preparedStatement.execute();
            }
            conn.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    private ArrayList<AnimeTitle> populateFromExcel(){
        try {
            ArrayList<AnimeTitle> animes;
            File file = new File("C:\\Users\\wards\\OneDrive\\4docs\\Coding\\Java\\AnimeBookmarkList\\Anime BookmarkList.xlsx");
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            animes = new ArrayList<>(rowCount);
            for (int i = 0;i<rowCount+1;i++){
                Row row = sheet.getRow(i);
                if(row != null) {
                    String title = new String();
                    String status = new String();
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        if (row.getCell(j) != null) {
                            switch (j){
                                case 0:
                                    title = row.getCell(j).getStringCellValue();
                                    //System.out.println(row.getCell(j));
                                    break;
                                case 2:
                                    status = row.getCell(j).getStringCellValue();
                                    //System.out.println(row.getCell(j));
                                    break;
                            }
                        }
                    }
                    AnimeTitle animeTitle = new AnimeTitle(title,status);
                    animes.add(animeTitle);
                }
            }
            return animes;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<AnimeTitle> getDBStatus(){
        return getFromDB("SELECT * from anime order by Status, Title");
    }

    public ArrayList<AnimeTitle> getDBPriority(){
        return getFromDB("SELECT * from anime order by Priority, Title");
    }

    public ArrayList<AnimeTitle> getDBAlphabetical(){
        return getFromDB("SELECT * from Anime order by Title");

    }
    private ArrayList<AnimeTitle> getFromDB(String querry){
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

            String selectStatement = querry; // put your SQL query here
            // execute the statement on the selected DB and receive the data as a ResultSet
            ResultSet rs = stat.executeQuery(selectStatement);
            // iterate through the resultset row by row
            ArrayList<AnimeTitle> animes = new ArrayList<>();
            while (rs.next())
            {
                String title = rs.getString(1);
                String status = rs.getString(2);
                int priority = rs.getInt(3);
                animes.add(new AnimeTitle(title,status,priority));
            }
            rs.close();
            stat.close();
            conn.close();
            return animes;
        }
        // in case something goes wrong, f.i. incorrect dbname, incorrect select query, driver not found, ...
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}




