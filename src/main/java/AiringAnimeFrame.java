import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class AiringAnimeFrame {
    private MyGUIManager parent;
    public AiringAnimeFrame(MyGUIManager parent){
        this.parent = parent;
        ArrayList<AiringAnime> title = parent.dataBaseManager.getAiring();
        listToButtons(loadFrame(),title);
    }

    private JFrame loadFrame(){
        JFrame frame = new JFrame("DangerZone");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    private void listToButtons(JFrame frame, ArrayList<AiringAnime> animeList){
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
        for(AiringAnime anime: animeList){
            String s = anime.getTitle();
            JButton b = new JButton(s);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animeClicked(anime,frame);

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
    private void animeClicked(AiringAnime anime,JFrame frame){
        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel(anime.getTitle()));
        mainPanel.add(new JLabel("You are currently at episode " + anime.getEpisode()));
        JPanel changeEpisodePanel = new JPanel(new FlowLayout());
        JTextField episodeTextField = new JTextField();
        episodeTextField.setMinimumSize(new Dimension(20,20));
        episodeTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                episodeTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        JButton episodeChangeButton = new JButton("Change current episode");
        episodeChangeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int newEpisode = Integer.parseInt(episodeTextField.getText());
                parent.dataBaseManager.updateAiringEpisode(anime.getTitle(), newEpisode);
                frame.dispose();
            }
            catch (Exception exception){
                exception.printStackTrace();
                MyLogger.log(exception.getMessage());
            }
        }
    });
        changeEpisodePanel.add(episodeTextField);
        changeEpisodePanel.add(episodeChangeButton);
        mainPanel.add(changeEpisodePanel);
        frame.setContentPane(mainPanel);
        SwingUtilities.updateComponentTreeUI(frame);
        episodeChangeButton.requestFocus();
        episodeTextField.setText(String.valueOf(anime.getEpisode()));
    }
}
