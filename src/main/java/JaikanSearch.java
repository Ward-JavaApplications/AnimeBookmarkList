import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoints;
import pw.mihou.jaikan.models.Anime;
import pw.mihou.jaikan.models.AnimeResult;

import java.util.ArrayList;
import java.util.List;

public class JaikanSearch {
    private String title;
    public JaikanSearch(String title){
        this.title = title;
    }
    public Anime getByTitle(){
        List<Anime> animes = new ArrayList<>();
        Jaikan.search(Endpoints.SEARCH,AnimeResult.class,"anime",title)
                .stream().limit(1).forEach(animeResult -> {
                    animes.add(animeResult.asAnime());
                });
        return animes.get(0);
    }
}
