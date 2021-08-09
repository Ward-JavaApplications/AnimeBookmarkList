import com.google.gson.annotations.SerializedName;

public class JikanBasicAnimeInfo {
    @SerializedName("mal_id")
    private String malId;
    private String type;
    private String name;
    private String url;

    public String getMalId() {
        return malId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
