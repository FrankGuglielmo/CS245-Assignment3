import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScrape extends Graph{

    public ArrayList<String> artistOfArtists = new ArrayList<>();

    public ArrayList<String> allCollaborations(Graph artistWeb, String artist){

        LinkedList result = artistWeb.collaborations(artist);
        ArrayList<String> allConnections = new ArrayList<String>(result);

        return allConnections;
    }

    //Tried to recursively find all the features of each feature for a given artist
    public void recursiveCollaborations(Graph graph, String artist){
        //Base Case
        ArrayList<String> singleCollaborations = allCollaborations(graph, artist);
        //Recursive Case
        for (String subArtist: singleCollaborations) {
            artistOfArtists.add(subArtist);
            recursiveCollaborations(graph, subArtist);
        }

    }


    public static void main(String[] args) {

        Graph graph = new Graph();

        for (int i = 2012; i < 2013; i++) {
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
                    ArrayList<String> cleanestNames = new ArrayList<>();
                    for (String dirtyName : line) {
                        String clean = dirtyName.strip();
                        cleanestNames.add(clean);
                    }
                    //Call handleAddingArtists
                        //Check to see if each artist is in graph or not, if not, add them
                        //connect each artist to each other with addEdge
                    graph.handleAddingArtists(cleanestNames);
                }
                else if(name.contains("featuring")) {
                    //cleanup
                    String [] line = name.split("featuring");
                    ArrayList<String> cleanestNames = new ArrayList<>();
                    for (String dirtyName : line) {
                        String clean = dirtyName.strip();
                        cleanestNames.add(clean);
                    }
                    //Call handleAddingArtists
                    //Check to see if each artist is in graph or not, if not, add them
                    //connect each artist to each other with addEdge
                    graph.handleAddingArtists(cleanestNames);
                }
                //If line contains an &
                else if(name.contains("&amp;")){
                    //cleanup
                    String [] line = name.split("&amp;");
                    ArrayList<String> cleanestNames = new ArrayList<>();
                    for (String dirtyName : line) {
                        String clean = dirtyName.strip();
                        cleanestNames.add(clean);
                    }
                    //Call handleAddingArtists
                    //Check to see if each artist is in graph or not, if not, add them
                    //connect each artist to each other with addEdge
                    graph.handleAddingArtists(cleanestNames);
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Find recommendations for artist: ");
        String person = scanner.nextLine();
        List condensedArtists = webscrape.allCollaborations(graph, person);
        Set<String> set = new HashSet<>(condensedArtists);
        ArrayList<String> endList = new ArrayList<>(set);

        System.out.println(endList);



    }


}
