import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pw.mihou.jaikan.models.Anime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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
            MyLogger.log(preparedStatement.toString());
            System.out.println(preparedStatement.toString());

            conn.close();

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }
    public void insertInAiring(String title){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "insert into Airing (Title,Episode)" + " values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setString(1,title);
            preparedStatement.setInt(2,0);
            MyLogger.log(preparedStatement.toString());
            System.out.println(preparedStatement.toString());
            preparedStatement.execute();


            conn.close();

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }
    public ArrayList<AiringAnime> getAiring(){
        Connection conn = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            String selectStatement = "Select * from Airing";
            ResultSet rs = stat.executeQuery(selectStatement);
            ArrayList<AiringAnime> animes = new ArrayList<>();
            while (rs.next())
            {
                String mark = rs.getString(1);
                int episode = rs.getInt(2);
                animes.add(new AiringAnime(mark,episode));
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
    public boolean airingIsPresent(String title){
        Connection conn = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            String selectStatement = "Select * from Airing where Title = \"" + title + "\"";
            ResultSet rs = stat.executeQuery(selectStatement);
            ArrayList<String> animes = new ArrayList<>();
            while (rs.next())
            {
                String mark = rs.getString(1);
                animes.add(mark);
            }
            rs.close();
            stat.close();
            conn.close();
            return !animes.isEmpty();
        }
        // in case something goes wrong, f.i. incorrect dbname, incorrect select query, driver not found, ...
        catch (Exception e)
        {
            e.printStackTrace();
            new ErrorMessage(e.getMessage());
            return true;
        }
    }
    public void updateAiringEpisode(String title, int episodeNR){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "Update Airing set Episode = " + episodeNR + " WHERE Title = \"" +title+"\"";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.execute();
            MyLogger.log(preparedStatement.toString());
            System.out.println(preparedStatement.toString());

            conn.close();

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }
    public void insertInUnreleased(String title,long releaseDate){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "insert into Unreleased (Title,ReleaseDate)" + " values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setString(1,title);
            preparedStatement.setLong(2,releaseDate);
            preparedStatement.execute();
            MyLogger.log(preparedStatement.toString());
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

            String querry = "insert into Anime (Title,Status,priority,released)" + " values (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setString(1,animeTitle.getTitle());
            preparedStatement.setString(2,animeTitle.getStatus());
            preparedStatement.setInt(3,animeTitle.getPriority());
            int i = 1;
            if(!animeTitle.isReleased()) i = 0;
            preparedStatement.setInt(4,i);
            preparedStatement.execute();
            MyLogger.log(preparedStatement.toString());
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
        return getFromDB("select * from anime order by Priority Desc, case Status COLLATE nocase\n" +
                "when \"Watching\" then 0\n" +
                "when \"Unwatched\" then 1\n" +
                "when \"Watched\" then 2\n" +
                "END\n" +
                ", Title COLLATE nocase");
    }

    public ArrayList<AnimeTitle> getDBAlphabetical(){
        return getFromDB("SELECT * from Anime order by Title COLLATE NOCASE");

    }
    public ArrayList<AnimeTitle> searchTitleInDB(String title){
        return getFromDB("Select * from anime where Title like \"%" + title+ "%\"");
    }
    public ArrayList<String> getFromDBDangerZone(String querry){
        Connection conn = null;
        MyLogger.log(querry);
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
        MyLogger.log(querry);
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
                int released = rs.getInt(4);
                boolean releasedBool = (released == 1);
                int malID = rs.getInt(5);
                animes.add(new AnimeTitle(title,malID,status,priority,releasedBool));
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
    public void setPriorityToZero(String title){
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "Update Anime set Priority = 0 WHERE Title = \"" +title+"\"";
            MyLogger.log(querry);
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
    public void changeAnimeStatus(String title, String status){
        //Update Anime set Status = "mango" WHERE Title = "aaaa"
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "Update Anime set Status = \"" + status + "\" WHERE Title = \"" +title+"\"";
            MyLogger.log(querry);
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
    public void updateReleased(String title,boolean released){
        try{
            Class.forName("org.sqlite.JDBC");
            int rel = 1;
            if(!released) rel = 1;
            String querry = "Update Anime set Released = "+rel+" Where title = \"" + title + "\"";
            MyLogger.log(querry);
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
    public void deleteAnimeEntryDangerZone(String title){
        try {
            Class.forName("org.sqlite.JDBC");
            String querry = "delete from DangerZone where Title = \""+title+"\"";
            MyLogger.log(querry);
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
    public void deleteFromAiring(String title){
        //delete from anime where Title = "aaaaa"
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "delete from Airing where Title = \""+title+"\"";
            MyLogger.log(querry);
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
    public void deleteAnimeEntry(String title){
        //delete from anime where Title = "aaaaa"
        try{
            Class.forName("org.sqlite.JDBC");
            String querry = "delete from anime where Title = \""+title+"\"";
            MyLogger.log(querry);
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

    public void changeAnimeTitle(String oldTitle,String newTitle){
        try{
            Class.forName("org.sqlite.JDBC");
            String[] querry = new String[]{ "update Anime set Title = \""+newTitle+"\" where Title = \""+oldTitle+"\"","update Unreleased set Title = \""+newTitle+"\" where Title = \""+oldTitle+"\"","update Airing set Title = \""+newTitle+"\" where Title = \""+oldTitle+"\""};
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            for(String s: querry){
                Statement stat = conn.createStatement();
                stat.executeUpdate(s);
            }
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
            MyLogger.log(querry);
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
            MyLogger.log(querry);
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
            MyLogger.log(querry);
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
    public StatsContainerStatus getStatusStats(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            String querry = "select count(Title) from Anime where status = ";
            String[] types = new String[]{"Watched","Unwatched","Watching"};
            int[] numbs = new int[3];
            int index =0;
            for(String s:types){
                String query = querry + "\"" + s + "\"";
                MyLogger.log(query);
                ResultSet rs = stat.executeQuery(query);
                numbs[index] = rs.getInt(1);
                index++;

            }
            conn.close();
            return new StatsContainerStatus(numbs[0],numbs[2],numbs[1]);
        }
        catch (Exception e){
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public StatsContainerPriority getPriorityStats(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            String querry = "select count(Priority) from Anime where Priority = ";
            int[] types = new int[]{0,1,2,3,4,5};
            int[] numbs = new int[6];
            int index =0;
            for(int s:types){
                String query = querry + s;
                MyLogger.log(query);
                ResultSet rs = stat.executeQuery(query);
                numbs[index] = rs.getInt(1);
                index++;

            }
            conn.close();
            return new StatsContainerPriority(numbs);
        }
        catch (Exception e){
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public AnimeTitle getRandomTitle(){
        String query = "SELECT * FROM Anime WHERE Status = \"Unwatched\" AND Released = 1 ORDER BY RANDOM() LIMIT 1";
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");
            Statement stat = conn.createStatement();
            MyLogger.log(query);
            ResultSet rs = stat.executeQuery(query);
            String title = rs.getString(1);
            String status = rs.getString(2);
            int priority = rs.getInt(3);


            conn.close();
            return new AnimeTitle(title,status,priority);
        }
        catch (Exception e){
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
    public void putMalID(String title,int malID){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "Update Anime set mal_id = " + malID + " WHERE Title = \"" +title+"\"";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.execute();
            MyLogger.log(preparedStatement.toString());
            System.out.println(preparedStatement.toString());

            conn.close();

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }

    }
    public void pushToCache(int id,String title,String gson){

        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "insert into Cache (mal_id,title,gson,image)" + " values (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,title);
            preparedStatement.setString(3,gson);
            preparedStatement.execute();
            MyLogger.log(preparedStatement.toString());
            System.out.println(preparedStatement.toString());

            conn.close();

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
        }

    }
    public String getFromCache(int id){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "select Gson from Cache where mal_id = " + id;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(querry);
            if(resultSet.isClosed()) return null;
            String gson = resultSet.getString(1);
            MyLogger.log(querry);
            System.out.println(querry);

            conn.close();
            return gson;

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public String getFromCache(String name){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + "Anime BookmarkList.db");

            String querry = "Select Gson from Cache where title = \"" + name + "\"";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(querry);
            if (resultSet.isClosed()) return null;
            String gson = resultSet.getString(1);
            MyLogger.log(querry);
            System.out.println(querry);

            conn.close();
            return gson;

        }
        catch (Exception e) {
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}




