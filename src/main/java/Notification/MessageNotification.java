package Notification;

import GUIFrames.AnimeFrame;
import Managers.MyGUIManager;
import Requests.AnimeHTMLParser;

import javax.swing.*;
import java.awt.*;

public class MessageNotification extends AnimeNotification {
    /**
     *
     * @param title the message you want to display, in the super this would be the anime title
     * @param url
     * @param parent
     */
    public MessageNotification(String title, String url, MyGUIManager parent){
        super(title, url, parent);
    }

    @Override
    public JPanel getMainPanel(String title, String url) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel(title));
        JButton button = new JButton("Go to anime");
        button.addActionListener(action -> {
            String animeNameInDB = MyGUIManager.dataBaseManager.getFromDB("Select * from anime where anime_id = " +
                    new AnimeHTMLParser().extractIdFromHyper(url)).get(0).getTitle();
            new AnimeFrame(animeNameInDB,getParent());
        });
        panel.add(button);
        return panel;
    }
}
