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

        JLabel loadingImagesPanel = new JLabel("Loading the images...");
        imageFrame.setContentPane(loadingImagesPanel);
        SwingUtilities.updateComponentTreeUI(imageFrame);


        try {
            JPanel picturesPanel = new JPanel(new FlowLayout());

            //images via Jikan
            String request = Jaikan.genericRequest(new Endpoint("https://api.jikan.moe/v3/character/" + anime.getId() + "/pictures"));

            new MyLogger().log(request);
            System.out.println(request);
            if(!request.equals("")) {
                JsonElement element = JsonParser.parseString(request);
                if (element != null) {
                    JsonObject obj = element.getAsJsonObject();
                    JsonArray pictures = obj.get("pictures").getAsJsonArray();
                    Iterator<JsonElement> listIterator = pictures.iterator();
                    while (listIterator.hasNext()) {
                        JsonObject pictureObj = listIterator.next().getAsJsonObject();
                        String largePicture = pictureObj.get("large").getAsString();
                        BufferedImage image = ImageIO.read(new URL(largePicture));
                        picturesPanel.add(new JLabel(new ImageIcon(image)));
                    }
                }
            }

            //images via google
            for(String url:new GoogleImageSearch().getImagesAsLinks(anime.getTitle())){
                try{
                    BufferedImage image = ImageIO.read(new URL(url.substring(7)));
                    picturesPanel.add(new JLabel(new ImageIcon(image)));
                }
                catch (Exception e){
                    String msg  = "Image " + url.substring(7) + " failed to load";
                    System.out.println(msg);
                    new MyLogger().log(msg);
                }
            }

            //display images

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
