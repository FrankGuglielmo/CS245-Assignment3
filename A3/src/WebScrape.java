import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScrape extends Graph{

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

                ArrayList<String> artists = new ArrayList<>();
                for (int j = 0; j < finalLines.size(); j++) {

                    String line = finalLines.get(j).replace("–", "(");
                    String [] artistArray = line.split("<br>");
                    artists.addAll(Arrays.asList(artistArray));
                }

                ArrayList<String> artistsFinal = new ArrayList<>();
                for (int j = 0; j < artists.size(); j++) {
                    String [] artistArray = artists.get(j).split("<p>");
                    artistsFinal.addAll(Arrays.asList(artistArray));
                }

                String delimiter = "[(•(] (.*?) [(]";
                pattern = Pattern.compile(delimiter);

                ArrayList<String> artistsOnly = new ArrayList<>();
                for (String item : artistsFinal) {
                    Matcher matcher = pattern.matcher(item);
                    if(matcher.find()){
                        artistsOnly.add(matcher.group(1));
                    }
                }
                //System.out.println(artistsOnly);


                //Make a new graph
                Graph graph = new Graph();

                //Problems with this part: There are a lot of different variations in the data set,
                //some contain featuring, some contain Featuring, &amp;, commas, and some contain one
                //or more of each delimiter. Code needs to chop down the string using all different variations
                //and then add each as a vertex if the vertex doesn't exist, and then link each function
                //using the addEdge function.

                for (String name : artistsOnly) {
                    //If line has multiple artists, split them up and add them as separate nodes
                    if(name.contains("Featuring")){
                        //Splits out all the artists using "Featuring" delimiter, add each artist as a new
                        //vertex in the graph if it doesn't already exist, and then link each one of
                        //them together using addEdge function.

//                        String [] singleArtistFirst = name.split("Featuring");
//
//                        for (String artistDraft : singleArtistFirst) {
//                            if(artistDraft.contains("&amp;")){
//                                String [] singleArtistSecond = name.split("&amp;");
//
//                                for (String item : singleArtistSecond) {
//                                    System.out.println(item);
//                                    if(!graph.artistIndex.contains(item)){
//                                        graph.addVertex(item);
//                                    }
//                                }
//                            }
//                        }
                    }
                    else if(name.contains("&amp;")){
                        //Splits out all the artists using &amp delimiter, add each artist as a new
                        //vertex in the graph if it doesn't already exist, and then link each one of
                        //them together using addEdge function.
                    }
                    else{
                        if(!graph.artistIndex.contains(name)){
                            graph.addVertex(name);
                        }
                    }

                }

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }


    }


}
