import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
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
                getFromUpcoming();

            }
        });
        menu.add(loadFromUpcoming);
        return menu;
    }

    private void getFromUpcoming(){
        JFrame mainFrame = new JFrame("Get From Upcoming");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(800,800);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        String json = Jaikan.genericRequest(new Endpoint("https://api.jikan.moe/v3/top/anime/1/upcoming"));
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
                }
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        mainFrame.setContentPane(scrollPane);
        SwingUtilities.updateComponentTreeUI(mainFrame);

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
