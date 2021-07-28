import pw.mihou.jaikan.models.Anime;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

public class AnimeFrame implements JaikanRetriever{
    private JFrame insertFrame;
    private String title;
    private MyGUIManager parent;
    private JPanel imagePanel;
    private JPanel mainPanel;
    public AnimeFrame(String animeTitle, MyGUIManager parent){
        this.parent = parent;
        this.title = animeTitle;
        loadFrame();
        new Thread(this::startAsyncImageSearch).start();

    }
    private void startAsyncImageSearch(){
        new JaikanSearch(title,this);
    }


    private void loadFrame(){
        insertFrame = new JFrame(title);
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(500,800);
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

        JPanel titleLabelPannel = new JPanel(new FlowLayout());
        JLabel titleLabel = new JLabel(title, SwingUtilities.CENTER);
        titleLabelPannel.add(titleLabel);
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

        JButton deleteButton = new JButton("Delete anime");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.deleteAnime(title) ;
                insertFrame.dispose();
                parent.refresh();

            }
        });
        priorityPanel.add(deleteButton);
        titlePanel.add(priorityPanel);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel,BorderLayout.PAGE_START);
        imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(new JLabel("Loading the data"),BorderLayout.CENTER);
        mainPanel.add(imagePanel,BorderLayout.LINE_START);

        insertFrame.setContentPane(mainPanel);
        SwingUtilities.updateComponentTreeUI(insertFrame);


    }

    @Override
    public void retrieveAnime(Anime anime) {
        try {

            String animeURL = anime.getImage();
            System.out.println(animeURL);

            final BufferedImage image = ImageIO.read(new URL(animeURL));

            imagePanel = new JPanel(new FlowLayout());

            String synopsis = anime.getSynopsis();
            if(synopsis == null) synopsis = "No synopsis found";

            JTextArea description  = new JTextArea(synopsis);
            description.setSize(insertFrame.getWidth()-10,100);
            description.setLineWrap(true);
            description.setWrapStyleWord(true);

            JPanel imageToDrawPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image.getScaledInstance(image.getWidth(), image.getHeight(), 0), Math.round(CENTER_ALIGNMENT*insertFrame.getSize().width), 0, null);
                }
            };
            imagePanel.add(description);
            imagePanel.add(imageToDrawPanel);
            mainPanel.remove(1);
            mainPanel.add(imagePanel);
            //SwingUtilities.updateComponentTreeUI(imagePanel)
            insertFrame.setContentPane(mainPanel);
            SwingUtilities.updateComponentTreeUI(insertFrame);

            insertFrame.setContentPane(imagePanel);
            SwingUtilities.updateComponentTreeUI(insertFrame);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
