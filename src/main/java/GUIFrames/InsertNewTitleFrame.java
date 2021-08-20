package GUIFrames;

import Exceptions.ErrorMessage;
import Managers.MyGUIManager;
import Managers.MyLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertNewTitleFrame {
    private MyGUIManager parent;

    public InsertNewTitleFrame(MyGUIManager parent) {
        this.parent = parent;
        JFrame mainFrame = loadFrame();
        mainFrame.setContentPane(getNewTitleMenu(mainFrame));
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    private JFrame loadFrame() {
        JFrame insertFrame = new JFrame("Insert new anime");
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(300, 300);
        insertFrame.setLocationRelativeTo(null);
        insertFrame.setVisible(true);
        return insertFrame;
    }

    private JPanel getNewTitleMenu(JFrame mainFrame) {

        JikanTopRequest jikanTopRequest = new JikanTopRequest(parent);

        JPanel menu = new JPanel(new SpringLayout());
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        JButton customTitleButton = new JButton("Insert from custom Title");
        customTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CustomTitleFrame(mainFrame,parent);
            }
        });
        menu.add(customTitleButton);
        JButton fromidButton = new JButton("Insert from id");
        fromidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Type the id or the link to myanimelist.net in the box below.");
                try{
                    int id = Integer.parseInt(input);
                    new NewAnimeFrame(id,parent);
                }
                catch (NumberFormatException numberFormatException){
                    //mss als link ingegeven
                    //example link https://myanimelist.net/anime/205/Samurai_Champloo
                    if(input.length()>=30) {
                        String linkID = input.substring(30);
                        String idString = getIDfromString(linkID);
                        try {
                            int id2 = Integer.parseInt(idString);
                            new NewAnimeFrame(id2, parent);
                        } catch (Exception exception) {
                            new ErrorMessage("Couldn't load anime from the given id");
                            exception.printStackTrace();
                            MyLogger.log(exception.getMessage());
                        }
                    }
                    else{
                        new ErrorMessage("Couldn't load anime from the given id");
                    }

                }
            }
        });
        menu.add(fromidButton);
        JButton loadFromUpcoming = new JButton("From upcoming anime");
        loadFromUpcoming.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                jikanTopRequest.getTopAnime("Top upcoming anime", "https://api.jikan.moe/v3/top/anime/", "/upcoming", 1);

            }
        });
        JButton seasonalButton = new JButton("From seasonal anime");
        seasonalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new SeasonalSelectorFrame(parent);
            }
        });
        menu.add(seasonalButton);
        menu.add(loadFromUpcoming);
        JButton loadFromFavorite = new JButton("From top favorites all time");
        loadFromFavorite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                jikanTopRequest.getTopAnime("Top favorite allTime", "https://api.jikan.moe/v3/top/anime/", "/favorite", 1);
            }
        });
        menu.add(loadFromFavorite);
        JButton loadByPopularity = new JButton("From top popular all time");
        loadByPopularity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                jikanTopRequest.getTopAnime("Top popularity", "https://api.jikan.moe/v3/top/anime/", "/bypopularity", 1);
            }
        });
        menu.add(loadByPopularity);
        JButton loadByAiringButton = new JButton("From airing anime");
        loadByAiringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                jikanTopRequest.getTopAnime("Top airing anime", "https://api.jikan.moe/v3/top/anime/", "/airing", 1);
            }
        });
        menu.add(loadByAiringButton);
        return menu;
    }

    private String getIDfromString(String linkID) {
        //search till /
        StringBuilder builder = new StringBuilder();
        for(Character character:linkID.toCharArray()){
            if(character == '/') return builder.toString();
            builder.append(character);
        }
        return null;
    }

}
