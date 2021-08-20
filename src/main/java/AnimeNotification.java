import javax.swing.*;
import java.awt.*;

public class AnimeNotification {
    private MyGUIManager parent;
    public AnimeNotification(String title,String url,MyGUIManager parent){
        this.parent = parent;
        JFrame mainFrame = buildFrame();
        mainFrame.setContentPane(getMainPanel(title,url));

    }
    private JFrame buildFrame(){
        JFrame mainFrame = new JFrame("New Anime has been found");
        mainFrame.setSize(400,100);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = ge.getDefaultScreenDevice();
        Rectangle rectangle = screen.getDefaultConfiguration().getBounds();
        int x = (int)rectangle.getMaxX() - mainFrame.getWidth();
        int y = (int)rectangle.getMaxY() - mainFrame.getHeight();
        mainFrame.setLocation(x,y);
        mainFrame.setVisible(true);
        return mainFrame;
    }
    public JPanel getMainPanel(String title,String url){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel infoLabel = new JLabel("The following anime has been found");
        JButton animeButton = new JButton(title);
        animeButton.addActionListener(action ->{
            new NewAnimeFrame(new AnimeHTMLParser().extractIdFromHyper(url),parent);
        });
        panel.add(infoLabel);
        panel.add(animeButton);
        return panel;
    }

    public MyGUIManager getParent() {
        return parent;
    }
}
