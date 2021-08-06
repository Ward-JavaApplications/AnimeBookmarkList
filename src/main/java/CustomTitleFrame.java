import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoints;
import pw.mihou.jaikan.models.Anime;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;

public class CustomTitleFrame {
    private JFrame insertFrame;
    private MyGUIManager parent;
    public CustomTitleFrame(JFrame insertFrame,MyGUIManager parent){
        this.insertFrame = insertFrame;
        this.parent = parent;
        insertFrame.setContentPane(loadPanel());
        SwingUtilities.updateComponentTreeUI(insertFrame);
    }

    private JPanel loadPanel(){
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
        JPanel saveButtonPanel = new JPanel(new SpringLayout());
        saveButtonPanel.setLayout(new BoxLayout(saveButtonPanel,BoxLayout.X_AXIS));
        JButton saveButton = new JButton("Search in MAL");
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
                        suggestionPanel(new AnimeTitle(titleField.getText(), status, priority));
                        insertFrame.dispose();
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
        saveButtonPanel.add(saveButton);
        JButton saveWithoutButton = new JButton("Save as custom");
        saveWithoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    System.out.println("Clicked");
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
        saveButtonPanel.add(saveWithoutButton);
        insertPanel.add(saveButtonPanel);
        return insertPanel;

    }
    private void suggestionPanel(AnimeTitle animeTitle){
        new SuggestionFrame(parent,animeTitle);
    }

}

