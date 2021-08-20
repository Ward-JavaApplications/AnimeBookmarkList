import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
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
        ArrayList<JikanAnime> suggestions = new AnimeHTMLParser().getSuggestions(animeTitle.getTitle(),5);
        for(JikanAnime anime: suggestions){
            JPanel animePanel = new JPanel(new SpringLayout());
            animePanel.setLayout(new BoxLayout(animePanel,BoxLayout.Y_AXIS));
            String englishTitle = anime.getEnglishTitle();
            if(englishTitle != null && !englishTitle.equals(""))
                animePanel.add(new JLabel(englishTitle), BorderLayout.PAGE_START);
            String japaneseTitle = anime.getJapaneseTitle();
            if(japaneseTitle != null && !japaneseTitle.equals(""))
                animePanel.add(new JLabel(japaneseTitle), BorderLayout.PAGE_START);
            BufferedImage image = null;
            try{
                image = ImageIO.read(new URL(anime.getImage()));
            }
            catch (Exception e){
                new ErrorMessage("couldn't load the image for anime " + anime.getTitle());
                e.printStackTrace();
                MyLogger.log(e.getMessage());
            }
            BufferedImage scaledImage = Scalr.resize(image,Scalr.Mode.FIT_TO_HEIGHT,400);
            animePanel.add(new JLabel(new ImageIcon(scaledImage)),BorderLayout.LINE_START);
            animePanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    saveAnime(new AnimeTitle(anime.getTitle(),anime.getId(),animeTitle.getStatus(),animeTitle.getPriority()),
                        new AnimeHTMLParser().getFromID(anime.getId()));
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
    public void saveAnime(AnimeTitle animeTitle,JikanAnime anime) {
        parent.insertNewAnimeInDB(animeTitle);
        new MyCacheManager().pushToCache(anime);
    }
    public void disposeFrame(){
        mainFrame.dispose();
    }

}
