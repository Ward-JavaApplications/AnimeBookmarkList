import com.mysql.cj.protocol.a.NativeConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileNotFoundException;
import java.time.chrono.JapaneseChronology;
import java.util.ArrayList;
import java.util.Locale;

public class GUIManager{

    private DataBaseManager dataBaseManager = new DataBaseManager();
    private int selectedMainFrame; //0=alpahbetical, 1 = priority, 2 = status
    JFrame mainFrame;
    String userTargetString;
    public GUIManager(){
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


        JButton insertNewTitleButton = new JButton("Insert anime");
        insertNewTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertNewTitleMenu();
                
            }
        });
        dataInsertPanel.add(insertNewTitleButton);

        JPanel repopulatePanel = new JPanel(new SpringLayout());
        repopulatePanel.setLayout(new BoxLayout(repopulatePanel,BoxLayout.X_AXIS));
        JLabel fillerLabel = new JLabel("                                                                                                      ");
        repopulatePanel.add(fillerLabel);
        JButton repopulateButton = new JButton("Repopulate from Excel");
        repopulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repopulateMenu();
            }
        });
        repopulatePanel.add(repopulateButton,SpringLayout.WEST);

        mainMenuPanel.add(dataReceivePanel);
        mainMenuPanel.add(dataInsertPanel);
        mainMenuPanel.add(dataSearchPanel);
        mainMenuPanel.add(repopulatePanel);
        frame.setContentPane(mainMenuPanel);
        SwingUtilities.updateComponentTreeUI(frame);
    }
    private void searchAnimeMenu(){
        JPanel searchPanel = new JPanel(new SpringLayout());
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));
        JLabel searchLabel = new JLabel("Type the name in the box below, better to type less than wrong");
        searchPanel.add(searchLabel);
        JTextField searchField = new JTextField();
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchForTitle(searchField.getText(),mainFrame);

            }
        });
        searchPanel.add(searchButton);

        mainFrame.setContentPane(searchPanel);
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }
    private void searchForTitle(String title,JFrame frame){
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
        //status
        JCheckBox checkBox = new JCheckBox("watched", false);
        insertPanel.add(checkBox);
        //priority
        JLabel priorityLabel = new JLabel("Priority", SwingConstants.CENTER);
        JTextField priorityField = new JTextField();
        insertPanel.add(priorityLabel);
        insertPanel.add(priorityField);
        //save anime
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    try{
                        String status = new String();
                        if(checkBox.isSelected()) status = "Watched";
                        else status = "Unwatched";
                        int priority = 0;
                        if (!priorityField.getText().equals("")) priority = Integer.parseInt(priorityField.getText());
                        else priority = 0;
                        if(priority<=5 && priority>=0) {
                            insertNewAnimeInDB(new AnimeTitle(titleField.getText(), status, priority));
                            insertFrame.dispose();
                            //refresh();
                        }
                        else throw new NumberRangeException(0,5);
                    }
                    catch (NumberFormatException numberFormatException)
                    {
                        new ErrorMessage("The given priority is not a valid number");
                    }
                    catch (NumberRangeException numberRangeException){
                        new ErrorMessage("The priority needs to be between: " + numberRangeException.getMinRange() + " and " + numberRangeException.getMaxRange());
                    }
                }

        });
        insertPanel.add(saveButton);
        insertFrame.setContentPane(insertPanel);
        SwingUtilities.updateComponentTreeUI(insertFrame);


    }

    private void insertNewAnimeInDB(AnimeTitle anime){
        dataBaseManager.insertInDB(anime);

    }
    private void showAllTitlesPriority(JFrame frame){
        ArrayList<AnimeTitle> animeList = dataBaseManager.getDBPriority();
        listToButtons(frame,animeList);
    }
    private void showAllTitlesStatus(JFrame frame){
        ArrayList<AnimeTitle> animeList = dataBaseManager.getDBStatus();
        listToButtons(frame, animeList);
    }

    private void showAllTitlesAlphabetical(JFrame frame) {
        ArrayList<AnimeTitle> animeList = dataBaseManager.getDBAlphabetical();
        listToButtons(frame, animeList);
    }

    private void listToButtons(JFrame frame, ArrayList<AnimeTitle> animeList) {
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
        for(AnimeTitle anime: animeList){
            JButton b = new JButton(anime.getTitle());
            b.setBackground(getButtonColor(anime));
            b.setFont(getFont(anime));
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animeWasClicked(b.getText());
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

    private Color getButtonColor(AnimeTitle target){
        int priority = target.getPriority();
        switch (priority){
            case 0:
                return Color.WHITE;
            case 1:
                return Color.BLUE;
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
    private Color getButtonColorWhenClicked(String status,JButton button){
        //System.out.println(status+", "+button.getText());
        if(status.toLowerCase(Locale.ROOT).equals(button.getText().toLowerCase(Locale.ROOT))){
            return Color.CYAN;
        }
        else return Color.WHITE;
    }

    public void animeWasClicked(String title){
        //getStatus of the anime
        String status = dataBaseManager.getStatus(title);
        JFrame insertFrame = new JFrame(title);
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(500,200);
        insertFrame.setLocationRelativeTo(null);
        insertFrame.setVisible(true);

        JPanel titlePanel = new JPanel(new SpringLayout());
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JPanel statusPanel = new JPanel(new FlowLayout());

        JPanel titleLabelPannel = new JPanel(new FlowLayout());
        JLabel titleLabel = new JLabel(title, SwingUtilities.CENTER);
        titleLabelPannel.add(titleLabel);
        titlePanel.add(titleLabelPannel);

        JButton watchedButton = new JButton("Watched");
        watchedButton.setBackground(getButtonColorWhenClicked(status,watchedButton));
        watchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTitleStatus("Watched", title);
                insertFrame.dispose();
                refresh();
            }
        });
        JButton unwatchedButton = new JButton("Unwatched");
        unwatchedButton.setBackground(getButtonColorWhenClicked(status,unwatchedButton));
        unwatchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTitleStatus("Unwatched", title);
                insertFrame.dispose();
                refresh();
            }
        });
        JButton watchingButton = new JButton("Watching");
        watchingButton.setBackground(getButtonColorWhenClicked(status,watchingButton));
        watchingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTitleStatus("Watching", title);
                insertFrame.dispose();
                refresh();
            }
        });
        statusPanel.add(watchedButton);
        statusPanel.add(watchingButton);
        statusPanel.add(unwatchedButton);
        titlePanel.add(statusPanel);
        JPanel priorityPanel = new JPanel(new FlowLayout());
        int currentPriority = dataBaseManager.getPriority(title);
        JTextField priorityTextField = new JTextField(String.valueOf(currentPriority));
        priorityTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                priorityTextField.setText("");
            }
        });
        JButton priorityButton = new JButton("Change priority");
        priorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int priority = Integer.parseInt(priorityTextField.getText());
                    changePriority(title, priority);
                    insertFrame.dispose();
                }
                catch (NumberFormatException numberFormatException)
                {
                    new ErrorMessage("The given priority was not a valid number");
                }
            }
        });
        priorityPanel.add(priorityTextField);
        priorityPanel.add(priorityButton);

        JButton deleteButton = new JButton("Delete anime");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAnime(title) ;
                insertFrame.dispose();
                refresh();

            }
        });
        priorityPanel.add(deleteButton);
        titlePanel.add(priorityPanel);
        insertFrame.setContentPane(titlePanel);
        SwingUtilities.updateComponentTreeUI(insertFrame);

    }
    private void setTitleStatus(String targetStatus, String animeName){
        dataBaseManager.changeAnimeStatus(animeName,targetStatus);
    }

    private void deleteAnime(String animeName){
        dataBaseManager.deleteAnimeEntry(animeName);
    }

    private void changePriority(String animeName, int priority){
        dataBaseManager.changePriority(priority,animeName);
    }

    private void refresh(){
        switch (selectedMainFrame)
        {
            case 0:
                showAllTitlesAlphabetical(mainFrame);
                break;
            case 1:
                showAllTitlesPriority(mainFrame);
                break;
            case 2:
                showAllTitlesStatus(mainFrame);
                break;
        }
    }

}
