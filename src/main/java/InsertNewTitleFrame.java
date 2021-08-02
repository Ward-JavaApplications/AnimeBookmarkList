import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;

public class InsertNewTitleFrame {
    private MyGUIManager parent;
    public InsertNewTitleFrame(MyGUIManager parent){
        this.parent = parent;
        JFrame mainFrame =  loadFrame();
        mainFrame.setContentPane(getNewTitleMenu(mainFrame));
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }
    private JFrame loadFrame(){
        JFrame insertFrame = new JFrame("Insert new anime");
        insertFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        insertFrame.setSize(300,200);
        insertFrame.setLocationRelativeTo(null);
        insertFrame.setVisible(true);
        return insertFrame;
    }
    private JPanel getNewTitleMenu(JFrame mainFrame){

        JikanTopRequest jikanTopRequest = new JikanTopRequest(parent);

        JPanel menu = new JPanel(new SpringLayout());
        menu.setLayout(new BoxLayout(menu,BoxLayout.Y_AXIS));
        JButton customTitleButton = new JButton("Insert from custom Title");
        customTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setContentPane(getCustomTitlePanel(mainFrame));
                SwingUtilities.updateComponentTreeUI(mainFrame);
            }
        });
        menu.add(customTitleButton);
        JButton loadFromUpcoming = new JButton("From upcoming anime");
        loadFromUpcoming.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                jikanTopRequest.getTopAnime("Top upcoming anime","https://api.jikan.moe/v3/top/anime/","/upcoming",1);

            }
        });
        menu.add(loadFromUpcoming);
        JButton loadFromFavorite = new JButton("From top favorites all time");
        loadFromFavorite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                jikanTopRequest.getTopAnime("Top favorite allTime","https://api.jikan.moe/v3/top/anime/","/favorite",1);
            }
        });
        menu.add(loadFromFavorite);
        JButton loadByPopularity = new JButton("From top popular all time");
        loadByPopularity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                jikanTopRequest.getTopAnime("Top popularity","https://api.jikan.moe/v3/top/anime/","/bypopularity",1);
            }
        });
        menu.add(loadByPopularity);
        JButton loadByAiringButton = new JButton("From airing anime");
        loadByAiringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                jikanTopRequest.getTopAnime("Top airing anime","https://api.jikan.moe/v3/top/anime/","/airing",1);
            }
        });
        menu.add(loadByAiringButton);
        return menu;
    }



    @NotNull
    private JPanel getCustomTitlePanel(JFrame insertFrame) {
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
                    if(priority<=5 && priority>=0) {
                        parent.insertNewAnimeInDB(new AnimeTitle(titleField.getText(), status, priority));
                        insertFrame.dispose();
                        //refresh();
                    }
                    else throw new NumberRangeException(0,5);
                }
                catch (NumberFormatException numberFormatException)
                {
                    new ErrorMessage("The given priority is not a valid number");
                }
                catch (NumberRangeException numberRangeException){
                    new ErrorMessage("The priority needs to be between: " + numberRangeException.getMinRange() + " and " + numberRangeException.getMaxRange());
                }
            }

        });
        insertPanel.add(saveButton);
        return insertPanel;
    }


}
