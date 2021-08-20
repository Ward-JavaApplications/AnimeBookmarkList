package JikanContainers;

public class JikanRecommendationAnime {
    private String synopsis;
    private String title;
    private int id;
    private String url;
    private String imageURL;
    private int recommendationCount;

    public JikanRecommendationAnime(String synopsis, String title, int id, String url, String imageURL,int recommendationCount) {
        this.synopsis = synopsis;
        this.title = title;
        this.id = id;
        this.url = url;
        this.imageURL = imageURL;
        this.recommendationCount = recommendationCount;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getRecommendationCount() {
        return recommendationCount;
    }
}
