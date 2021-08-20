package JikanContainers;

import Requests.AnimeHTMLParser;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    private List<String> genres = Collections.emptyList();
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
    private ArrayList<AnimeHTMLParser.RelatedAnimeContainer> related;

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

    public boolean getCheckRegularly(){
        Date from = aired.getFrom();
        Date to = aired.getTo();
        Date now = new Date();
        if(from == null){
            return true;
        }
        else{
            return to.after(now);
        }

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

    public List<String> getGenres() {
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

    public ArrayList<AnimeHTMLParser.RelatedAnimeContainer> getRelated() {
        return related;
    }

    public String getTitle() {
        if(getEnglishTitle() != null && !getEnglishTitle().equals("")) return englishTitle;
        else if(getJapaneseTitle() !=null && !getJapaneseTitle().equals("")) return japaneseTitle;
        else return "No title was found";
    }

    ///////////////////////////////////////////

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setJapaneseTitle(String japaneseTitle) {
        this.japaneseTitle = japaneseTitle;
    }

    public void setEnglishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
    }

    public void setAnimeTitle(String animeTitle) {
        this.animeTitle = animeTitle;
    }

    public void setTitleSynonyms(List<JikanNameable> titleSynonyms) {
        this.titleSynonyms = titleSynonyms;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAiring(boolean airing) {
        this.airing = airing;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setScoredBy(int scoredBy) {
        this.scoredBy = scoredBy;
    }

    public void setAired(JikanDates aired) {
        this.aired = aired;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setProducers(List<JikanNameable> producers) {
        this.producers = producers;
    }

    public void setLicensors(List<JikanNameable> licensors) {
        this.licensors = licensors;
    }

    public void setStudios(List<JikanNameable> studios) {
        this.studios = studios;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public void setOpening(List<String> opening) {
        this.opening = opening;
    }

    public void setEnding(List<String> ending) {
        this.ending = ending;
    }

    public void setRelated(ArrayList<AnimeHTMLParser.RelatedAnimeContainer> related) {
        this.related = related;
    }
}
