//import com.mysql.cj.x.protobuf.MysqlxDatatypes;
//import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
//import org.checkerframework.checker.units.qual.A;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Attributes;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import pw.mihou.jaikan.models.Anime;
//
//import javax.swing.*;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class MyMalScraperAnime {
//    private Anime anime;
//    private ArrayList<RelatedAnimeContainer> additionalMaterial = new ArrayList<>();
//    public MyMalScraperAnime(Anime anime){
//        this.anime = anime;
//        scrape();
//    }
//    private void scrape(){
//        try {
//            Document document = Jsoup.connect(anime.getUrl()).get();
//            //Managers.MyLogger.log(document.toString());
//            Element related = document.select("table.anime_detail_related_anime").first();
//            //System.out.println(related.toString());
//            Elements typesOfRelated = related.select("tr");
//            for(Element element:typesOfRelated){
//                String type = element.select("td").first().text();
//                Elements titles = element.select("a");
//                for(Element title:titles){
//                    if(title.attr("href").startsWith("anime", 1))
//                    additionalMaterial.add(new RelatedAnimeContainer(type,title.attr("href"),title.text()));
////                    System.out.println(type  + " has the following");
////                    System.out.println(title.attr("href"));
////                    System.out.println(title.text() + " is the name of the reference");
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Managers.MyLogger.log(e.getMessage());
//        }
//
//    }
//    public ArrayList<RelatedAnimeContainer> getRelatedAnime(){
//        return additionalMaterial;
//    }
//    public class RelatedAnimeContainer{
//        private String type;
//        private String url;
//        private String name;
//
//        public RelatedAnimeContainer(String type, String url, String name) {
//            this.type = type;
//            this.url = "https://myanimelist.net" + url;
//            this.name = name;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public String getName() {
//            return name;
//        }
//    }
//}
