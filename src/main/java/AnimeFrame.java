import org.sqlite.SQLiteException;
import pw.mihou.jaikan.models.Anime;
import pw.mihou.jaikan.models.Dates;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnimeFrame implements JaikanRetriever{
    private JFrame insertFrame;
    private String title;
    private MyGUIManager parent;
    private JPanel imagePanel;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private AnimeFrame animeFrameParent;
    private AnimeSearcher animeSearcher;
    public AnimeFrame(String animeTitle, MyGUIManager parent){
        this.parent = parent;
        this.title = animeTitle;
        this.animeFrameParent = this;
        this.animeSearcher = new AnimeSearcher();
        loadFrame();
        new Thread(this::startAsyncImageSearch).start();

    }
    private class AnimeSearcher{
        private String url;
        private boolean hasBeenRequested;
        private AnimeSearcher(){
            url = null;
            hasBeenRequested = false;
        }
        private void loadBrowser(){
            if(url == null){
                hasBeenRequested = true;
            }
            else{
                openBrowser();
            }
        }
        private void setUrl(String newUrl){
            url = newUrl;
            if(hasBeenRequested){
                hasBeenRequested = false;
                openBrowser();
            }
        }
        private void openBrowser(){
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                }
                else new ErrorMessage("Pc was not compatible to open browser");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void startAsyncImageSearch(){
        new JaikanSearch(title,this);
    }


    private void loadFrame(){
        insertFrame = new JFrame(title);
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(600,900);
        insertFrame.setLocationRelativeTo(null);
        insertFrame.setVisible(true);
        defaultPanel();
        SwingUtilities.updateComponentTreeUI(insertFrame);
        //loadImage(title);


    }

    private void defaultPanel(){
        //getStatus of the anime
        String status = parent.dataBaseManager.getStatus(title);

        JPanel titlePanel = new JPanel(new SpringLayout());
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JPanel statusPanel = new JPanel(new FlowLayout());

        JPanel titleLabelPannel = new JPanel(new BorderLayout());
        titleLabel = new JLabel(title, SwingUtilities.CENTER);
        JButton copyButton = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("images/copy icon.png"));
            Image newImg = img.getScaledInstance(20,20,0);
            copyButton.setIcon(new ImageIcon(newImg));
            copyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StringSelection stringSelection = new StringSelection(title);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection,null);
                    copyButton.setIcon(null);
                    copyButton.setText("Copied");
                }
            });
        }
        catch (Exception e){
            MyLogger.log(e.getMessage());
            e.printStackTrace();
        }
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
                animeSearcher.loadBrowser();
            }
        });
        titleLabelPannel.add(titleLabel,BorderLayout.CENTER);
        titleLabelPannel.add(copyButton,BorderLayout.EAST);
        titleLabelPannel.add(malButton,BorderLayout.WEST);
        titlePanel.add(titleLabelPannel);

        JButton watchedButton = new JButton("Watched");
        watchedButton.setBackground(parent.getButtonColorWhenClicked(status,watchedButton));
        watchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setTitleStatus("Watched", title);
                parent.dataBaseManager.setPriorityToZero(title);
                insertFrame.dispose();
                parent.refresh();
            }
        });
        JButton unwatchedButton = new JButton("Unwatched");
        unwatchedButton.setBackground(parent.getButtonColorWhenClicked(status,unwatchedButton));
        unwatchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setTitleStatus("Unwatched", title);
                insertFrame.dispose();
                parent.refresh();
            }
        });
        JButton watchingButton = new JButton("Watching");
        watchingButton.setBackground(parent.getButtonColorWhenClicked(status,watchingButton));
        watchingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setTitleStatus("Watching", title);
                insertFrame.dispose();
                parent.refresh();
            }
        });
        statusPanel.add(watchedButton);
        statusPanel.add(watchingButton);
        statusPanel.add(unwatchedButton);
        titlePanel.add(statusPanel);
        JPanel priorityPanel = new JPanel(new FlowLayout());
        int currentPriority = parent.dataBaseManager.getPriority(title);
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
                    if(!(priority>=0&&priority<=5)) throw new NumberRangeException(0,5);
                                        parent.changePriority(title, priority);
                    insertFrame.dispose();
                    parent.refresh();
                }
                catch (NumberFormatException numberFormatException)
                {
                    new ErrorMessage("The given priority was not a valid number");
                }
                catch (NumberRangeException numberRangeException){
                    new ErrorMessage("The given priority was not between 0 and 5");
                }
            }
        });
        priorityPanel.add(priorityTextField);
        priorityPanel.add(priorityButton);

        JButton changeTitleButton = new JButton("Change title");
        changeTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangeTitleFrame(title,parent,animeFrameParent);
            }
        });
        priorityPanel.add(changeTitleButton);

        JButton deleteButton = new JButton("Delete anime");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("images/sharp_delete_forever_black_24dp.png"));
                }
                catch (Exception exception){
                    exception.printStackTrace();
                    MyLogger.log(exception.getMessage());
                }
                int i = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete " + title,"Confirm delete",JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE,new ImageIcon(img));
                switch (i){
                    case 0:
                        parent.deleteAnime(title) ;
                        insertFrame.dispose();
                        parent.refresh();
                        break;

                }


            }
        });

        priorityPanel.add(deleteButton);
        JButton moveToDangerZoneButton = new JButton("Move to DangerZone");
        moveToDangerZoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("images/silence wench.jpg")).getScaledInstance(250,250,0);
                }
                catch (Exception exception){
                    exception.printStackTrace();
                    MyLogger.log(exception.getMessage());
                }
                int i = JOptionPane.showConfirmDialog(null,"Are you sure you want to move " + title + " to the dangerZone\nThis will remove it from the main list","Confirm move"
                        ,JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE,new ImageIcon(img));
                switch (i){
                    case 0:
                        parent.dataBaseManager.insertInDangerZone(title);
                        parent.dataBaseManager.deleteAnimeEntry(title);
                        insertFrame.dispose();
                        parent.refresh();
                        break;

                }
            }
        });
        priorityPanel.add(moveToDangerZoneButton);
        titlePanel.add(priorityPanel);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel,BorderLayout.PAGE_START);
        imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(new JLabel("Loading the data"),BorderLayout.CENTER);
        mainPanel.add(imagePanel,BorderLayout.LINE_START);

        insertFrame.setContentPane(mainPanel);
        SwingUtilities.updateComponentTreeUI(insertFrame);


    }
    public void updateTitle(String newtitle,MyGUIManager parent){
        insertFrame.dispose();
        new AnimeFrame(newtitle,parent);
    }

    @Override
    public void retrieveAnime(Anime anime) {
        try {

            String animeURL = anime.getImage();
            System.out.println(animeURL);
            MyLogger.log(animeURL);

            animeSearcher.setUrl(anime.getUrl());



            final BufferedImage image = ImageIO.read(new URL(animeURL));
            JLabel imageToDraw = new JLabel(new ImageIcon(image));

            JLabel title = new JLabel("The anime found is " + anime.getTitle());

            String synopsis = anime.getSynopsis();
            if(synopsis == null) synopsis = "No synopsis found";

            JTextArea description = new JTextArea();
            int safetyCounter = 50;
            while(safetyCounter > 0){

                safetyCounter --;

                imagePanel = new JPanel(new FlowLayout());

                description  = new JTextArea();
                description.setSize(insertFrame.getSize().width-18,100);
                // description.setName("Name found: " + anime.getTitle());
                description.setText(synopsis);
                description.setLineWrap(true);
                description.setWrapStyleWord(true);


                StringBuilder allGenres = new StringBuilder();
                for(int i = 0; i < anime.getGenres().size()-1;i++){
                    allGenres.append(anime.getGenres().get(i) + ", ");
                }
                allGenres.append(anime.getGenres().get(anime.getGenres().size()-1));
                JLabel genres = new JLabel("The genres are: " + allGenres.toString());
                JLabel episodeNumber = new JLabel("Amount of episodes: " + anime.getEpisodes());
                JLabel duration = new JLabel("Anime duration: " + anime.getDuration());
                Dates airDates = anime.getAired();
                Date firstDate = airDates.getFrom();
                Date lastDate = airDates.getTo();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                JLabel airDatesLabel;
                if(lastDate != null) {
                    airDatesLabel = new JLabel("The anime aired from " + dateFormat.format(firstDate) + " untill " + dateFormat.format(lastDate));
                }
                else {
                    if(firstDate==null) airDatesLabel = new JLabel("Unknown when the anime will start airing");

                    else airDatesLabel = new JLabel("The anime aired from " + dateFormat.format(firstDate) + " and is still ongoing");
                }
                JLabel ratings = new JLabel("The anime got a score of " + anime.getScore() + " and ranks currently " + anime.getRank() + "th");
                JPanel descriptionPanel = new JPanel(new SpringLayout());
                descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
                descriptionPanel.add(genres);
                descriptionPanel.add(episodeNumber);
                descriptionPanel.add(duration);
                descriptionPanel.add(airDatesLabel);
                descriptionPanel.add(ratings);




                imagePanel.add(title);
                imagePanel.add(description);
                imagePanel.add(imageToDraw);
                imagePanel.add(descriptionPanel);


                JButton recommendationsButton = new JButton("Load recommendations");
                recommendationsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int id = anime.getId();
                        String request = "https://api.jikan.moe/v3/anime/"+id+"/recommendations";
                        new JikanTopRequest(parent).getTopRecommendations(request,anime);

                    }
                });

                JButton loadMoreImagesButton = new JButton("Load more images");
                loadMoreImagesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new ExtraImagesFrame(anime);
                    }
                });
                JPanel buttonPanel = new JPanel(new BorderLayout());
                buttonPanel.add(loadMoreImagesButton,BorderLayout.EAST);
                buttonPanel.add(recommendationsButton,BorderLayout.WEST);
                imagePanel.add(buttonPanel);

                mainPanel.remove(1);
                mainPanel.add(imagePanel);

                insertFrame.setContentPane(mainPanel);
                SwingUtilities.updateComponentTreeUI(insertFrame);

                System.out.println(description.getHeight() + " this was the height of the description");
                MyLogger.log(description.getHeight() + " this was the height of the description");
                if(description.getHeight()<1000){
                    MyLogger.log("This was short enough therefore we will display");
                    System.out.println("This was short enough therefore we will display");
                    if(anime.isAiring()&&!parent.dataBaseManager.airingIsPresent(anime.getTitle())) parent.dataBaseManager.insertInAiring(anime.getTitle());
                    return;

                }
            }



        }
        catch (Exception e){
            e.printStackTrace();
            new ErrorMessage(e.getMessage());
        }
    }

}
