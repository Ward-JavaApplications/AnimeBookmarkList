package Managers;

import JikanContainers.JikanAnime;
import Managers.MyGUIManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.image.BufferedImage;

public class MyCacheManager{
    MyGUIManager parent;


    public MyCacheManager(MyGUIManager parent){
        this.parent = parent;
    }

    public void pushToCache(JikanAnime anime, BufferedImage image){

        if(getFromCache(anime.getId()) == null){
            Gson gson = new Gson();
            String json = gson.toJson(anime);
//            Blob blob = null;
//            try {
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                ImageIO.write(image, "", byteArrayOutputStream);
//                byte[] imageBytes = byteArrayOutputStream.toByteArray();
//                blob = new SerialBlob(imageBytes);
//
//            }
//            catch (Exception e){
//                Managers.MyLogger.log(e.getMessage());
//                e.printStackTrace();
//            }
            parent.dataBaseManager.pushToCache(anime.getId(),anime.getTitle(), json);
        }
    }

    public JikanAnime getFromCache(int id){
        String gson = parent.dataBaseManager.getFromCache(id);
        if(gson == null)return null;
        if(gson.equals("")) return null;
        return jsonToAnime(gson);
    }

    private JikanAnime jsonToAnime(String gson) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JikanAnime anime =  gsonBuilder.create().fromJson(gson, JikanAnime.class);
        return anime;
    }

    public JikanAnime getFromCache(String name){
        String gson = parent.dataBaseManager.getFromCache(name);
        if(gson == null) return null;
        if(gson.equals("")) return null;
        return jsonToAnime(gson);
    }


}
