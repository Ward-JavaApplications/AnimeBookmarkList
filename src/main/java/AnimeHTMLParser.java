import com.google.gson.Gson;
import org.apache.poi.hssf.record.pivottable.StreamIDRecord;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.xssf.binary.XSSFBUtils;
import org.checkerframework.checker.units.qual.A;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimeHTMLParser {

    public JikanAnime getFromID(int id){
        JikanAnime cached = getIfCached(id);
        if(cached == null)
            return readAnimeFromURL("https://myanimelist.net/anime/"+id,id);
        else return cached;
    }
    public void getFromIDAsync(int id,JikanRetriever parent){
        parent.retrieveAnime(getFromID(id));
    }
    public JikanAnime getFromString(String targetTitle){
        JikanBasicAnimeInfo jikanBasicAnimeInfo = readFromAnimeSearch("https://myanimelist.net/anime.php?cat=anime&q="+targetTitle.replace(" ","+"),1).get(0);
        return getFromID(jikanBasicAnimeInfo.getId());

    }
    public JikanAnime getFromURL(String url){
        int id = extractIdFromHyper(url);
        return getFromID(id);
    }
    public void getFromURLAsync(String url,JikanRetriever parent){
        parent.retrieveAnime(getFromURL(url));
    }
    public ArrayList<JikanAnime> getSuggestions(String targetName,int amount){
        ArrayList<JikanBasicAnimeInfo> list = readFromAnimeSearch("https://myanimelist.net/anime.php?cat=anime&q="+targetName.replace(" ","+"),amount);
        ArrayList<JikanAnime> animes = new ArrayList<>();
        for (JikanBasicAnimeInfo jikanBasicAnimeInfo : list) {
            animes.add(getFromID(jikanBasicAnimeInfo.getId()));
        }
        return animes;
    }
    public void getFromStringAsync(String targetTitle,JikanRetriever parent){
        parent.retrieveAnime(getFromString(targetTitle));
    }
    public ArrayList<JikanRecommendationAnime> getRecommendations(JikanAnime anime){
        ArrayList<JikanRecommendationAnime> animeInfos = new ArrayList<>();
        String request = "https://myanimelist.net/anime/"+anime.getId()+"/"+anime.getJapaneseTitle().replace(" ","_")+"/userrecs";
        try {
            Document document = Jsoup.connect(request).get();
            Elements recommendations = document.select("div.borderclass>table>tbody>tr>td>div>a.hoverinfo_trigger");
            for (Element recommendation : recommendations) {
                String url = recommendation.attr("href");
                Element imgElement = recommendation.selectFirst("img");
                String imageURL = imgElement.attr("data-srcset");
                imageURL = get2xFromImageUrl(imageURL);
                String title = imgElement.attr("alt").substring(7   );
                int id = extractIdFromHyper(url);
                Element recommendationCount = recommendation.parent().parent().parent();
                String amountOfRecommendationsString = recommendationCount.select("td>div>a.js-similar-recommendations-button>strong").text();
                int amountOfRecommendations= 0;
                try{
                    amountOfRecommendations = Integer.parseInt(amountOfRecommendationsString);
                }
                catch (Exception exception){
                    //we have run out of recommendations
                    return animeInfos;
                }
                Element synopsisElement = recommendationCount.parent().selectFirst("div>div.detail-user-recs-text");
                String synopsis = synopsisElement.text();
                animeInfos.add(new JikanRecommendationAnime(synopsis,title,id,url,imageURL,amountOfRecommendations));

            }
            return animeInfos;
            //System.out.println(recommendations.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            MyLogger.log(e.getMessage());
        }
        return null;

    }

    public ArrayList<JikanBasicAnimeInfo> getFromTop(String url){
        return null;
    }

    private String get2xFromImageUrl(String imageURL){
        boolean startParsing = false;
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : imageURL.toCharArray()) {
            if (startParsing) {
                if(c==' ')
                    return stringBuilder.toString();
                else
                    stringBuilder.append(c);
            }
            else if(c == ','){
                startParsing = true;
            }
        }
        return "";
    }

    private JikanAnime getIfCached(int id){
        String jsonAnime = new DataBaseManager().getFromCache(id);
        Gson gson = new Gson();
        JikanAnime anime = gson.fromJson(jsonAnime,JikanAnime.class);
        return anime;
    }


    private ArrayList<JikanBasicAnimeInfo> readFromAnimeSearch(String url,int amountOfResults){
        ArrayList<JikanBasicAnimeInfo> animeInfos = new ArrayList<>();
        try{
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("div.js-categories-seasonal").select("tbody>tr");
            //System.out.println(elements.toString());
            //first element is a header element therefore we will skip this one
            for(int index = 1; index <= amountOfResults;index++){
                Element currentElement = elements.get(index).select("td>div>a").first();
                String animeURL = currentElement.attr("href");
                Element imageAndTitleElement = currentElement.select("img").first();
                String animeName = imageAndTitleElement.attr("alt");
                String imageURL = imageAndTitleElement.attr("data-src");
                int id = extractIdFromHyper(animeURL);
                JikanBasicAnimeInfo jikanBasicAnimeInfo = new JikanBasicAnimeInfo(id,animeName,animeURL,imageURL);
                animeInfos.add(jikanBasicAnimeInfo);
            }
            return animeInfos;

        }
        catch (Exception e){
            e.printStackTrace();
            MyLogger.log(e.getMessage());
        }
        return animeInfos;
    }

    public int extractIdFromHyper(String url){
        String markel = "https://myanimelist.net/anime/";
        url = url.substring(markel.length());
        int id = 0;
        for(Character character:url.toCharArray()){
            try{
                int i = Integer.parseInt(String.valueOf(character));
                id = id*10+i;
            }
            catch (Exception ignored){
                return id;
            }
        }
        return id;
    }


    private JikanAnime readAnimeFromURL(String url,int id){

        try {
            JikanAnime jikanAnime = new JikanAnime();
            Document document = Jsoup.connect(url).get();
            //id
            jikanAnime.setId(id);
            //titles
            Map<String,String> titles = getTitles(document);
            jikanAnime.setJapaneseTitle(titles.get("japaneseTitle"));
            jikanAnime.setEnglishTitle(titles.get("englishTitle"));
            //getFromBorderClass
            Elements borderClass = document.select(".borderClass");
            getBorderClass(borderClass,jikanAnime);
            //url
            jikanAnime.setUrl(url);
            //imageURL
            String imageURL = document.select("div>a>img[data-src]").get(0).attr("data-src");
            jikanAnime.setImage(imageURL);
            //synopsis
            String description = document.select("tr>td>p[itemprop=description]").text();
            jikanAnime.setSynopsis(description);
            //relatedAnime
            getRelatedAnime(document,jikanAnime);

            //push to cash
            try{
                Gson gson = new Gson();
                new DataBaseManager().pushToCache(id,gson.toJson(jikanAnime));
            }
            catch (Exception ignoredException)
            {
                System.out.println("Couldn't push to cache");
            }
            return jikanAnime;

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    private Map<String,String> getTitles(Document document){
        Elements elements = document.select(".h1-title");
        //System.out.println(elements.toString());
        String japanaseTitle = elements.select(".title-name").text();
        String englishTitle = elements.select(".title-english").text();
        Map<String,String> titles = new HashMap<>(2);
        titles.put("japaneseTitle",japanaseTitle);
        titles.put("englishTitle",englishTitle);
        return titles;
    }
    private void getBorderClass(Elements document,JikanAnime jikanAnime){
        Elements elementsSpaceIt = document.select("div.spaceit");
        //episodes
        String episodes = elementsSpaceIt.get(0).ownText();
        int episodesInt = 0;
        try{
            episodesInt = Integer.parseInt(episodes);
        }
        catch (Exception ignored){}
        jikanAnime.setEpisodes(episodesInt);
        //aired
        String aired = elementsSpaceIt.get(1).ownText();
        jikanAnime.setAired(new JikanDates(aired));
        //duration
        String duration = elementsSpaceIt.get(5).ownText();
        jikanAnime.setDuration(duration);
        //genres
        List<String> genresList = new ArrayList<>();
        Elements genres = document.select("span[itemprop=genre]");
        for(Element element: genres){
            genresList.add(element.ownText());
        }
        jikanAnime.setGenres(genresList);
        //statistics
        Elements statistics = document.select(".js-statistics-info");
        String score = statistics.select(".score-label").text();
        double scoreDouble = 0d;
        try{
            scoreDouble = Double.parseDouble(score);
        }
        catch (Exception ignore){}
        jikanAnime.setScore(scoreDouble);
        Elements rankedElements = statistics.select("div:contains(Ranked:)");
        String ranked = rankedElements.text();
        int rankedInt = getRankedFromHastagString(ranked);
        jikanAnime.setRank(rankedInt);
        String popularity = document.select("div>span:contains(Popularity:)").get(0).parent().text();
        int popularityInt = getRankedFromHastagString(popularity+"2 ");
        jikanAnime.setPopularity(popularityInt);

    }

    private int getRankedFromHastagString(String oldString){
        StringBuilder rankedBuilder = new StringBuilder();
        boolean started = false;
        for(Character character:oldString.toCharArray()){
            if(character == '#') started = true;
            else if(started){
                if(character == ' ') {
                    String newString = rankedBuilder.substring(0, rankedBuilder.toString().length() - 1);
                    try{
                        return Integer.parseInt(newString);
                    }
                    catch (Exception e){
                        MyLogger.log(e.getMessage());
                        e.printStackTrace();
                    }
                }
                rankedBuilder.append(character);
            }

        }
        return 0;
    }
    private void getRelatedAnime(Document document,JikanAnime jikanAnime){
        ArrayList<RelatedAnimeContainer> relatedAnimeContainer = new ArrayList<>();
        Element related = document.select("table.anime_detail_related_anime").first();
        //System.out.println(related.toString());
        Elements typesOfRelated = related.select("tr");
        if(typesOfRelated!=null) {
            for (Element element : typesOfRelated) {
                String type = element.select("td").first().text();
                Elements titles = element.select("a");
                for (Element title : titles) {
                    if (title.attr("href").startsWith("anime", 1)){
                        relatedAnimeContainer.add(new RelatedAnimeContainer(type,
                                title.attr("href"), title.text()));
//                        System.out.println(type  + " has the following");
//                        System.out.println(title.attr("href"));
//                        System.out.println(title.text() + " is the name of the reference");
                    }
                }
            }
            jikanAnime.setRelated(relatedAnimeContainer);
        }

    }

    public class RelatedAnimeContainer{
        private String type;
        private String url;
        private String name;

        public RelatedAnimeContainer(String type, String url, String name) {
            this.type = type;
            this.url = "https://myanimelist.net" + url;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }
    }







}
