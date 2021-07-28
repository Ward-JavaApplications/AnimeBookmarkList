import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GoogleImageSearch {
    GoogleImageSearch(){
        ownTry();
        //w3spoint();
        //googleSearch("");
        //System.out.println(getImage("Naruto,Wikipedia,Cover"));
    }
    private void googleSearch(String keywords){
        try {
            Document doc = Jsoup
                    .connect("https://www.google.com/search?q=naruto+wikipedia+cover&client=firefox-b-d&sxsrf=ALeKk025ieTa-Lg9SkuInW-uLWztR46tdA:1625585544868&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjVoNCH4s7xAhVBsKQKHbGoAS8Q_AUoAXoECAEQAw&biw=1278&bih=1281")
                    .referrer("https://en.wikipedia.org/")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0")
                    .get();
            Element images = doc.select("[data-src]").first();
            System.out.println(images.attr("abs:data-src"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getImage(String searchParameters) {
        String result = "";
        //String ua = "Mozilla/5.0";
        try {
            String googleUrl = "https://www.google.com/search?tbm=isch&q=" + searchParameters.replace(",", "");
            //Document doc1 = Jsoup.connect(googleUrl).userAgent(ua).timeout(10 * 1000).get();
            Document doc1 = Jsoup.connect(googleUrl).get();
            Element media = doc1.select("[data-src]").first();
            String sourceUrl = media.attr("abs:data-src");

            result = "http://images.google.com/search?tbm=isch&q=" + searchParameters + " image source= " + sourceUrl.replace("&quot", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private boolean displayImage(String imageSrc){
        ImagePanel imagePanel = new ImagePanel(imageSrc);
        return imagePanel.isImageDrawnSuccesfully();
    }

    public void ownTry(){
        try {
            String url = "https://www.google.com/search?q=naruto&sxsrf=ALeKk03jnsnO4iM6nvZ2IY55vQQhIKJa9w:1625854393995&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjc1vDMy9bxAhWH6aQKHcR2ClsQ_AUoAXoECAEQAw&biw=1278&bih=1286";
            Document doc = Jsoup.connect(url).get();
            Elements images = doc.select("img[src]");
            for(Element e:images){
                System.out.print(e.attr("src"));
                if(displayImage(e.attr("abs:href"))) return;

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    private void w3spoint(){
        Document document;
        String url = "https://www.wikipedia.org/";
        try {
            //Get Document object after parsing the html from given url.
            document = Jsoup.connect(url).get();

            //Get images from document object.
            Elements images =
                    document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

            //Iterate images and print image attributes.
            for (Element image : images) {
                System.out.println("Image Source: " + image.attr("src"));
                System.out.println("Image Height: " + image.attr("height"));
                System.out.println("Image Width: " + image.attr("width"));
                System.out.println("Image Alt Text: " + image.attr("alt"));
                System.out.println("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}