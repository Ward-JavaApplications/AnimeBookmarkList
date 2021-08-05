import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pw.mihou.jaikan.models.Anime;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;

public class MyCacheManager{
    MyGUIManager parent;


    public MyCacheManager(MyGUIManager parent){
        this.parent = parent;
    }

    public void pushToCache(Anime anime, BufferedImage image){

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
//                MyLogger.log(e.getMessage());
//                e.printStackTrace();
//            }
            parent.dataBaseManager.pushToCache(anime.getId(),anime.getTitle(), json);
        }
    }

    public Anime getFromCache(int id){
        String gson = parent.dataBaseManager.getFromCache(id);
        if(gson == null)return null;
        if(gson.equals("")) return null;
        return jsonToAnime(gson);
    }

    private Anime jsonToAnime(String gson) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Anime anime =  gsonBuilder.create().fromJson(gson,Anime.class);
        return anime;
    }

    public Anime getFromCache(String name){
        String gson = parent.dataBaseManager.getFromCache(name);
        if(gson == null) return null;
        if(gson.equals("")) return null;
        return jsonToAnime(gson);
    }


}
