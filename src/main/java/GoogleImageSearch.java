import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GoogleImageSearch {
    GoogleImageSearch(){

    }


    public String[] getImagesAsLinks(String title){
        try{
            String url = "https://www.google.com/search?q=" + title;
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc);
            Elements images = doc.select("img[src]");
            ArrayList<String> imagesList = new ArrayList<>(100);
            for(Element e:images){
                imagesList.add(e.attr("src"));
            }
            return imagesList.toArray(new String[0]);

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

//    public void ownTry(){
//        try {
//            String url = "https://www.google.com/search?q=naruto&sxsrf=ALeKk03jnsnO4iM6nvZ2IY55vQQhIKJa9w:1625854393995&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjc1vDMy9bxAhWH6aQKHcR2ClsQ_AUoAXoECAEQAw&biw=1278&bih=1286";
//            Document doc = Jsoup.connect(url).get();
//            Elements images = doc.select("img[src]");
//            for(Element e:images){
//                System.out.print(e.attr("src"));
//                if(displayImage(e.attr("abs:href"))) return;
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//    private void w3spoint(){
//        Document document;
//        String url = "https://www.wikipedia.org/";
//        try {
//            //Get Document object after parsing the html from given url.
//            document = Jsoup.connect(url).get();
//
//            //Get images from document object.
//            Elements images =
//                    document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
//
//            //Iterate images and print image attributes.
//            for (Element image : images) {
//                System.out.println("Image Source: " + image.attr("src"));
//                System.out.println("Image Height: " + image.attr("height"));
//                System.out.println("Image Width: " + image.attr("width"));
//                System.out.println("Image Alt Text: " + image.attr("alt"));
//                System.out.println("");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}