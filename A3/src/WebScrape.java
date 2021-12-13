import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScrape {


    public static void main(String[] args) {


        for (int i = 1990; i < 1991; i++) {
            try{
                //Prepare the year for when we connect to URL
                String year = String.valueOf(i);
                String url = "https://top40weekly.com/" + year + "-all-charts/";
                //Get all the data from the given webpage
                final Document document = Jsoup.connect(url).get();
                //Chop our data to get just the div where our song info is
                Elements songs = document.select("div.entry-content.content");
                //Split the lines of the doc into just the strings as seen in the Html
                String [] draftLines = songs.toString().split("(?=<p>)");
                //Chop down even more of the waste to get lines by themselves
                ArrayList<String> secondDraftLines = new ArrayList<>();
                for (int j = 0; j < draftLines.length; j++) {
                    //Split the backend of each of the lines, some had unnecessary data
                    secondDraftLines.addAll(Arrays.asList(draftLines[j].split("(?=</p>)")));
                }
                String regex = "[<][p][>]\\d";
                Pattern pattern = Pattern.compile(regex);
                ArrayList<String> finalLines = new ArrayList<>();
                for (int j = 0; j < secondDraftLines.size(); j++) {
                    Matcher match = pattern.matcher(secondDraftLines.get(j));
                    if(match.find()){
                        //Add all of the lines containing the songs into an ArrayList for String manipulation later
                        finalLines.add(secondDraftLines.get(j));
                        //System.out.println(secondDraftLines.get(j));
                    }
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }



    }


}
