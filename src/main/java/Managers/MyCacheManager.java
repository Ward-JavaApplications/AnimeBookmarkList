package Managers;

import JikanContainers.JikanAnime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyCacheManager{
    private DataBaseManager dataBaseManager;


    public MyCacheManager(){
        this.dataBaseManager = MyGUIManager.dataBaseManager;
    }

    public void pushToCache(JikanAnime anime){
        System.out.println("pushing");

        if(getFromCache(anime.getId()) == null){
            Gson gson = new Gson();
            String json = gson.toJson(anime);
            dataBaseManager.pushToCache(anime.getId(), json,anime.getCheckRegularly());
        }
    }

    public JikanAnime getFromCache(int id){
        String gson = dataBaseManager.getFromCache(id);
        if(gson == null)return null;
        if(gson.equals("")) return null;
        return jsonToAnime(gson);
    }

    private JikanAnime jsonToAnime(String gson) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JikanAnime anime =  gsonBuilder.create().fromJson(gson, JikanAnime.class);
        return anime;
    }
}
