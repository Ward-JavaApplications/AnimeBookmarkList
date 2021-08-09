import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JikanAnime {

    @SerializedName("mal_id")
    private int id = 0;
    private String url = "";
    @SerializedName("image_url")
    private String image = "";
    @SerializedName("title")
    private String japaneseTitle = "";
    @SerializedName("title_english")
    private String englishTitle = "";
    private String animeTitle;
    @SerializedName("title_synonyms")
    private List<JikanNameable> titleSynonyms;
    private String status = "";
    private boolean airing = false;
    private String synopsis = "";
    private String type = "";
    private int episodes = 0;
    private int rank = 0;
    private double score = 0.0;
    @SerializedName("scored_by")
    private int scoredBy = 0;
    private JikanDates aired;
    private int members = 0;
    private int popularity = 0;
    private int favorites = 0;
    private List<JikanNameable> genres = Collections.emptyList();
    private String premiered = "";
    private String background = "";
    private List<JikanNameable> producers = Collections.emptyList();
    private List<JikanNameable> licensors = Collections.emptyList();
    private List<JikanNameable> studios = Collections.emptyList();
    private String rating = "";
    private String duration = "";
    private String source = "";
    private String broadcast = "";
    @SerializedName("opening_themes")
    private List<String> opening = Collections.emptyList();
    @SerializedName("ending_Themes")
    private List<String> ending = Collections.emptyList();
    private JsonObject related;

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public String getJapaneseTitle() {
        return japaneseTitle;
    }

    public boolean isAiring() {
        return airing;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getType() {
        return type;
    }

    public int getEpisodes() {
        return episodes;
    }

    public int getRank() {
        return rank;
    }

    public double getScore() {
        return score;
    }

    public JikanDates getAired() {
        return aired;
    }

    public int getMembers() {
        return members;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getFavorites() {
        return favorites;
    }

    public List<JikanNameable> getGenres() {
        return genres;
    }

    public String getPremiered() {
        return premiered;
    }

    public String getBackground() {
        return background;
    }

    public List<JikanNameable> getProducers() {
        return producers;
    }

    public List<JikanNameable> getLicensors() {
        return licensors;
    }

    public List<JikanNameable> getStudios() {
        return studios;
    }

    public String getRating() {
        return rating;
    }

    public String getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public List<String> getOpening() {
        return opening;
    }

    public List<String> getEnding() {
        return ending;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public List<JikanNameable> getTitleSynonyms() {
        return titleSynonyms;
    }

    public int getScoredBy() {
        return scoredBy;
    }

    public ArrayList<JikanRelated.JikanRelatedContainer> getRelated() {
        return new JikanRelated(related).getJikanRelated();
    }

    public String getTitle() {
        return animeTitle;
    }
}
