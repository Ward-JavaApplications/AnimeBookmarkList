import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class DataBaseManager {
    public DataBaseManager(){

    }
    public void populateDBFromExcel(String excelFileName){
        clearDB();
        ArrayList<AnimeTitle> animes = populateFromExcel(excelFileName);
        populateDB(animes);

    }
    private void clearDB(){
        Connection conn = null;
        try {
            String querry = "Delete from anime";
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate(querry);
            conn.close();

        }
        catch (Exception e) {
            e.printStackTrace();
            new ErrorMessage(e.getMessage());
        }
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
            new ErrorMessage(e.getMessage());
        }

    }
    private ArrayList<AnimeTitle> populateFromExcel (String excelFileName){
        try {
            ArrayList<AnimeTitle> animes;
            //by default it was "Anime BookmarkList.xlsx"
            File file = new File(excelFileName);
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
        catch (FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
            new ErrorMessage(fileNotFoundException.getMessage());
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            new ErrorMessage(e.getMessage());
            return null;
        }
    }
    public void insertInDangerZone(String title){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "insert into DangerZone (Title)" + " values (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setString(1,title);
            preparedStatement.execute();
            System.out.println(preparedStatement.toString());

            conn.close();

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }
    public void insertInDB(AnimeTitle animeTitle){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "insert into Anime (Title,Status,priority)" + " values (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setString(1,animeTitle.getTitle());
            preparedStatement.setString(2,animeTitle.getStatus());
            preparedStatement.setInt(3,animeTitle.getPriority());
            preparedStatement.execute();
            System.out.println(preparedStatement.toString());

            conn.close();

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }
    public ArrayList<AnimeTitle> getDBStatus(){
        return getFromDB("select * from anime order by case Status COLLATE nocase\n" +
                "when \"Watching\" then 0\n" +
                "when \"Unwatched\" then 1\n" +
                "when \"Watched\" then 2\n" +
                "END");
    }
    public ArrayList<String> getDangerZone(){
        ArrayList<String> dangerZone = getFromDBDangerZone("select * from DangerZone order by Title COLLATE NOCASE ASC");
        return dangerZone;
    }

    public ArrayList<AnimeTitle> getDBPriority(){
        return getFromDB("select * from anime order by Priority DESC,Title COLLATE NOCASE ASC");
    }

    public ArrayList<AnimeTitle> getDBAlphabetical(){
        return getFromDB("SELECT * from Anime order by Title COLLATE NOCASE");

    }
    public ArrayList<AnimeTitle> searchTitleInDB(String title){
        return getFromDB("Select * from anime where Title like \"%" + title+ "%\"");
    }
    public ArrayList<String> getFromDBDangerZone(String querry){
        Connection conn = null;
        System.out.println(querry);
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
            ArrayList<String> animes = new ArrayList<>();
            while (rs.next())
            {
                String title = rs.getString(1);
                animes.add(title);
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
            new ErrorMessage(e.getMessage());
            return null;
        }
    }
    public ArrayList<AnimeTitle> getFromDB(String querry){
        Connection conn = null;
        System.out.println(querry);
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
            new ErrorMessage(e.getMessage());
            return null;
        }
    }
    public void changeAnimeStatus(String title, String status){
        //Update Anime set Status = "mango" WHERE Title = "aaaa"
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "Update Anime set Status = \"" + status + "\" WHERE Title = \"" +title+"\"";
            System.out.println(querry);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate(querry);
            conn.close();
        }
        catch (Exception e)
        {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }


    }
    public void deleteAnimeEntry(String title){
        //delete from anime where Title = "aaaaa"
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "delete from anime where Title = \""+title+"\"";
            System.out.println(querry);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate(querry);
            conn.close();
        }
        catch (Exception e){
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public int getPriority(String anime){
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "select Priority from anime where title = \"" + anime + "\"";
            System.out.println(querry);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            ResultSet rs =  stat.executeQuery(querry);
            int targetInt = rs.getInt(1);
            conn.close();
            return targetInt;



        }
        catch (Exception e){
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public String getStatus(String anime){
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "select Status from anime where title = \"" + anime + "\"";
            System.out.println(querry);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            ResultSet rs =  stat.executeQuery(querry);
            String targetString = rs.getString(1);
            conn.close();
            return targetString;
        }
        catch (Exception e){
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public void changePriority(int priority, String title){
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "Update Anime set Priority = \"" + priority + "\" WHERE Title = \"" +title+"\"";
            System.out.println(querry);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate(querry);
            conn.close();
        }
        catch (Exception e){
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }


}




