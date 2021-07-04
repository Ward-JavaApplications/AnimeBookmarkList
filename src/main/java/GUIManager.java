import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GUIManager{

    public GUIManager(){
        startGUI();
    }
    private void startGUI(){
        JFrame frame = new JFrame("Anime BookmarkList");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        showStartMenu(frame);

    }
    private void showStartMenu(JFrame frame){
        JPanel mainMenuPanel = new JPanel();
        JPanel dataReceivePanel = new JPanel();
        JButton showAllTitlesAlphabeticalButton = new JButton("Show alphabetical");
        showAllTitlesAlphabeticalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesAlphabetical(frame);
            }
        });
        dataReceivePanel.add(showAllTitlesAlphabeticalButton);

        JButton showAllTitlesPriorityButton = new JButton("Show priority");
        showAllTitlesPriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesPriority(frame);
            }
        });
        dataReceivePanel.add(showAllTitlesPriorityButton);

        JButton showAllTitlesStatusButton = new JButton("Show status");
        showAllTitlesStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTitlesStatus(frame);
            }
        });
        dataReceivePanel.add(showAllTitlesStatusButton);

        mainMenuPanel.add(dataReceivePanel,BorderLayout.SOUTH);
        frame.setContentPane(mainMenuPanel);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void showAllTitlesPriority(JFrame frame){
        DataBaseManager manager = new DataBaseManager();
        ArrayList<AnimeTitle> animeList = manager.getDBPriority();
        listToButtons(frame,animeList);
    }
    private void showAllTitlesStatus(JFrame frame){
        DataBaseManager manager = new DataBaseManager();
        ArrayList<AnimeTitle> animeList = manager.getDBStatus();
        listToButtons(frame, animeList);
    }

    private void showAllTitlesAlphabetical(JFrame frame) {
        DataBaseManager manager = new DataBaseManager();
        ArrayList<AnimeTitle> animeList = manager.getDBAlphabetical();
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
                    displayTitleInfo(b.getText());
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

    public void displayTitleInfo(String title){
        System.out.println(title);
    }
}
