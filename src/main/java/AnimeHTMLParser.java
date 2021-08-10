import org.apache.poi.hssf.record.pivottable.StreamIDRecord;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
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
        return readAnimeFromURL("https://myanimelist.net/anime/"+id);
    }


    private JikanAnime readAnimeFromURL(String url){

        try {
            JikanAnime jikanAnime = new JikanAnime();
            Document document = Jsoup.connect(url).get();
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

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

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
        for(Element element:typesOfRelated){
            String type = element.select("td").first().text();
            Elements titles = element.select("a");
            for(Element title:titles){
                if(title.attr("href").startsWith("anime", 1))
                    relatedAnimeContainer.add(new RelatedAnimeContainer(type,title.attr("href"),title.text()));
//                    System.out.println(type  + " has the following");
//                    System.out.println(title.attr("href"));
//                    System.out.println(title.text() + " is the name of the reference");
            }
        }
        jikanAnime.setRelated(relatedAnimeContainer);

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
