import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchAnimeFrame {

    private MyGUIManager parent;
    private JTextField searchField;
    public SearchAnimeFrame(MyGUIManager parent){
        this.parent = parent;
        JFrame frame = loadFrame();
        frame.setContentPane(getSearchPanel(frame));
        searchField.requestFocus();
        SwingUtilities.updateComponentTreeUI(frame);
    }
    private JFrame loadFrame(){
        JFrame mainFrame = new JFrame("Search for title");
        mainFrame.setSize(500,150);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        return mainFrame;
    }

    private JPanel getSearchPanel(JFrame mainFrame){
        JPanel searchPanel = new JPanel(new SpringLayout());
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));
        JPanel returnButtonPanel =  new JPanel(new BorderLayout());
        returnButtonPanel.setMaximumSize(new Dimension(mainFrame.getMaximumSize().width,10));
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
            }
        });
        returnButtonPanel.add(returnButton,BorderLayout.EAST);
        searchPanel.add(returnButtonPanel);
        JLabel searchLabel = new JLabel("Type the name in the box below, better to type less than wrong");
        searchPanel.add(searchLabel);
        searchField = new JTextField();
        searchField.addActionListener(action -> nameConfirmed(mainFrame));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameConfirmed(mainFrame);

            }
        });
        searchPanel.add(searchButton);
        return searchPanel;
    }

    private void nameConfirmed(JFrame mainFrame) {
        parent.setSearchTerm(searchField.getText());
        parent.searchForTitle(searchField.getText(),parent.getMainFrame());
        mainFrame.dispose();
    }

}
