import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImagePanel {
    private boolean imageDrawnSuccesfully = true;
    private JFrame frame;
    ImagePanel(String url){
        loadImage(url);

    }
    private void loadImage(String url){
        try {
            frame = buildFrame();

            final BufferedImage image = ImageIO.read(new URL(url));


            JPanel pane = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image.getScaledInstance(200, 200, 0), 0, 0, null);
                }
            };


            frame.add(pane);
            SwingUtilities.updateComponentTreeUI(frame);
        }
        catch (Exception e){
            e.printStackTrace();
            imageDrawnSuccesfully = false;
            frame.dispose();

        }
    }


    private JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        return frame;
    }

    public boolean isImageDrawnSuccesfully() {
        return imageDrawnSuccesfully;
    }
}