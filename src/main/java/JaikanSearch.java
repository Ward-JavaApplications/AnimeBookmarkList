//import org.json.JSONException;
//import pw.mihou.jaikan.Jaikan;
//import pw.mihou.jaikan.endpoints.Endpoints;
//import pw.mihou.jaikan.models.Anime;
//import pw.mihou.jaikan.models.AnimeResult;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class JaikanSearch {
//    private JaikanRetriever parent;
//
//    public JaikanSearch(String title,JaikanRetriever parent){
//
//        this.parent = parent;
//        getByTitle(title);
//    }
//    public JaikanSearch(int id,JaikanRetriever parent){
//        this.parent = parent;
//        getById(id);
//    }
//    public JaikanSearch(){
//
//    }
//    public ArrayList<Anime> getSuggestions(String title){
//        ArrayList<Anime> animes = new ArrayList<>();
//        Jaikan.search(Endpoints.SEARCH, AnimeResult.class, "anime", title)
//                .stream().limit(3).forEach(animeResult -> {
//            animes.add(animeResult.asAnime());
//        });
//        return animes;
//    }
//    public void getByTitle(String title){
//        try {
//
//            List<Anime> animes = new ArrayList<>();
//            Jaikan.search(Endpoints.SEARCH, AnimeResult.class, "anime", title)
//                    .stream().limit(1).forEach(animeResult -> {
//                animes.add(animeResult.asAnime());
//            });
//            if(animes.get(0) != null)
//            parent.retrieveAnime(animes.get(0));
//            else {
//                throw new MyJSONException();
//            }
//        }
//        catch(JSONException | MyJSONException myJSONException){
//            new ErrorMessage("Couldn't load the anime, try restarting the app if you would like extra info");
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            new ErrorMessage(e.getMessage());
//        }
//    }
//    private void getById(int id){
//        Anime anime = Jaikan.as(Endpoints.OBJECT,Anime.class,"anime",id);
//        parent.retrieveAnime(anime);
//    }
//    public Anime getByIdSync(int id){
//        return Jaikan.as(Endpoints.OBJECT,Anime.class,"anime",id);
//    }
//    private static class MyJSONException extends Exception{}
//}
