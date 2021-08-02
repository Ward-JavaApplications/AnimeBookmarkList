import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoints;
import pw.mihou.jaikan.models.Anime;
import pw.mihou.jaikan.models.AnimeResult;

import java.util.ArrayList;
import java.util.List;

public class JaikanSearch {
    private JaikanRetriever parent;
    public JaikanSearch(String title,JaikanRetriever parent){

        this.parent = parent;
        getByTitle(title);
    }
    public JaikanSearch(int id,JaikanRetriever parent){
        this.parent = parent;
        getById(id);
    }
    public void getByTitle(String title){

        List<Anime> animes = new ArrayList<>();
        Jaikan.search(Endpoints.SEARCH,AnimeResult.class,"anime",title)
                .stream().limit(1).forEach(animeResult -> {
                    animes.add(animeResult.asAnime());
                });
        parent.retrieveAnime(animes.get(0));
    }
    private void getById(int id){
        Anime anime = Jaikan.as(Endpoints.OBJECT,Anime.class,"anime",id);
        parent.retrieveAnime(anime);
    }
}
