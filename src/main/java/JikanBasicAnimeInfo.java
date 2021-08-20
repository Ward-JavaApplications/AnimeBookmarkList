import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JikanBasicAnimeInfo {
    @SerializedName("mal_id")
    private int malId;
    private String type;
    private String title;
    private String url;
    private String image_url;


    public JikanBasicAnimeInfo(int malId, String title, String url, String image_url) {
        this.malId = malId;
        this.title = title;
        this.url = url;
        this.image_url = image_url;
    }

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

    public void setMalId(int malId) {
        this.malId = malId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public class JikanBasicAnimeInfoList{
        private List<JikanBasicAnimeInfo> list;

        public List<JikanBasicAnimeInfo> getList() {
            return list;
        }
    }
}
