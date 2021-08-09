import com.google.gson.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JikanSearch {
    Gson gson;
    private boolean receivedAnswer = false;
    public JikanSearch(){
        this.gson = new Gson();
    }

    public JikanAnime getJikanAnime(int id){
        return gson.fromJson(connectAndGetJsonExact("https://api.jikan.moe/v3/anime/"+id),JikanAnime.class);
    }
    public void getJikanAnimeAsync(int id, JikanRetriever jikanRetriever){
        jikanRetriever.retrieveAnime(getJikanAnime(id));
    }
    public void getJikanAnimeAsync(String targetName, JikanRetriever jikanRetriever){
        jikanRetriever.retrieveAnime(getJikanAnime(targetName));
    }
    public JikanAnime getJikanAnime(String targetName){
        try {
            targetName = makeParsable(targetName);
            String jsonString = connectAndGetJsonExact("https://api.jikan.moe/v3/search/anime?q=" + targetName + "&limit=1");
            //System.out.println("unfinished json " + jsonString);
            JsonElement jsonElement = JsonParser.parseString(jsonString);
            JsonElement targetElements = jsonElement.getAsJsonObject().get("results");
            JsonElement targetElement = targetElements.getAsJsonArray().get(0);
            //System.out.println(targetElement.toString());
            JikanBasicAnimeInfo basicAnimeInfo = gson.fromJson(targetElement.toString(), JikanBasicAnimeInfo.class);
            int id = basicAnimeInfo.getId();
            return getJikanAnime(id);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public String genericRequest(String url){
        return connectAndGetJsonExact(url);
    }
    public ArrayList<JikanBasicAnimeInfo> getSuggestions(String targetName){
        String json = connectAndGetJsonSearch("https://api.jikan.moe/v3/search/anime?q="+targetName+"&limit=5");
        List<JikanBasicAnimeInfo> list = gson.fromJson(json, JikanBasicAnimeInfo.JikanBasicAnimeInfoList.class).getList();
        return new ArrayList<JikanBasicAnimeInfo>(list);
    }

    private String connectAndGetJsonSearch(String urlLink){
        String jsonString = connectAndGetJsonExact(urlLink);
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonElement targetElement = jsonElement.getAsJsonObject().get("results");
        return targetElement.toString();
    }

    private String connectAndGetJsonExact(String urlLink){
        //https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl
        Restarter restarter = new Restarter();
        restarter.setBehaviour(new RestarterBehaviour() {
            @Override
            public void restart() {
                connectAndGetJsonExact(urlLink);
            }
        });
        Thread thread = new Thread(restarter);
        thread.start();
        try {
            System.out.println(urlLink);
            URL url = new URL(urlLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            //System.out.println(responseCode);
            if(responseCode != 200){
                //request failed
                Scanner sc = new Scanner(url.openStream());
                StringBuilder json = new StringBuilder();
                while(sc.hasNextLine()){
                    String line = sc.nextLine();
                    json.append(line);
                }
                sc.close();
                throw new RuntimeException("HttpsResponseCode: " + responseCode + "\n" + json.toString());
            }
            else{
                receivedAnswer = true;

                Scanner sc = new Scanner(url.openStream());
                StringBuilder json = new StringBuilder();
                while(sc.hasNext()){
                    json.append(sc.next());
                }
                sc.close();
                System.out.println("Dees hebben we geladen " + json.toString());
                MyLogger.log(json.toString());
                return json.toString();
            }
        }
        catch (Exception e){
            new ErrorMessage("failed loading anime with the following url " + urlLink);
            e.printStackTrace();
            return null;
        }

    }
    private String makeParsable(String targetString){
        return targetString.replace(" ","+");
    }

    private class Restarter implements Runnable{
        private RestarterBehaviour behaviour;

        public void setBehaviour(RestarterBehaviour behaviour) {
            this.behaviour = behaviour;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(8 * 1000L);
                if(!receivedAnswer){
                    behaviour.restart();
                }
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

        }
    }

    private interface RestarterBehaviour{
        void restart();
    }


}
