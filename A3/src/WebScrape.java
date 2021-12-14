import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScrape extends Graph{


//    public void bfs (int source) throws Exception {
//        boolean [] visited = new boolean[getArtistCount() + 1];
//        bfs(source, visited);
//    }
//    void bfs(int v, boolean [] visited) throws Exception {
//        LinkedQueue q = new LinkedQueue();
//        visited[v] =  true;
//        q.enqueue(v);
//        while(!q.empty())
//            v = (int) q.dequeue();
//        for (String adj : collaborations(v))
//            if(!visited[adj]) {
//                q.enqueue(adj);
//                visited[adj] =  true;
//            }   }


    public ArrayList<String> allCollaborations(Graph artistWeb, String artist){

        LinkedList result = artistWeb.collaborations(artist);
        ArrayList<String> allConnections = new ArrayList<String>(result);

        return allConnections;
    }



    public static void main(String[] args) {

        Graph graph = new Graph();

        for (int i = 2019; i < 2020; i++) {
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

                //Problems with this part: There are a lot of different variations in the data set,
                //some contain featuring, some contain Featuring, &amp;, commas, and some contain one
                //or more of each delimiter. Code needs to chop down the string using all different variations
                //and then add each as a vertex if the vertex doesn't exist, and then link each function
                //using the addEdge function.

                ArrayList<String> artistNames = new ArrayList<>();

                for (String name : artistsOnly) {
                //If the line has featuring and an &
                if(name.contains("Featuring") && name.contains("&amp;")){
                    //cleanup
                    String [] line = name.split("Featuring");
                    for (String dirtyName : line) {
                        String clean = dirtyName.strip();
                        artistNames.add(clean);
                    }
                    ArrayList<String> cleanestNames = new ArrayList<>();
                    for (String cleanestName: artistNames) {
                        if(cleanestName.contains("&amp;")){
                            String [] line2 = cleanestName.split("&amp;");
                            for (String doubleArtist : line2) {
                                String clean2 = doubleArtist.strip();
                                cleanestNames.add(clean2);
                            }
                        }
                        else{
                            cleanestNames.add(cleanestName);
                        }

                    }
                    //Call handleAddingArtists
                    //Check to see if each artist is in graph or not, if not, add them
                    //connect each artist to each other with addEdge
                    graph.handleAddingArtists(cleanestNames);
                }
                else if(name.contains("featuring") && name.contains("&amp;")){
                    //cleanup
                    String [] line = name.split("featuring");
                    for (String dirtyName : line) {
                        String clean = dirtyName.strip();
                        artistNames.add(clean);
                    }
                    ArrayList<String> cleanestNames = new ArrayList<>();
                    for (String cleanestName: artistNames) {
                        if(cleanestName.contains("&amp;")){
                            String [] line2 = cleanestName.split("&amp;");
                            for (String doubleArtist : line2) {
                                String clean2 = doubleArtist.strip();
                                cleanestNames.add(clean2);
                            }
                        }
                        else{
                            cleanestNames.add(cleanestName);
                        }

                    }
                    //Call handleAddingArtists
                    //Check to see if each artist is in graph or not, if not, add them
                    //connect each artist to each other with addEdge
                    graph.handleAddingArtists(cleanestNames);
                    }
                //If line contains a feature
                else if(name.contains("Featuring")){
                    //cleanup
                    String [] line = name.split("Featuring");
                    for (String dirtyName : line) {
                        String clean = dirtyName.strip();
                        artistNames.add(clean);
                    }
                    //Call handleAddingArtists
                        //Check to see if each artist is in graph or not, if not, add them
                        //connect each artist to each other with addEdge
                    graph.handleAddingArtists(artistNames);
                }
                else if(name.contains("featuring")) {
                    //cleanup
                    String [] line = name.split("featuring");
                    for (String dirtyName : line) {
                        String clean = dirtyName.strip();
                        artistNames.add(clean);
                    }
                    //Call handleAddingArtists
                    //Check to see if each artist is in graph or not, if not, add them
                    //connect each artist to each other with addEdge
                    graph.handleAddingArtists(artistNames);
                }
                //If line contains an &
                else if(name.contains("&amp;")){
                    //cleanup
                    String [] line = name.split("&amp;");
                    for (String dirtyName : line) {
                        String clean = dirtyName.strip();
                        artistNames.add(clean);
                    }
                    //Call handleAddingArtists
                    //Check to see if each artist is in graph or not, if not, add them
                    //connect each artist to each other with addEdge
                    graph.handleAddingArtists(artistNames);
                }
                //Line just contains one artist
                else{
                    //no cleanup
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


        WebScrape webscrape = new WebScrape();
        System.out.println(webscrape.allCollaborations(graph, "Justin Bieber"));



    }


}
