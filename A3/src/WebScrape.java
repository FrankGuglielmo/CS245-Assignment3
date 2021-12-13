import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class WebScrape {


    public static void main(String[] args) {


        for (int i = 1990; i < 1998; i++) {
            try{
                String year = String.valueOf(i);
                String url = "https://top40weekly.com/" + i + "-all-charts/";
                final Document document = Jsoup.connect(url).get();

                Elements songs = document.select("div.entry-content.content");
                for (int j = 6; j <= 606; j+=12) {

                    for (int k = 0; k < 4; k++) {
                        Elements rowValue = songs.select("p:nth-of-type(" +
                                String.valueOf(j + k) + ")");
                        for (Element val: rowValue) {
                            System.out.println(val);
                        }
                    }
                }

            }
            catch(Exception ex){
                ex.printStackTrace();
            }


            System.out.println();
            System.out.println();
            System.out.println();
        }



    }


}
