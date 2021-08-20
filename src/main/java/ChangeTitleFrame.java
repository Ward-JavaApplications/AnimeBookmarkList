import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeTitleFrame {
    private String oldTitle;
    private MyGUIManager parent;
    private AnimeFrame animeFrameParent;
    private JFrame frame;
    public ChangeTitleFrame(String oldTitle,MyGUIManager parent,AnimeFrame animeFrameParent){
        this.oldTitle = oldTitle;
        this.parent = parent;
        this.animeFrameParent = animeFrameParent;
        frame = createFrame();
        frame.setContentPane(titleFramePanel());
    }

    private JFrame createFrame(){
        frame = new JFrame("Change title of " + oldTitle);
        frame.setSize(500,100);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }
    private JPanel titleFramePanel(){
        JPanel panel = new JPanel(new SpringLayout());
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JLabel titleLabel= new JLabel("Enter new Name");
        panel.add(titleLabel);
        JTextField textField = new JTextField();
        textField.setSize(10,30);
        panel.add(textField);
        JPanel buttonPanel = new JPanel(new SpringLayout());
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        JButton discardButton = new JButton("Discard");
        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();


            }
        });
        buttonPanel.add(discardButton);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newTitle = textField.getText();
                System.out.println("new title is " + newTitle);
                if((newTitle.equals(""))||(newTitle==null)) {

                    JOptionPane.showMessageDialog(null,"Cannot change title to empty");
                }
                else{
                    parent.changeAnimeTitle(oldTitle, newTitle);
                    animeFrameParent.updateTitle(newTitle,parent);
                    frame.dispose();
                }
            }
        });
        buttonPanel.add(saveButton);
        panel.add(buttonPanel);
        return panel;

    }


}
