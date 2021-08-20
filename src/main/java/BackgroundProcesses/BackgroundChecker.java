package BackgroundProcesses;

import JikanContainers.AnimeTitle;
import JikanContainers.JikanAnime;
import Managers.MyGUIManager;
import Notification.AnimeNotification;
import Notification.MessageNotification;
import Requests.AnimeHTMLParser;

import java.util.ArrayList;

public class BackgroundChecker implements Runnable {
    private MyGUIManager parent;
    public BackgroundChecker(MyGUIManager parent){
        this.parent = parent;
    }

    @Override
    public void run() {
        AnimeHTMLParser parser = new AnimeHTMLParser();
        ArrayList<AnimeTitle> animeToCheck = MyGUIManager.dataBaseManager.getCheckListAsArray();
        for (AnimeTitle animeTitle : animeToCheck) {
            JikanAnime cachedAnime = parser.getFromID(animeTitle.getMalID());
            JikanAnime newAnime = parser.getFromIDNoCache(animeTitle.getMalID());
            checkIfRelatedAnimeChanged(cachedAnime,newAnime);
            checkIfStartedAiring(cachedAnime,newAnime);
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException interruptedException){
                interruptedException.printStackTrace();
                return;
            }
        }

    }
    private void checkIfRelatedAnimeChanged(JikanAnime oldOne, JikanAnime newOne){

        ArrayList<AnimeHTMLParser.RelatedAnimeContainer> newRelated = newOne.getRelated();
        ArrayList<AnimeHTMLParser.RelatedAnimeContainer> oldRelated  = oldOne.getRelated();
        if(newRelated.size() != oldRelated.size()) {
            for (AnimeHTMLParser.RelatedAnimeContainer relatedAnimeContainer : newRelated) {
                if (!oldRelated.contains(relatedAnimeContainer.getName())) {
                    //new Related anime added
                    new AnimeNotification(relatedAnimeContainer.getName(),relatedAnimeContainer.getUrl(),parent);
                }
            }
        }
    }
    private void checkIfStartedAiring(JikanAnime oldOne,JikanAnime newOne){
        if(!oldOne.isAiring() && newOne.isAiring()){
            //the anime started airing
            new MessageNotification(newOne.getTitle() + " has startedAiring", newOne.getUrl(),parent);
        }
    }
}