import pw.mihou.jaikan.models.Anime;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;

public class SuggestionFrame extends JPanel {
    MyGUIManager parent;
    JFrame mainFrame;
    AnimeTitle animeTitle;
    public SuggestionFrame(MyGUIManager parent,AnimeTitle animeTitle){
        this.parent = parent;
        this.animeTitle = animeTitle;
        getFrame();
        mainFrame.setContentPane(getJPanel(animeTitle,mainFrame));

    }

    private JFrame getFrame(){
        mainFrame = new JFrame("Suggestion for " + animeTitle.getTitle());
        mainFrame.setSize(1800,600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setVisible(true);
        return mainFrame;
    }
    public JPanel getJPanel(AnimeTitle animeTitle,JFrame frameToDispose){
        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        JPanel suggestionPanel = new JPanel(new SpringLayout());
        suggestionPanel.setLayout(new BoxLayout(suggestionPanel,BoxLayout.X_AXIS));

        JPanel titlePanel = new JPanel(new FlowLayout());
        JLabel titleLabel = new JLabel("The following anime's have been found when looking for title \"" + animeTitle.getTitle() + "\" click on the correct anime.");
        titleLabel.setFont((titleLabel.getFont().deriveFont(Font.BOLD)));
        titleLabel.setFont(titleLabel.getFont().deriveFont(25f));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel);
        ArrayList<Anime> suggestions = new JaikanSearch().getSuggestions(animeTitle.getTitle());
        for(Anime anime: suggestions){
            JPanel animePanel = new JPanel(new SpringLayout());
            animePanel.setLayout(new BoxLayout(animePanel,BoxLayout.Y_AXIS));
            animePanel.add(new JLabel(anime.getTitle()), BorderLayout.PAGE_START);
            Image image = null;
            try{
                image = ImageIO.read(new URL(anime.getImage()));
            }
            catch (Exception e){
                new ErrorMessage("couldn't load the image for anime " + anime.getTitle());
                e.printStackTrace();
                MyLogger.log(e.getMessage());
            }
            animePanel.add(new JLabel(new ImageIcon(image)),BorderLayout.LINE_START);
            animePanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    saveAnime(new AnimeTitle(anime.getTitle(),animeTitle.getStatus(),animeTitle.getPriority()),anime);
                    frameToDispose.dispose();
                    //refresh();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            suggestionPanel.add(animePanel);
        }
        mainPanel.add(suggestionPanel);
        return mainPanel;



    }
    public void saveAnime(AnimeTitle animeTitle,Anime anime) {
        parent.insertNewAnimeInDB(animeTitle);
        new MyCacheManager(parent).pushToCache(anime,null);
    }
    public void disposeFrame(){
        mainFrame.dispose();
    }

}
