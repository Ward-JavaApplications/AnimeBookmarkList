import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JikanSearch {
    Gson gson;
    public JikanSearch(){
        this.gson = new Gson();
    }
    public JikanAnime getJikanAnime(int id){
        return gson.fromJson(connectAndGetJson("https://api.jikan.moe/v3/anime/"+id),JikanAnime.class);
    }
//    public JikanAnime getJikanAnimeAsync(int id,JaikanRetriever jaikanRetriever){
//        jaikanRetriever.retrieveAnime(getJikanAnime(id));
//    }
    public JikanAnime getJikanAnime(String targetName){
        String url = gson.fromJson(connectAndGetJson("https://api.jikan.moe/v3/search/anime?q="+targetName+"&limit=1"),JikanBasicAnimeInfo.class).getUrl();
        return gson.fromJson(connectAndGetJson(url),JikanAnime.class);
    }
    public String genericRequest(String url){
        return connectAndGetJson(url);
    }

    private String connectAndGetJson(String urlLink){
        //https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl
        try {
            URL url = new URL(urlLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            //System.out.println(responseCode);
            if(responseCode != 200){
                //request failed
                throw new RuntimeException("HttpsResponseCode: " + responseCode);
            }
            else{
                Scanner sc = new Scanner(url.openStream());
                StringBuilder json = new StringBuilder();
                while(sc.hasNext()){
                    json.append(sc.next());
                }
                sc.close();
                return json.toString();
            }
        }
        catch (Exception e){
            new ErrorMessage(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

}
