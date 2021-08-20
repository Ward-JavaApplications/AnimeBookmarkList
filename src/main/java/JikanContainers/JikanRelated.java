package JikanContainers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;

public class JikanRelated {
    JsonObject parentObject;
    public JikanRelated(@NotNull JsonObject parentObject){
        this.parentObject = parentObject;

    }
    public ArrayList<JikanRelatedContainer> getJikanRelated(){
        ArrayList<JikanRelatedContainer> jikanRelatedContainerArrayList = new ArrayList<>();
        Set<String> keySet = parentObject.keySet();
        for(String typeRec:keySet){
            JsonArray jsonArray =  parentObject.get(typeRec).getAsJsonArray();
            jsonArray.forEach(n -> jikanRelatedContainerArrayList.add(new JikanRelatedContainer(typeRec,n.toString())));
        }
        return jikanRelatedContainerArrayList;

    }
    public class JikanRelatedContainer{
        private String typeRec;
        private JikanBasicAnimeInfo jikanBasicAnimeInfo;
        public JikanRelatedContainer(String typeRec,String json){
            this.typeRec = typeRec;
            Gson gson = new Gson();
            this.jikanBasicAnimeInfo = gson.fromJson(json,JikanBasicAnimeInfo.class);
        }
    }
}
