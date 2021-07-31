import com.google.gson.*;
import org.json.JSONObject;
import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoint;
import pw.mihou.jaikan.models.Anime;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;

public class ExtraImagesFrame {


    public ExtraImagesFrame(Anime anime) {
        loadImages(anime);
    }

    private void loadImages(Anime anime){
        JFrame imageFrame = new JFrame(anime.getTitle() + " extra images");
        imageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        imageFrame.setSize(800,400);
        imageFrame.setLocationRelativeTo(null);
        imageFrame.setVisible(true);


        try {
            JPanel picturesPanel = new JPanel(new FlowLayout());

            String request = Jaikan.genericRequest(new Endpoint("https://api.jikan.moe/v3/character/" + anime.getId() + "/pictures"));
            new MyLogger().log(request);
            System.out.println(request);
            JsonObject obj = JsonParser.parseString(request).getAsJsonObject();
            JsonArray pictures = obj.get("pictures").getAsJsonArray();
            Iterator<JsonElement> listIterator = pictures.iterator();
            while (listIterator.hasNext()) {
                JsonObject pictureObj = listIterator.next().getAsJsonObject();
                String largePicture = pictureObj.get("large").getAsString();
                BufferedImage image = ImageIO.read(new URL(largePicture));
                picturesPanel.add(new JLabel(new ImageIcon(image)));
            }

            JScrollPane scrollPane = new JScrollPane(picturesPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
            imageFrame.setContentPane(scrollPane);
            SwingUtilities.updateComponentTreeUI(imageFrame);
        }
        catch (Exception e){
            e.printStackTrace();
            new MyLogger().log(e.getMessage());
            imageFrame.setContentPane(new JLabel("Couldn't load additional pictures"));
            SwingUtilities.updateComponentTreeUI(imageFrame);
        }

    }
}
