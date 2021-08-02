import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;

public class JikanTopRequest {
    private MyGUIManager parent;
    private JFrame mainFrame;
    private JPanel mainPanel;

    public JikanTopRequest(MyGUIManager parent){
        this.parent =  parent;
    }

    public void getTopRecommendations(String request,String title){
        loadFrameAndPanel("Recommendations for " + title);
        addTitleToPanelRecommendations(request);


    }

    public void getTopAnime(JFrame frame, JPanel mainPanel, String requestFirstHalf, String requestSecondHalf, int pageNumber){
        String request = requestFirstHalf + String.valueOf(pageNumber) + requestSecondHalf;

        JPanel panel = addTitlesToPanel(frame,mainPanel,requestFirstHalf,requestSecondHalf,pageNumber,request);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        frame.setContentPane(scrollPane);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void getTopAnime(String frameTitle,String requestFirstHalf,String requestSecondHalf,int pageNumber){
        String request = requestFirstHalf + String.valueOf(pageNumber) + requestSecondHalf;

        loadFrameAndPanel(frameTitle);

        addTitlesToPanel(mainFrame, mainPanel,requestFirstHalf, requestSecondHalf, pageNumber, request);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        mainFrame.setContentPane(scrollPane);
        SwingUtilities.updateComponentTreeUI(mainFrame);

    }
    private void loadFrameAndPanel(String frameTitle){
        mainFrame = new JFrame(frameTitle);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(800,800);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
    }
    private void addTitleToPanelRecommendations(String request){
        JPanel scrollPanePanel = new JPanel(new SpringLayout());
        scrollPanePanel.setLayout(new BoxLayout(scrollPanePanel,BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(scrollPanePanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        String json = Jaikan.genericRequest(new Endpoint(request));
        MyLogger.log(json);
        JsonElement element = JsonParser.parseString(json);
        if (element != null) {
            JsonObject obj = element.getAsJsonObject();
            JsonArray pictures = obj.get("recommendations").getAsJsonArray();
            Iterator<JsonElement> listIterator = pictures.iterator();

            int index = 0;
            JPanel rowPanel = new JPanel(new SpringLayout());
            rowPanel.setLayout(new BoxLayout(rowPanel,BoxLayout.X_AXIS));
            while (listIterator.hasNext()) {
                JsonObject anime = listIterator.next().getAsJsonObject();
                String title = anime.get("title").getAsString();
                int amountOfRecommenders = anime.get("recommendation_count").getAsInt();
                String imageURL = anime.get("image_url").getAsString();
                int id = anime.get("mal_id").getAsInt();
                JLabel imageLabel = new JLabel();
                try {
                    BufferedImage image = ImageIO.read(new URL(imageURL));
                    imageLabel.setIcon(new ImageIcon(image));
                }
                catch (Exception e){
                    MyLogger.log(e.getMessage());
                    e.printStackTrace();
                    imageLabel.setText("Image failed to load");
                }
                //JPanel animeTitlePanel = new JPanel(new SpringLayout());
                //animeTitlePanel.setLayout(new BoxLayout(animeTitlePanel,BoxLayout.Y_AXIS));
                JPanel animeTitlePanel = new JPanel(new FlowLayout());
                animeTitlePanel.add(new JLabel(title));
                animeTitlePanel.add(new JLabel("Was recommended by " + amountOfRecommenders + " people"));
                animeTitlePanel.add(imageLabel);
                animeTitlePanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        new NewAnimeFrame(title,id,parent);
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
                scrollPanePanel.add(animeTitlePanel);
                mainFrame.setContentPane(scrollPane);
                SwingUtilities.updateComponentTreeUI(mainFrame);


            }
        }
        mainFrame.setContentPane(scrollPane);
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    private JPanel addTitlesToPanel(JFrame mainFrame, JPanel mainPanel, String requestFirstHalf, String requestSecondHalf, int pageNumber, String request) {
        String json = Jaikan.genericRequest(new Endpoint(request));
        MyLogger.log(json);
        JsonElement element = JsonParser.parseString(json);
        if (element != null) {
            JsonObject obj = element.getAsJsonObject();
            JsonArray pictures = obj.get("top").getAsJsonArray();
            Iterator<JsonElement> listIterator = pictures.iterator();

            int index = 0;
            JPanel rowPanel = new JPanel(new SpringLayout());
            rowPanel.setLayout(new BoxLayout(rowPanel,BoxLayout.X_AXIS));
            while (listIterator.hasNext()) {
                JsonObject anime = listIterator.next().getAsJsonObject();
                String title = anime.get("title").getAsString();
                //System.out.println(title);
                String imageURL = anime.get("image_url").getAsString();
                int id = anime.get("mal_id").getAsInt();
                JLabel imageLabel = new JLabel();
                try {
                    BufferedImage image = ImageIO.read(new URL(imageURL));
                    imageLabel.setIcon(new ImageIcon(image));
                }
                catch (Exception e){
                    MyLogger.log(e.getMessage());
                    e.printStackTrace();
                    imageLabel.setText("Image failed to load");
                }
                JPanel animeTitlePanel = new JPanel(new SpringLayout());
                animeTitlePanel.setLayout(new BoxLayout(animeTitlePanel,BoxLayout.Y_AXIS));
                animeTitlePanel.add(new JLabel(title));
                animeTitlePanel.add(imageLabel);
                animeTitlePanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        new NewAnimeFrame(title,id,parent);
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
                mainPanel.add(animeTitlePanel);
//                        rowPanel.add(animeTitlePanel);
//                        if (index >= 3) {
//                            //need to add to a new row
//                            mainPanel.add(rowPanel);
//                            rowPanel.removeAll();
//                            index = 0;
//                        }
//                        index ++;


            }
            JButton loadMoreButton = new JButton("Load more titles");
            loadMoreButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainPanel.remove(loadMoreButton);
                    getTopAnime(mainFrame,mainPanel, requestFirstHalf, requestSecondHalf, pageNumber +1);
                    SwingUtilities.updateComponentTreeUI(mainFrame);

                }
            });
            mainPanel.add(loadMoreButton);
        }
        return mainPanel;
    }
}
