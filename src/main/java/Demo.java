import BackgroundProcesses.BackgroundChecker;
import Managers.MyGUIManager;
import Requests.AnimeHTMLParser;

public class Demo {
    public static void main(String[] args) {
        MyGUIManager parent = new MyGUIManager();
        new Thread(new BackgroundChecker(parent)).start();
        parent.startGUI();
    }
}
