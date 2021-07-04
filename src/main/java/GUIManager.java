import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUIManager{

    private DataBaseManager dataBaseManager = new DataBaseManager();
    private int selectedMainFrame; //0=alpahbetical, 1 = priority, 2 = status
    JFrame mainFrame;
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
        JPanel mainMenuPanel = new JPanel(new BorderLayout());
        JPanel dataReceivePanel = new JPanel();
        JPanel dataInsertPanel = new JPanel();
        JButton showAllTitlesAlphabeticalButton = new JButton("Show alphabetical");
        showAllTitlesAlphabeticalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesAlphabetical(frame);
                selectedMainFrame = 0;
            }
        });
        dataReceivePanel.add(showAllTitlesAlphabeticalButton);

        JButton showAllTitlesPriorityButton = new JButton("Show priority");
        showAllTitlesPriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesPriority(frame);
                selectedMainFrame = 1;
            }
        });
        dataReceivePanel.add(showAllTitlesPriorityButton);

        JButton showAllTitlesStatusButton = new JButton("Show status");
        showAllTitlesStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesStatus(frame);
                selectedMainFrame = 2;
            }
        });
        dataReceivePanel.add(showAllTitlesStatusButton);

        JButton insertNewTitleButton = new JButton("Insert anime");
        insertNewTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertNewTitleMenu(frame);
                refresh();
            }
        });
        dataInsertPanel.add(insertNewTitleButton);

        mainMenuPanel.add(dataReceivePanel,BorderLayout.NORTH);
        mainMenuPanel.add(dataInsertPanel, BorderLayout.SOUTH);
        frame.setContentPane(mainMenuPanel);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void insertNewTitleMenu(JFrame mainFrame){
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
                        insertNewAnimeInDB(new AnimeTitle(titleField.getText(),status,priority));
                        insertFrame.dispose();
                        refresh();
                    }
                    catch (NumberFormatException numberFormatException)
                    {
                        new ErrorMessage("The given priority is not a valid number");
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
        panel.add(buttonPanel);
        //add all the buttons for the different anime
        for(AnimeTitle anime: animeList){
            JButton b = new JButton(anime.getTitle());
            b.setBackground(getButtonColor(anime));
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
        frame.setContentPane(scrollPane);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private Color getButtonColor(AnimeTitle target){
        if(target.getStatus().equals("Watched"))
            return Color.GREEN;
        if(target.getStatus().equals("Unwatched"))
            return Color.ORANGE;
        else return Color.YELLOW;
    }

    public void animeWasClicked(String title){
        JFrame insertFrame = new JFrame(title);
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(300,200);
        insertFrame.setLocationRelativeTo(null);
        insertFrame.setVisible(true);

        JPanel titlePanel = new JPanel(new SpringLayout());
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JPanel statusPanel = new JPanel(new FlowLayout());

        JLabel titleLabel = new JLabel(title, SwingUtilities.CENTER);
        titlePanel.add(titleLabel);

        JButton watchedButton = new JButton("Watched");
        watchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTitleStatus("Watched", title);
                insertFrame.dispose();
                refresh();
            }
        });
        JButton unwatchedButton = new JButton("Unwatched");
        unwatchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTitleStatus("Unwatched", title);
                insertFrame.dispose();
                refresh();
            }
        });
        JButton watchingButton = new JButton("Watching");
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
