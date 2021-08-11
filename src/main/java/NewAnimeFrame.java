import pw.mihou.jaikan.models.Anime;
import pw.mihou.jaikan.models.Dates;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewAnimeFrame{
    private JFrame insertFrame;
    private String title;
    private MyGUIManager parent;
    private JPanel imagePanel;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private NewAnimeFrame animeFrameParent;
    private JTextField priorityTextField;
    private int id;
    private BufferedImage image;
    public NewAnimeFrame(String animeTitle, int id, MyGUIManager parent){
        this.parent = parent;
        this.title = animeTitle;
        this.animeFrameParent = this;
        this.id = id;
        loadFrame();
        startSyncImageSearch();

    }
    public NewAnimeFrame(int id, MyGUIManager parent){
        this.parent = parent;
        this.animeFrameParent = this;
        this.id = id;
        loadFrame();
        startSyncImageSearch();

    }
    private void startSyncImageSearch(){
        JikanAnime anime =  new AnimeHTMLParser().getFromID(id);
        title = anime.getTitle();
        defaultPanel(anime);
        retrieveAnime(anime);
        imagePanel.requestFocus();
        priorityTextField.setText("0");


    }


    private void loadFrame(){
        insertFrame = new JFrame(title);
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(500,900);
        insertFrame.setLocationRelativeTo(null);
        insertFrame.setVisible(true);

        SwingUtilities.updateComponentTreeUI(insertFrame);
        //loadImage(title);


    }


    private void defaultPanel(JikanAnime anime){



        JPanel titlePanel = new JPanel(new SpringLayout());
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JPanel statusPanel = new JPanel(new FlowLayout());

        JPanel titleLabelPannel = new JPanel(new BorderLayout());
        titleLabel = new JLabel(anime.getTitle(), SwingUtilities.CENTER);
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

        CapsuleObject capsuleObject = importAnimePanel(title,anime);
        JPanel insertAnimePanel = capsuleObject.getPanel();

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
                        Desktop.getDesktop().browse(new URI(anime.getUrl()));
                    }
                    else new ErrorMessage("Pc was not compatible to open browser");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        titleLabelPannel.add(titleLabel,BorderLayout.CENTER);
        titleLabelPannel.add(copyButton,BorderLayout.EAST);
        titleLabelPannel.add(malButton,BorderLayout.WEST);
        titlePanel.add(insertAnimePanel,BorderLayout.SOUTH);
        titlePanel.add(titleLabelPannel);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel,BorderLayout.PAGE_START);
        imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(new JLabel("Loading the data"),BorderLayout.CENTER);
        mainPanel.add(imagePanel,BorderLayout.LINE_START);

        insertFrame.setContentPane(mainPanel);
        SwingUtilities.updateComponentTreeUI(insertFrame);
        imagePanel.requestFocus();
        capsuleObject.getTextField().setText("0");



    }
    private CapsuleObject importAnimePanel(String title,JikanAnime anime){
        JPanel mainPanel = new JPanel(new FlowLayout());
        JLabel priorityLabel = new JLabel("priority:");
        priorityTextField = new JTextField();
        //priorityTextField.setFocusable(false);
        priorityTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                priorityTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        mainPanel.add(priorityLabel);
        mainPanel.add(priorityTextField);
        JButton insertAnimeButton = new JButton("Import this anime");
        insertAnimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int priority = Integer.parseInt(priorityTextField.getText());
                    if(!(priority>=0&&priority<=5)) throw new NumberRangeException(0,5);
                    if(!(parent.dataBaseManager.getFromDB("select * from anime where title = \""+title+"\"").isEmpty())) throw new TitleAlreadyPresentException();
                    boolean released = (anime.getAired().getFrom() != null) && anime.getAired().getFrom().getTime() < new Date().getTime();
                    parent.insertNewAnimeInDB(new AnimeTitle(title,"Unwatched",priority,released));
                    if(!released){
                        if(anime.getAired().getFrom() != null)
                        parent.dataBaseManager.insertInUnreleased(title, anime.getAired().getFrom().getTime());
                        else parent.dataBaseManager.insertInUnreleased(title, 0L);
                    }
                    parent.dataBaseManager.putMalID(anime.getTitle(),anime.getId());
                    insertFrame.dispose();
                }
                catch (NumberFormatException numberFormatException)
                {
                    new ErrorMessage("The given priority was not a valid number");
                }
                catch (NumberRangeException numberRangeException){
                    new ErrorMessage("The given priority was not between 0 and 5");
                } catch (TitleAlreadyPresentException titleAlreadyPresentException) {
                    new ErrorMessage("The given title was already present in the database");
                }
            }
        });
        mainPanel.add(insertAnimeButton);


        return new CapsuleObject(mainPanel,priorityTextField);

    }
    public void updateTitle(String newtitle,MyGUIManager parent){
        insertFrame.dispose();
        new AnimeFrame(newtitle,parent);
    }


    public void retrieveAnime(JikanAnime anime) {
        try {

            String animeURL = anime.getImage();
            System.out.println(animeURL);
            MyLogger.log(animeURL);




            image = ImageIO.read(new URL(animeURL));
            JLabel imageToDraw = new JLabel(new ImageIcon(image));


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
                JikanDates airDates = anime.getAired();
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



                imagePanel.add(description);
                imagePanel.add(imageToDraw);
                imagePanel.add(descriptionPanel);



                JButton loadMoreImagesButton = new JButton("Load more images");
                loadMoreImagesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new ExtraImagesFrame(anime);
                    }
                });
                JPanel buttonPanel = new JPanel(new BorderLayout());
                buttonPanel.add(loadMoreImagesButton,BorderLayout.EAST);
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
                    return;

                }
            }




        }
        catch (Exception e){
            e.printStackTrace();
            new ErrorMessage("Couldn't find the anime with id " + id);
        }
    }

    private class CapsuleObject {
        private JPanel panel;
        private JTextField textField;

        public CapsuleObject(JPanel panel, JTextField textField) {
            this.panel = panel;
            this.textField = textField;
        }

        public JPanel getPanel() {
            return panel;
        }

        public JTextField getTextField() {
            return textField;
        }
    }

}


