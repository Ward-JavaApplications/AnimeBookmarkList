import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoint;
import pw.mihou.jaikan.models.Anime;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class JikanTopRequest {
    private MyGUIManager parent;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JikanAnime jikanAnime;

    public JikanTopRequest(MyGUIManager parent){
        this.parent =  parent;
    }


    public void getTopRecommendations(String request, JikanAnime anime){
        this.jikanAnime = anime;
        loadFrameAndPanel("Recommendations for " + anime.getTitle());
        String malButtonRequest = "https://myanimelist.net/anime/"+anime.getId()+"/"+anime.getTitle().replace(" ","_")+"/userrecs";
        addTitleToPanelRecommendations(request,malButtonRequest);


    }
    public void getSeasonAnime(String frameTitle,String year,String season){
        String malButtonRequest = "https://myanimelist.net/anime/season/" + year + "/" + season;
        String request = "https://api.jikan.moe/v3/season/" + year + "/" + season;


        loadFrameAndPanel(frameTitle);

        addMalButton(mainPanel,malButtonRequest);

        mainPanel.add(addTitlesToPanelSeasonal(request,malButtonRequest));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        mainFrame.setContentPane(scrollPane);
        SwingUtilities.updateComponentTreeUI(mainFrame);
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
        String malButtonRequest = "https://myanimelist.net/topanime.php?type=" + requestSecondHalf.substring(1);
        System.out.println(malButtonRequest);

        loadFrameAndPanel(frameTitle);

        addMalButton(mainPanel,malButtonRequest);

        addTitlesToPanel(mainFrame, mainPanel,requestFirstHalf, requestSecondHalf, pageNumber, request);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        mainFrame.setContentPane(scrollPane);
        SwingUtilities.updateComponentTreeUI(mainFrame);

    }
    private JPanel addMalButton(JPanel panel,String request){
        JPanel buttonPanel = new JPanel(new BorderLayout());
        Image image = null;
        try{
            image = ImageIO.read(getClass().getResource("images/mal_icon.png")).getScaledInstance(30,25,0);
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        JButton malButton = new JButton();
        malButton.setIcon(new ImageIcon(image));
        malButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(request));
                    }
                    else new ErrorMessage("Pc was not compatible to open browser");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(malButton,BorderLayout.WEST);
        panel.add(buttonPanel);
        return panel;
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
    private JPanel addTitlesToPanelSeasonal(String request,String malButtonRequest){
        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        //TODO         ArrayList<JikanBasicAnimeInfo> animes = new AnimeHTMLParser().getFromTop(request);
        String json = Jaikan.genericRequest(new Endpoint(request));
        MyLogger.log(json);
        JsonElement element = JsonParser.parseString(json);
        if (element != null) {
            JsonObject obj = element.getAsJsonObject();
            JsonArray pictures = obj.get("anime").getAsJsonArray();
            Iterator<JsonElement> listIterator = pictures.iterator();
            boolean wasLegitSearch = false;
            while (listIterator.hasNext()) {
                wasLegitSearch = true;
                JsonObject anime = listIterator.next().getAsJsonObject();
                String title = anime.get("title").getAsString();
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
                JPanel animeTitlePanel = new JPanel(new BorderLayout());
                animeTitlePanel.add(new JLabel(title),BorderLayout.PAGE_START);
                animeTitlePanel.add(imageLabel,BorderLayout.LINE_START);
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


            }
            if(!wasLegitSearch){
                mainPanel = new JPanel(new BorderLayout());
                mainPanel.add(new JLabel("The requested season was not present in the database"),BorderLayout.CENTER);
            }
        }
        else{
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(new JLabel("The requested season was not present in the database"),BorderLayout.CENTER);
        }

        return mainPanel;
    }
    private void addTitleToPanelRecommendations(String request,String malButtonRequest){
        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        addMalButton(mainPanel,malButtonRequest);
        MyRunnable paneLoader = new MyRunnable(mainPanel);

        ArrayList<JikanRecommendationAnime> recommendationAnimes = new AnimeHTMLParser().getRecommendations(jikanAnime);
        if(recommendationAnimes.isEmpty()){
            JPanel panel =new JPanel(new BorderLayout());
            panel.add(new JLabel("There were no recommendations",SwingUtilities.CENTER),BorderLayout.CENTER);
            paneLoader.updatePanel(panel);
        }
            for(JikanRecommendationAnime recommendationAnime:recommendationAnimes) {

                String title = recommendationAnime.getTitle();
                String synopsis = recommendationAnime.getSynopsis();
                int amountOfRecommenders = recommendationAnime.getRecommendationCount();
                String imageURL = recommendationAnime.getImageURL();
                int id = recommendationAnime.getId();
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
                JPanel recommendationPanel = new JPanel(new SpringLayout());
                recommendationPanel.setLayout(new BoxLayout(recommendationPanel,BoxLayout.X_AXIS));
                JPanel recommendationTextPanel = new JPanel(new BorderLayout());
                JPanel recommendationImagePanel = new JPanel(new FlowLayout());
                recommendationImagePanel.add(imageLabel);
                recommendationTextPanel.add(new JLabel(title),BorderLayout.PAGE_START);

                JTextArea textArea = new JTextArea();
                textArea.setText(synopsis);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                //textArea.setSize(new Dimension(300,500));

                recommendationTextPanel.add(textArea,BorderLayout.CENTER);
                recommendationTextPanel.add(new JLabel("Recommended by " + amountOfRecommenders + " people"),BorderLayout.PAGE_END);

                recommendationPanel.add(recommendationImagePanel);
                recommendationPanel.add(recommendationTextPanel);
                recommendationPanel.addMouseListener(new MouseListener() {
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
                paneLoader.updatePanel(recommendationPanel);
                new Thread(paneLoader).start();

            }

    }

    public class MyRunnable implements Runnable{

        private JPanel panel;
        private JScrollPane scrollPane;
        public MyRunnable(JPanel panel){
            scrollPane = new JScrollPane(panel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(30);
            this.panel = panel;
        }
        public void updatePanel(JPanel newPanel){
            panel.add(newPanel);
        }

        @Override
        public void run() {
            mainFrame.setContentPane(scrollPane);
            SwingUtilities.updateComponentTreeUI(mainFrame);
            scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());
            scrollPane.getHorizontalScrollBar().setVisible(false);
            //mainFrame.pack();
            //scrollPane.createVerticalScrollBar();

        }
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
