import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

public class A3 {
    public static void main(String[] args) throws IOException {

        Document document = Jsoup.connect("http://top40weekly.com").get();
        String page_content = document.text();
        System.out.println(page_content);
    }
}
