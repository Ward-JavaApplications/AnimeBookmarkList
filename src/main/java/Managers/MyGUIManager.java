package Managers;

import Exceptions.ErrorMessage;
import GUIFrames.*;
import JikanContainers.AnimeTitle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Locale;

public class MyGUIManager {

    public static DataBaseManager dataBaseManager = new DataBaseManager();
    private int selectedMainFrame; //0=alpahbetical, 1 = priority, 2 = status, 3 = favorite, 4 = search
    private String searchTerm;
    private JFrame mainFrame;
    private int scrollPanelPosition = 0;
    private String userTargetString;
    private JScrollPane mainScrollPane;
    public MyGUIManager(){
        startGUI();
    }
    private void startGUI(){
        mainFrame = new JFrame("Anime BookmarkList");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(500,500);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        showStartMenu(mainFrame);

    }
    private MyGUIManager getParent(){
        return this;
    }

    private void showStartMenu(JFrame frame){
        JPanel mainMenuPanel = new JPanel(new SpringLayout());
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
        JPanel dataReceivePanel = new JPanel();
        JPanel dataInsertPanel = new JPanel();
        JButton showAllTitlesAlphabeticalButton = new JButton("Sort alphabetical");
        showAllTitlesAlphabeticalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesAlphabetical(frame);
                selectedMainFrame = 0;
            }
        });
        dataReceivePanel.add(showAllTitlesAlphabeticalButton);

        JButton showAllTitlesPriorityButton = new JButton("Sort priority");
        showAllTitlesPriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesPriority(frame);
                selectedMainFrame = 1;
            }
        });
        dataReceivePanel.add(showAllTitlesPriorityButton);

        JButton showAllTitlesStatusButton = new JButton("Sort status");
        showAllTitlesStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesStatus(frame);
                selectedMainFrame = 2;
            }
        });
        dataReceivePanel.add(showAllTitlesStatusButton);

        JPanel dataSearchPanel = new JPanel();
        JButton dataSearchButton = new JButton("Search For Title");
        dataSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchAnimeMenu();
            }
        });
        dataSearchPanel.add(dataSearchButton);

        JPanel searchRandomPanel = new JPanel();
        JButton searchRandomButton = new JButton("Get random anime");
    searchRandomButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            AnimeTitle animeTitle = dataBaseManager.getRandomTitle();
            new RandomAnimeFrame(animeTitle.getTitle(),getParent());
        }
    });
    searchRandomPanel.add(searchRandomButton);



        JButton insertNewTitleButton = new JButton("Insert anime");
        insertNewTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertNewTitleMenu();
                
            }
        });
        dataInsertPanel.add(insertNewTitleButton);

        JPanel loadAiringPanel = new JPanel();
        JButton loadAiringButton = new JButton("Load the anime you're currently watching");
        loadAiringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AiringAnimeFrame(getParent());
            }
        });
        loadAiringPanel.add(loadAiringButton);
        JButton loadFavoriteButton = new JButton("Load favorite anime");
        loadFavoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesFavorite(mainFrame);
            }
        });
        loadAiringPanel.add(loadFavoriteButton);

        JPanel loadStatsPanel = new JPanel();
        JButton loadStatsButton = new JButton("Load stats");
        loadStatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StatsFrame();
            }
        });
        loadStatsPanel.add(loadStatsButton);

        JPanel repopulatePanel = new JPanel(new SpringLayout());
        repopulatePanel.setLayout(new BoxLayout(repopulatePanel,BoxLayout.X_AXIS));
        JLabel fillerLabel = new JLabel("                                                                         ");
        repopulatePanel.add(fillerLabel);
        JButton repopulateButton = new JButton("Repopulate from Excel");
        repopulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repopulateMenu();
            }
        });


        JButton dangerZoneButton = new JButton("Danger Zone");
        dangerZoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDangerZoneMenu();
            }
        });
        repopulatePanel.add(dangerZoneButton,SpringLayout.EAST);
        repopulatePanel.add(repopulateButton,SpringLayout.WEST);
        mainMenuPanel.add(dataReceivePanel);
        mainMenuPanel.add(dataInsertPanel);
        mainMenuPanel.add(searchRandomPanel);
        mainMenuPanel.add(dataSearchPanel);
        mainMenuPanel.add(loadAiringPanel);
        mainMenuPanel.add(loadStatsPanel);
        mainMenuPanel.add(repopulatePanel);
        frame.setContentPane(mainMenuPanel);
        SwingUtilities.updateComponentTreeUI(frame);
    }
    private void loadDangerZoneMenu(){
        JFrame dangerZoneFrame = new JFrame("DangerZone");
        dangerZoneFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dangerZoneFrame.setSize(500,500);
        dangerZoneFrame.setLocationRelativeTo(null);
        dangerZoneFrame.setVisible(true);

        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        JButton loadDangerZoneButton = new JButton("Load DangerZone");
        loadDangerZoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDangerZone(dangerZoneFrame);
                //dangerZoneFrame.dispose();
            }
        });
        JButton insertButton = new JButton("Insert title");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertInDangerZone();
                dangerZoneFrame.dispose();
            }
        });
        mainPanel.add(loadDangerZoneButton);
        mainPanel.add(insertButton);
        dangerZoneFrame.setContentPane(mainPanel);
        SwingUtilities.updateComponentTreeUI(dangerZoneFrame);

    }
    private void insertInDangerZone(){
        JFrame insertFrame = new JFrame("Insert new anime");
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(300,200);
        insertFrame.setLocationRelativeTo(null);
        insertFrame.setVisible(true);

        ///setup buttons
        JPanel insertPanel = new JPanel(new SpringLayout());
        insertPanel.setLayout(new BoxLayout(insertPanel, BoxLayout.Y_AXIS));
        //title panel

        JLabel titleLabel = new JLabel("Enter title",SwingConstants.CENTER);
        JTextField titleField = new JTextField();
        insertPanel.add(titleLabel);
        insertPanel.add(titleField);
        //save anime
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                        insertNewAnimeInDangerZone((titleField.getText()));
                        insertFrame.dispose();
                        //refresh();

                }
                catch (Exception exception){
                    new ErrorMessage(exception.getMessage());
                    exception.printStackTrace();
                }
            }

        });
        insertPanel.add(saveButton);
        insertFrame.setContentPane(insertPanel);
        SwingUtilities.updateComponentTreeUI(insertFrame);
    }
    private void loadDangerZone(JFrame frame){
        listToButtonsDangerZone(frame,dataBaseManager.getDangerZone());
    }
    private void searchAnimeMenu(){
        selectedMainFrame = 4;
        new SearchAnimeFrame(this);
    }

    public void searchForTitle(String title,JFrame frame){
        ArrayList<AnimeTitle> animes = dataBaseManager.searchTitleInDB(title);
        listToButtons(frame,animes);
    }
    private void repopulateMenu(){
        JFrame repopulateFrame = new JFrame("RepopulateDB");
        repopulateFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        repopulateFrame.setSize(800,200);
        repopulateFrame.setLocationRelativeTo(null);
        repopulateFrame.setVisible(true);

        JPanel repopulatePanel = new JPanel(new SpringLayout());
        repopulatePanel.setLayout(new BoxLayout(repopulatePanel,BoxLayout.Y_AXIS));
        JLabel warningLabel = new JLabel("Watch out you will be deleting all the data of this DB and going back to the data of an excel file!!!");
        repopulatePanel.add(warningLabel);
        JLabel infoLabel = new JLabel("Type the name of the corresponding xlsx file in the box below");
        String infoFieldString = "xlsx file(don't forget the .xlsx extension at the end";

        JTextField infoField = new JTextField(infoFieldString);
        infoField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                infoField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                userTargetString = infoField.getText();
                infoField.setText(infoFieldString);
            }
        });
        repopulatePanel.add(infoLabel);
        repopulatePanel.add(infoField);
        JButton repopulateButton = new JButton("Confirm repopulation");
        repopulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataBaseManager.populateDBFromExcel(userTargetString);
                    repopulateFrame.dispose();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        });
        repopulatePanel.add(repopulateButton);
        repopulateFrame.setContentPane(repopulatePanel);
        warningLabel.requestFocus();
        SwingUtilities.updateComponentTreeUI(repopulateFrame);
    }

    private void insertNewTitleMenu(){
        new InsertNewTitleFrame(this);
    }

    private void insertNewAnimeInDangerZone(String title){
        dataBaseManager.insertInDangerZone(title);
    }
    public void insertNewAnimeInDB(AnimeTitle anime){
        dataBaseManager.insertInDB(anime);

    }
    private JScrollPane showAllTitlesPriority(JFrame frame){
        ArrayList<AnimeTitle> animeList = dataBaseManager.getDBPriority();
        return listToButtons(frame,animeList);
    }
    private JScrollPane showAllTitlesStatus(JFrame frame){
        ArrayList<AnimeTitle> animeList = dataBaseManager.getDBStatus();
        return listToButtons(frame, animeList);
    }

    private JScrollPane showAllTitlesAlphabetical(JFrame frame) {
        ArrayList<AnimeTitle> animeList = dataBaseManager.getDBAlphabetical();
        return listToButtons(frame, animeList);
    }

    private void listToButtonsDangerZone(JFrame frame, ArrayList<String> animeList){
        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        JPanel panel = new JPanel(new SpringLayout());
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        //Add a button to go back to main menu
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //loadDangerZoneMenu();
                frame.dispose();
            }
        });
        buttonPanel.add(returnButton, BorderLayout.EAST);
        mainPanel.add(buttonPanel);
        //add all the buttons for the different anime
        for(String s: animeList){
            JButton b = new JButton(s);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animeWasClickedDangerZone(b.getText());

                }
            });

            panel.add(b);

        }
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        mainPanel.add(scrollPane);
        frame.setContentPane(mainPanel);
        SwingUtilities.updateComponentTreeUI(frame);
    }
    private JScrollPane listToButtons(JFrame frame, ArrayList<AnimeTitle> animeList) {
        ArrayList<JButton> buttons = new ArrayList<>(animeList.size());
        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        JPanel panel = new JPanel(new SpringLayout());
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        //Add a button to go back to main menu
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStartMenu(frame);
            }
        });
        buttonPanel.add(returnButton, BorderLayout.EAST);
        mainPanel.add(buttonPanel);
        //add all the buttons for the different anime
        ImageIcon star1 = null;
        ImageIcon star2 = null;
        ImageIcon star3 = null;
        int iconSize = 20;
        try{
            star1 = new ImageIcon(ImageIO.read(getClass().getResource("../images/1 star.png")).getScaledInstance(iconSize*2,iconSize,0));
            star2 = new ImageIcon(ImageIO.read(getClass().getResource("../images/2 star.png")).getScaledInstance(iconSize*2,iconSize,0));
            star3 = new ImageIcon(ImageIO.read(getClass().getResource("../images/3 star.png")).getScaledInstance(iconSize*2,iconSize,0));
        }
        catch (Exception e){
            e.printStackTrace();
            MyLogger.log(e.getMessage());
        }
        for(AnimeTitle anime: animeList){
            JButton b = new JButton(anime.getTitle());
            b.setBackground(getButtonColor(anime));
            b.setFont(getFont(anime));
            switch (anime.getFavorite()){
                case 1:
                    b.setIcon(star1);
                    break;
                case 2:
                    b.setIcon(star2);
                    break;
                case 3:
                    b.setIcon(star3);
                    break;
            }
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setScrollPanelPosition(mainScrollPane.getVerticalScrollBar().getValue());
                    animeWasClicked(b.getText());
                }
            });
            b.setHorizontalTextPosition(SwingConstants.LEFT);

            panel.add(b);

        }
        mainScrollPane = new JScrollPane(panel);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(30);
        mainPanel.add(mainScrollPane);
        frame.setContentPane(mainPanel);
        SwingUtilities.updateComponentTreeUI(frame);
        return mainScrollPane;
    }

    private Color getButtonColor(AnimeTitle target){
        int priority = target.getPriority();
        switch (priority){
            case 0:
                return Color.WHITE;
            case 1:
                return Color.CYAN;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.ORANGE;
            case 5:
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }
    private Font getFont(AnimeTitle animeTitle){
        switch (animeTitle.getStatus().toLowerCase(Locale.ROOT)){
            case "watched":
                return new Font("Dialog",Font.BOLD,12);
            case "unwatched":
                return new Font("Dialog", Font.PLAIN,12);
            case "watching":
                return new Font("Dialog", Font.ITALIC,12);
            default:
                new ErrorMessage("Couldn't get the status of the anime.\nCurrent status is " + animeTitle.getStatus());
                return new Font("Dialog",Font.PLAIN,12);
        }
    }
    public Color getButtonColorWhenClicked(String status,JButton button){
        //System.out.println(status+", "+button.getText());
        if(status.toLowerCase(Locale.ROOT).equals(button.getText().toLowerCase(Locale.ROOT))){
            return Color.CYAN;
        }
        else return Color.WHITE;
    }
    private void animeWasClickedDangerZone(String title){
        JFrame insertFrame = new JFrame(title);
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(500,200);
        insertFrame.setLocationRelativeTo(null);
        insertFrame.setVisible(true);

        JPanel titlePanel = new JPanel(new SpringLayout());
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JPanel titleLabelPannel = new JPanel(new FlowLayout());
        JLabel titleLabel = new JLabel(title, SwingUtilities.CENTER);
        titleLabelPannel.add(titleLabel);
        titlePanel.add(titleLabelPannel);

        JButton deleteButton = new JButton("Delete anime");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAnimeDangerZone(title); ;
                insertFrame.dispose();
                loadDangerZoneMenu();

            }
        });
        JPanel priorityPanel = new JPanel(new FlowLayout());
        priorityPanel.add(deleteButton);
        titlePanel.add(priorityPanel);
        insertFrame.setContentPane(titlePanel);
        SwingUtilities.updateComponentTreeUI(insertFrame);
    }


    public void animeWasClicked(String title){
        new AnimeFrame(title,this);

    }
    public void setTitleStatus(String targetStatus, String animeName){
        dataBaseManager.changeAnimeStatus(animeName,targetStatus);
    }

    public void deleteAnimeDangerZone(String animeName){
        dataBaseManager.deleteAnimeEntryDangerZone(animeName);
    }

    public void deleteAnime(String animeName){
        dataBaseManager.deleteAnimeEntry(animeName);
    }
    public void changeAnimeTitle(String oldTitle,String newTitle){
        dataBaseManager.changeAnimeTitle(oldTitle, newTitle);
        refresh();
    }


    public void changePriority(String animeName, int priority){
        dataBaseManager.changePriority(priority,animeName);
    }

    public JScrollPane showAllTitlesFavorite(JFrame frame){
        selectedMainFrame = 3;
        return listToButtons(frame,dataBaseManager.getFromDB("Select * from Anime order by favorite desc,title asc"));

    }

    public void refresh(){
        JScrollPane jScrollPane;
        switch (selectedMainFrame)
        {
            case 0:
                jScrollPane = showAllTitlesAlphabetical(mainFrame);
                jScrollPane.getVerticalScrollBar().setValue(getScrollPanelPosition());
                break;
            case 1:
                jScrollPane = showAllTitlesPriority(mainFrame);
                jScrollPane.getVerticalScrollBar().setValue(getScrollPanelPosition());
                break;
            case 2:
                jScrollPane = showAllTitlesStatus(mainFrame);
                jScrollPane.getVerticalScrollBar().setValue(getScrollPanelPosition());
                break;
            case 3:
                jScrollPane = showAllTitlesFavorite(mainFrame);
                jScrollPane.getVerticalScrollBar().setValue(getScrollPanelPosition());
                break;
            case 4:
                searchForTitle(getSearchTerm(),mainFrame);
                break;
        }
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getScrollPanelPosition() {
        return scrollPanelPosition;
    }

    public void setScrollPanelPosition(int scrollPanelPosition) {
        this.scrollPanelPosition = scrollPanelPosition;
    }
}
