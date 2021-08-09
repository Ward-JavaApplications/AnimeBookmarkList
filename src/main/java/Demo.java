import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        //new MyGUIManager();
        test();
    }

    private static void test(){
        //https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl
        try {
            URL url = new URL("https://api.jikan.moe/v3/anime/1");
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
                parseToJson(json.toString());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    private static void parseToJson(String json){
        JsonObject element = JsonParser.parseString(json).getAsJsonObject();
        System.out.println(element.get("url"));
    }
}
