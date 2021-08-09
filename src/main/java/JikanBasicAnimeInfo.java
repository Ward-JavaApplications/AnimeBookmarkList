import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JikanBasicAnimeInfo {
    @SerializedName("mal_id")
    private int malId;
    private String type;
    private String title;
    private String url;
    private String image_url;

    public int getId() {
        return malId;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image_url;
    }

    public String getUrl() {
        return url;
    }

    public class JikanBasicAnimeInfoList{
        private List<JikanBasicAnimeInfo> list;

        public List<JikanBasicAnimeInfo> getList() {
            return list;
        }
    }
}
