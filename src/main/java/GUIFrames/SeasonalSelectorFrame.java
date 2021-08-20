package GUIFrames;

import Exceptions.ErrorMessage;
import Managers.MyGUIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

public class SeasonalSelectorFrame {
    private MyGUIManager parent;
    private JTextField year;
    public SeasonalSelectorFrame(MyGUIManager parent){
        this.parent = parent;
        JFrame frame = loadFrame();
        frame.setContentPane(getMainPanel(frame));
        SwingUtilities.updateComponentTreeUI(frame);
        frame.requestFocus();
        year.setText(String.valueOf(new Date().getYear()+1900));

    }

    private JFrame loadFrame(){
        JFrame mainFrame = new JFrame("Season selector");
        mainFrame.setSize(500,300);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setVisible(true);
        return mainFrame;
    }

    private JPanel getMainPanel(JFrame frame){
        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Select the season which you would like to load"));
        year = new JTextField();
        year.setMinimumSize(new Dimension(20,10));
        year.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                year.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        SpinnerListModel spinnerListModel = new SpinnerListModel(new String[]{"Winter","Spring","Summer","Fall"});
        String seasons[] = {
                "Winter", "Winter", "Spring", "Spring", "Summer", "Summer",
                "Summer", "Summer", "Fall", "Fall", "Winter", "Winter"};
        JSpinner jSpinner = new JSpinner(spinnerListModel);
        String currentSeason = seasons[new Date().getMonth()];
        jSpinner.setValue(currentSeason);
        mainPanel.add(year);
        mainPanel.add(jSpinner);
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(year.getText());
                    frame.dispose();
                    String season = jSpinner.getValue().toString().toLowerCase();
                    new JikanTopRequest(parent).getSeasonAnime("Top from " + season + " " + year.getText(),
                            year.getText(),season);
                }
                catch (NumberFormatException parseException){
                    new ErrorMessage("The given year was " + year.getText() + " and is not a valid number");
                }
            }
        });
        mainPanel.add(continueButton);
    return mainPanel;
    }


}
