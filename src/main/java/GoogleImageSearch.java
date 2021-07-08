import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleImageSearch {
    GoogleImageSearch(){
        googleSearch("");
        System.out.println(getImage("Naruto,Wikipedia,Cover"));
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


}